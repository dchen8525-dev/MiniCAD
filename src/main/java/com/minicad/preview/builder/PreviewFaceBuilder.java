package com.minicad.preview.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minicad.export.json.StepPointExtractor;
import com.minicad.export.json.StepMetadataHelper;
import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.export.json.StepValidationHelper;
import com.minicad.geometry.*;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.ShellHelper;
import com.minicad.helper.SurfaceGeometryHelper;
import com.minicad.preview.sampling.PreviewCurveEvaluator;
import com.minicad.preview.sampling.PreviewSurfaceSampler;
import com.minicad.preview.sampling.TriangulationHelper;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.*;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepMachinedSurface;
import com.minicad.step.model.*;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.*;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.VertexLoop;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.PbrPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.VectorPayload;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepEntityUnwrapper;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.export.json.StepPayloadBuilder;
import com.minicad.export.json.StepPlacementTransformer;
import com.minicad.export.json.StepTypeNameResolver;

/**
 * Face building and geometry collection orchestration for STEP preview export.
 * Extracted from StepPreviewJsonExporter to isolate face and geometry logic.
 */
public final class PreviewFaceBuilder {

    private static final Logger log = LoggerFactory.getLogger(PreviewFaceBuilder.class);

    private static final int TOPOLOGY_SURFACE_GRID_SEGMENTS = 16;

    private PreviewFaceBuilder() {}

    // ─── Core face building ──────────────────────────────────────────────

    public static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).collect(Collectors.toList());
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.get(0);
            return List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return bounds;
    }

    public static StepEntity faceGeometry(StepFaceEntity stepFace) {
        return StepGeometryHelper.faceGeometry(stepFace);
    }

    public static boolean faceSameSense(StepFaceEntity stepFace) {
        return StepValidationHelper.faceSameSense(stepFace);
    }

    public static UnsupportedFacePayload toUnsupportedFacePayload(StepFaceEntity stepFace, String reason) {
        StepEntity geometry = faceGeometry(stepFace);
        return new UnsupportedFacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                StepTypeNameResolver.surfaceTypeName(geometry),
                reason == null ? "preview export returned no mesh" : reason
        );
    }

    // ─── Surface unwrapping ──────────────────────────────────────────────

    private record SurfaceUnwrapRule(
            Class<?> type,
            Predicate<StepEntity> guard,
            Function<StepEntity, StepEntity> basis
    ) {
        boolean matches(StepEntity surface) {
            return type.isInstance(surface) && (guard == null || guard.test(surface));
        }
    }

    private static SurfaceUnwrapRule unwrapRule(Class<?> type, Function<StepEntity, StepEntity> basis) {
        return new SurfaceUnwrapRule(type, null, basis);
    }

    /**
     * Wrapper-surface types unwrapped to the surface they reference. Shared by
     * unwrapParametricPreviewSurface and describeUnsupportedPreviewSurface;
     * replaces their two former 11-branch if/else-if chains.
     */
    private static final List<SurfaceUnwrapRule> SURFACE_UNWRAP_RULES = List.of(
            unwrapRule(StepRectangularTrimmedSurface.class, surface -> ((StepRectangularTrimmedSurface) surface).basisSurface()),
            unwrapRule(StepCurveBoundedSurface.class, surface -> ((StepCurveBoundedSurface) surface).basisSurface()),
            unwrapRule(StepOrientedSurface.class, surface -> ((StepOrientedSurface) surface).surfaceElement()),
            unwrapRule(StepOffsetSurface.class, surface -> ((StepOffsetSurface) surface).basisSurface()),
            unwrapRule(StepOffsetSurface2.class, surface -> ((StepOffsetSurface2) surface).basisSurface()),
            unwrapRule(StepSurfacePatch.class, surface -> ((StepSurfacePatch) surface).basisSurface()),
            unwrapRule(StepRectangularCompositeSurface.class, surface -> ((StepRectangularCompositeSurface) surface).parentSurface()),
            unwrapRule(StepMachinedSurface.class, surface -> ((StepMachinedSurface) surface).face()),
            unwrapRule(StepBlendedSurface.class, surface -> ((StepBlendedSurface) surface).primarySurface()),
            unwrapRule(StepMappedItem.class, surface -> ((StepMappedItem) surface).mappingTarget()),
            new SurfaceUnwrapRule(StepGeometricReplica.class,
                    surface -> "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()),
                    surface -> ((StepGeometricReplica) surface).parent())
    );

    /** Basis surface of a wrapper type, or null when the surface is terminal. */
    private static StepEntity unwrapBasisSurface(StepEntity surface) {
        for (SurfaceUnwrapRule rule : SURFACE_UNWRAP_RULES) {
            if (rule.matches(surface)) {
                return rule.basis().apply(surface);
            }
        }
        return null;
    }

    /** Single-level unwrap access for other classes reusing SURFACE_UNWRAP_RULES. */
    public static StepEntity unwrapBasisSurfaceOnce(StepEntity surface) {
        return unwrapBasisSurface(surface);
    }

    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            StepEntity basis = unwrapBasisSurface(current);
            if (basis == null) {
                return current;
            }
            current = basis;
        }
        return current;
    }

    public static String describeUnsupportedPreviewSurface(StepEntity surface) {
        return describeUnsupportedPreviewSurface(surface, null);
    }

    public static String describeUnsupportedPreviewSurface(StepEntity surface, StepCadBuilder builder) {
        if (surface == null) {
            return null;
        }
        if (surface instanceof StepGeometricReplica
                && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) surface;
            if (replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator) { com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) replica.transformation();
                double scale = transformation.scale() == null ? 1.0 : transformation.scale();
                if (Math.abs(scale) <= 1.0e-9) {
                    return "SURFACE_REPLICA zero scale preview is unsupported";
                }
                if (builder != null) {
                    double[] matrix = StepPlacementTransformer.matrixForTransformationOperator(transformation, builder);
                    if (MathUtilityHelper.inverseUniformScaleTransform(matrix) == null) {
                        return "SURFACE_REPLICA non-uniform scale preview is unsupported";
                    }
                }
            }
            return describeUnsupportedPreviewSurface(replica.parent(), builder);
        }
        StepEntity basis = unwrapBasisSurface(surface);
        if (basis != null) {
            return describeUnsupportedPreviewSurface(basis, builder);
        }
        return StepTypeNameResolver.surfaceTypeName(surface);
    }

    // ─── Surface-specific face payload builders ──────────────────────────

    public static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }

        if (!(bounds.get(0).loop() instanceof EdgeLoop)) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.cylindricalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.surfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "cylindrical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        null,
                        lowerHeight,
                        upperHeight,
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    public static FacePayload toConicalFacePayload(
            StepFaceEntity stepFace,
            StepConicalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.conicalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.conicalSurfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "conical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        surface.semiAngle(),
                        lowerHeight,
                        upperHeight,
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    public static FacePayload toSphericalFacePayload(
            StepFaceEntity stepFace,
            StepSphericalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        SphericalSurface surface = builder.buildSphericalSurface(stepSurface.id());
        OrientedEdge lowerArc = outerLoop.edges().get(0);
        OrientedEdge upperArc = outerLoop.edges().get(2);

        List<CartesianPoint> lowerPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<Double> lowerU = SurfaceGeometryHelper.unwrapAngles(surface.position(), lowerPoints);
        double lowerV = SurfaceGeometryHelper.sphericalV(surface.position(), lowerPoints.get(0), surface.radius());
        double upperV = SurfaceGeometryHelper.sphericalV(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(upperArc).get(0), surface.radius());
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || lowerU.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateSphericalStrip(surface, lowerV, upperV, lowerU, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.sphericalNormal(surface.position(), lowerU.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SPHERICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.sphericalSurfacePoint(surface.position(), surface.radius(), lowerU.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "spherical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        null,
                        lowerV,
                        upperV,
                        lowerU.get(0),
                        lowerU.get(lowerU.size() - 1) - lowerU.get(0),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    public static FacePayload toToroidalFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = StepEdgePayloadBuilder.sampleOrientedEdge(edge);
            List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, points);
            List<Double> vValues = SurfaceGeometryHelper.unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.get(uValues.size() - 1) - uValues.get(0));
            double vRange = Math.abs(vValues.get(vValues.size() - 1) - vValues.get(0));
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.get(0);
        OrientedEdge upperVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
        if (SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge)) > SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, lowerPoints);
        double lowerV = SurfaceGeometryHelper.averageToroidalV(surface, lowerPoints);
        double upperV = SurfaceGeometryHelper.averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                new FaceSurfacePayload(
                        "toroidal_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.majorRadius(),
                        surface.minorRadius(),
                        null,
                        lowerV,
                        upperV,
                        uValues.get(0),
                        uValues.get(uValues.size() - 1) - uValues.get(0),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    public static FacePayload toRationalBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRationalBSplineSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(stepSurface.id());
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(
                PreviewSurfaceSampler.sampleSurfaceGrid(surface, 16, 16),
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "RATIONAL_B_SPLINE_SURFACE",
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    public static FacePayload toRuledSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRuledSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        RuledSurface3 surface = builder.buildRuledSurface(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "RULED_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "ruled_surface", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null
                ),
                null
        );
    }

    public static FacePayload toFourSidedPatchFacePayload(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        SurfacePatch patch = PreviewSurfaceSampler.buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulatePatch(patch, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = patch.normalAt(0.5, 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                StepTypeNameResolver.surfaceTypeName(geometry),
                PayloadConversionHelper.toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    public static FacePayload toSurfaceOfLinearExtrusionFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SURFACE_OF_LINEAR_EXTRUSION",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "linear_extrusion", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null
                ),
                null
        );
    }

    public static FacePayload toSurfaceOfRevolutionFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SURFACE_OF_REVOLUTION",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "surface_of_revolution", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null
                ),
                null
        );
    }

    public static FacePayload toOffsetSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepOffsetSurface2 stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        // Build the basis surface and wrap in OffsetSurface3
        SurfaceGeometry baseGeometry = builder.buildSurfaceGeometry(stepSurface.basisSurface().id());
        OffsetSurface3 surface = new OffsetSurface3(baseGeometry, stepSurface.distance());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "OFFSET_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "offset_surface", null, null, null, 0.0, null, surface.distance(),
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null
                ),
                null
        );
    }

    public static FacePayload toFreeFormSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepFreeFormSurface stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        BSplineSurface3 surface = PreviewSurfaceSampler.buildFreeFormSurface(stepSurface, builder);
        int uSegments = Math.max(surface.uKnots().size() - 1, 10);
        int vSegments = Math.max(surface.vKnots().size() - 1, 10);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(
                PreviewSurfaceSampler.sampleSurfaceGrid(surface, uSegments, vSegments),
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "FREE_FORM_SURFACE",
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    public static FacePayload toConeFacePayload(
            StepFaceEntity stepFace,
            StepConicalSurfaceWithEllipticalAxis stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        try {
            SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
            return toSampledSurfaceFacePayload(stepFace, surface, "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS",
                    buildFaceBounds(stepFace, builder), metadata);
        } catch (Exception ex) {
            // Recoverable degradation: conical-elliptical face build failed, signal and return null.
            log.warn("PreviewFaceBuilder conical-elliptical face build failed; returning null", ex);
            return null;
        }
    }

    public static FacePayload toParaboloidFacePayload(
            StepFaceEntity stepFace,
            StepParaboloidSurface stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "PARABOLOID_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    public static FacePayload toHyperboloidFacePayload(
            StepFaceEntity stepFace,
            StepHyperboloidSurface stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "HYPERBOLOID_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    public static FacePayload toSurfaceOfTranslationFacePayload(
            StepFaceEntity stepFace,
            StepSurfaceOfTranslation stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SURFACE_OF_TRANSLATION",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    public static FacePayload toSurfaceOfProjectionFacePayload(
            StepFaceEntity stepFace,
            StepSurfaceOfProjection stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SURFACE_OF_PROJECTION",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    /**
     * Composite-surface basis rules keyed by concrete basis type, replacing the
     * former 5-branch if/else-if chain (first match wins, mirrors the original
     * sequential ifs). Each rule casts the basis surface and delegates to the
     * matching face-payload builder; a basis matching no rule returns null, as
     * the old trailing statement did. All 5 types are final direct StepEntity
     * implementations (no subtype relation today), so the order is behaviour
     * neutral but frozen by composite-basis-face-dispatch-order.txt. The Plane
     * rule keeps the original call shape (passes the uncast basis plus swapped
     * metadata/builder argument order to toFourSidedPatchFacePayload).
     */
    @FunctionalInterface
    private interface CompositeBasisFaceHandler {
        FacePayload build(StepFaceEntity stepFace, StepEntity basis, StepCadBuilder builder,
                StepMetadataExtractor.DisplayMetadata metadata);
    }

    private record CompositeBasisFaceRule(
            Class<? extends StepEntity> type, CompositeBasisFaceHandler handler) {
        boolean matches(StepEntity basis) {
            return type.isInstance(basis);
        }
    }

    private static CompositeBasisFaceRule compositeBasisFaceRule(
            Class<? extends StepEntity> type, CompositeBasisFaceHandler handler) {
        return new CompositeBasisFaceRule(type, handler);
    }

    private static final List<CompositeBasisFaceRule> COMPOSITE_BASIS_FACE_RULES = List.of(
            compositeBasisFaceRule(StepCylindricalSurface.class, (stepFace, basis, builder, metadata) ->
                    toCylindricalFacePayload(stepFace, (StepCylindricalSurface) basis, builder, metadata)),
            compositeBasisFaceRule(StepConicalSurface.class, (stepFace, basis, builder, metadata) ->
                    toConicalFacePayload(stepFace, (StepConicalSurface) basis, builder, metadata)),
            compositeBasisFaceRule(StepSphericalSurface.class, (stepFace, basis, builder, metadata) ->
                    toSphericalFacePayload(stepFace, (StepSphericalSurface) basis, builder, metadata)),
            compositeBasisFaceRule(StepToroidalSurface.class, (stepFace, basis, builder, metadata) ->
                    toToroidalFacePayload(stepFace, (StepToroidalSurface) basis, builder, metadata)),
            compositeBasisFaceRule(StepPlane.class, (stepFace, basis, builder, metadata) ->
                    toFourSidedPatchFacePayload(stepFace, basis, metadata, builder))
    );

    public static FacePayload toRectangularCompositeSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRectangularCompositeSurface stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        StepEntity basis = stepSurface.parentSurface();
        for (CompositeBasisFaceRule rule : COMPOSITE_BASIS_FACE_RULES) {
            if (rule.matches(basis)) {
                return rule.handler().build(stepFace, basis, builder, metadata);
            }
        }
        return null;
    }

    // ─── Geometry collection orchestration (delegates to PreviewGeometryCollector) ───

    public static GeometryCollection buildLegacyGeometry(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        return PreviewGeometryCollector.buildLegacyGeometry(resolved, builder, metadata);
    }

    public static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        return PreviewGeometryCollector.buildGeometryForShells(shellIds, resolved, builder, metadata, inheritedShellMetadata);
    }

    public static GeometryCollection buildGeometryForSolids(
            Set<Integer> solidIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedSolidMetadata
    ) {
        return PreviewGeometryCollector.buildGeometryForSolids(solidIds, resolved, builder, metadata, inheritedSolidMetadata);
    }

    public static GeometryCollection mergeGeometry(GeometryCollection left, GeometryCollection right) {
        return PreviewGeometryCollector.mergeGeometry(left, right);
    }

    public static void collectShellLikeIds(StepEntity item, Set<Integer> shellIds) {
        PreviewGeometryCollector.collectShellLikeIds(item, shellIds);
    }

    public static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        PreviewGeometryCollector.collectStandaloneEdges(item, edges, resolved, builder, metadata);
    }

    public static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        return PreviewGeometryCollector.buildMappedRepresentationGeometry(representation, resolved, builder, metadata, visitingRepresentations);
    }

    public static GeometryCollection buildRelatedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        return PreviewGeometryCollector.buildRelatedRepresentationGeometry(representation, resolved, builder, metadata, visitingRepresentations);
    }

    public static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        return PreviewGeometryCollector.expandMappedItemGeometry(mappedItem, resolved, builder, metadata, visitingRepresentations);
    }

    public static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        return PreviewGeometryCollector.collectRepresentationShells(representation, resolved);
    }

    public static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        return PreviewGeometryCollector.collectRepresentationSolids(representation, resolved);
    }

    // ─── Edge/loop building ──────────────────────────────────────────────

    public static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) bound.loop();
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) bound.loop();
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
                sampled.add(sampled.get(0));
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = StepEdgePayloadBuilder.sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
            sampled.add(sampled.get(0));
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    public static void collectTopologyEdges(Face face, Set<Edge> edges) {
        StepPayloadBuilder.collectTopologyEdges(face, edges);
    }

    public static <T> List<T> reverseClosedLoop(List<T> points) {
        return StepPayloadBuilder.reverseClosedLoop(points);
    }

    public static ColorPayload resolveEdgeColor(int edgeId, StepMetadataExtractor metadata) {
        StepMetadataExtractor.DisplayMetadata meta = metadata.forItem(edgeId);
        return meta.rgb() != null ? toColorPayload(meta.rgb()) : null;
    }

    public static EdgePayload buildTopologyEdgePayload(int edgeId, Edge edge) {
        return new EdgePayload(
                edgeId,
                toPointPayloads(sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense())),
                null,
                null
        );
    }

    public static EdgePayload toPolylineEdgePayload(StepPolyline polyline) {
        List<CartesianPoint> points = polyline.points().stream()
                .map(StepPointExtractor::pointFromStep)
                .collect(Collectors.toList());
        return new EdgePayload(polyline.id(), toPointPayloads(points), null, null);
    }

    public static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPointExtractor::pointFromStep)
                .collect(Collectors.toList());
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.get(0).distanceTo(closed.get(closed.size() - 1)) > 1.0e-9) {
            closed.add(closed.get(0));
        }
        return new EdgePayload(polyLoop.id(), toPointPayloads(List.copyOf(closed)), null, null);
    }

    // ─── Shell/vertex utilities ──────────────────────────────────────────

    public static List<StepFaceEntity> shellFaces(StepEntity entity) {
        return ShellHelper.shellFaces(entity);
    }

    public static boolean isShellEntity(StepEntity entity) {
        return ShellHelper.isShellEntity(entity);
    }

    public static boolean isShellLikeEntity(StepEntity entity) {
        return ShellHelper.isShellLikeEntity(entity);
    }

    public static VectorPayload computeNormal(PointPayload p1, PointPayload p2, PointPayload p3) {
        double nx = (p2.y() - p1.y()) * (p3.z() - p1.z()) - (p2.z() - p1.z()) * (p3.y() - p1.y());
        double ny = (p2.z() - p1.z()) * (p3.x() - p1.x()) - (p2.x() - p1.x()) * (p3.z() - p1.z());
        double nz = (p2.x() - p1.x()) * (p3.y() - p1.y()) - (p2.y() - p1.y()) * (p3.x() - p1.x());
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0e-9) return null;
        return new VectorPayload(nx / len, ny / len, nz / len);
    }

    public static StepEntity unwrapStyledItem(StepEntity item) {
        return StepEntityUnwrapper.unwrapStyledItem(item);
    }

    public static ColorPayload toColorPayload(int[] rgb) {
        return PayloadConversionHelper.toColorPayload(rgb);
    }

    public static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        return PayloadConversionHelper.toPbrPayload(metadata);
    }

    private record SurfaceTypeNameEntry(Class<?> type, String name) {
    }

    /**
     * Geometry surface type names, replacing the former 16-branch if/else-if
     * chain. Order mirrors the original chain (first match wins), which also
     * preserves the old resolution if subtype overlaps ever appear.
     */
    private static final List<SurfaceTypeNameEntry> SURFACE_TYPE_NAMES = List.of(
            new SurfaceTypeNameEntry(Plane.class, "PLANE"),
            new SurfaceTypeNameEntry(CylindricalSurface.class, "CYLINDRICAL_SURFACE"),
            new SurfaceTypeNameEntry(ConicalSurface.class, "CONICAL_SURFACE"),
            new SurfaceTypeNameEntry(SphericalSurface.class, "SPHERICAL_SURFACE"),
            new SurfaceTypeNameEntry(ToroidalSurface.class, "TOROIDAL_SURFACE"),
            new SurfaceTypeNameEntry(BSplineSurface3.class, "BSPLINE_SURFACE"),
            new SurfaceTypeNameEntry(RationalBSplineSurface3.class, "RATIONAL_BSPLINE_SURFACE"),
            new SurfaceTypeNameEntry(RuledSurface3.class, "RULED_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfRevolution3.class, "SURFACE_OF_REVOLUTION"),
            new SurfaceTypeNameEntry(OffsetSurface3.class, "OFFSET_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfLinearExtrusion3.class, "SURFACE_OF_LINEAR_EXTRUSION"),
            new SurfaceTypeNameEntry(SurfaceOfConstantRadius3.class, "SURFACE_OF_CONSTANT_RADIUS"),
            new SurfaceTypeNameEntry(ParaboloidSurface.class, "PARABOLOID_SURFACE"),
            new SurfaceTypeNameEntry(HyperboloidSurface.class, "HYPERBOLOID_SURFACE"),
            new SurfaceTypeNameEntry(SurfaceOfTranslation3.class, "SURFACE_OF_TRANSLATION"),
            new SurfaceTypeNameEntry(SurfaceOfProjection3.class, "SURFACE_OF_PROJECTION")
    );

    public static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        for (SurfaceTypeNameEntry entry : SURFACE_TYPE_NAMES) {
            if (entry.type().isInstance(surface)) {
                return entry.name();
            }
        }
        throw new IllegalArgumentException("Unknown value type: " + surface);
    }

    // ─── Local helper methods (copied from StepPreviewJsonExporter) ──────
    // These are private in StepPreviewJsonExporter and cannot be accessed.
    // Duplicated here to keep StepPreviewJsonExporter unchanged.

    private static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PreviewFaceBuilder::toPointPayload).collect(Collectors.toList());
    }

    private static List<PointPayload> triangulateSurfaceGrid(List<List<CartesianPoint>> grid, boolean sameSense) {
        return PreviewSurfaceSampler.triangulateSurfaceGrid(grid, sameSense);
    }

    private static List<PointPayload> triangulatePatch(SurfacePatch patch, boolean sameSense) {
        return PreviewSurfaceSampler.triangulatePatch(patch, sameSense);
    }

    private static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = Direction3.from(reference.subtract(axis.scale(reference.dot(axis))).normalize());
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // ─── Spherical strip triangulation ────────────────────────────────────

    private static List<PointPayload> triangulateSphericalStrip(
            SphericalSurface surface,
            double lowerV,
            double upperV,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) continue;
            CartesianPoint p00 = SurfaceGeometryHelper.sphericalSurfacePoint(placement, radius, angle0, lowerV);
            CartesianPoint p10 = SurfaceGeometryHelper.sphericalSurfacePoint(placement, radius, angle1, lowerV);
            CartesianPoint p01 = SurfaceGeometryHelper.sphericalSurfacePoint(placement, radius, angle0, upperV);
            CartesianPoint p11 = SurfaceGeometryHelper.sphericalSurfacePoint(placement, radius, angle1, upperV);
            Vector3 targetNormal = SurfaceGeometryHelper.sphericalNormal(placement, (angle0 + angle1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            TriangulationHelper.appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            TriangulationHelper.appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    // ─── Sample edge (delegates to PreviewCurveEvaluator) ────────────────

    private static List<CartesianPoint> sampleEdge(
            CartesianPoint start,
            CartesianPoint end,
            Curve3 curve,
            boolean sameSense
    ) {
        return PreviewCurveEvaluator.sampleEdge(start, end, curve, sameSense);
    }

    // ─── Generic parametric/sampled face payload helpers ─────────────────

    public static FacePayload toParametricSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            String surfaceTypeName,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                surfaceTypeName,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    public static FacePayload toSampledSurfaceFacePayload(
            StepFaceEntity stepFace,
            SurfaceGeometry surface,
            String surfaceType,
            List<FaceBound> bounds,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        int segments = 32;
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(segments, segments);
        if (grid.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                surfaceType,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }
}
