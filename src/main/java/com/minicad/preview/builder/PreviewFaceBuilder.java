package com.minicad.preview.builder;

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
import com.minicad.preview.mapper.PreviewUvCoords;
import com.minicad.preview.sampling.PreviewCurveEvaluator;
import com.minicad.preview.sampling.PreviewSurfaceSampler;
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
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepPlanarBox;
import com.minicad.step.model.StepPlanarExtent;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.*;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepFlatPattern;
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
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepPlacementTransformer;
import com.minicad.export.json.StepTypeNameResolver;

/**
 * Face building and geometry collection orchestration for STEP preview export.
 * Extracted from StepPreviewJsonExporter to isolate face and geometry logic.
 */
public final class PreviewFaceBuilder {

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
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    public static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        return true;
    }

    public static FacePayload reverseFacePayload(FacePayload base) {
        List<PointPayload> reversedTriangles = new ArrayList<>(base.triangles().size());
        for (int index = 0; index + 2 < base.triangles().size(); index += 3) {
            reversedTriangles.add(base.triangles().get(index));
            reversedTriangles.add(base.triangles().get(index + 2));
            reversedTriangles.add(base.triangles().get(index + 1));
        }
        return new FacePayload(
                base.stepId(),
                base.name(),
                base.surfaceType(),
                base.origin(),
                new VectorPayload(-base.normal().x(), -base.normal().y(), -base.normal().z()),
                !base.sameSense(),
                base.color(),
                base.transparency(),
                base.pbr(),
                base.layers(),
                base.loops(),
                List.copyOf(reversedTriangles),
                base.surface(),
                base.uvLoops()
        );
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

    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            if (current instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) current;
                current = offsetSurface2.basisSurface();
                continue;
            }
            if (current instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) current;
                current = surfacePatch.basisSurface();
                continue;
            }
            if (current instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) current;
                current = compositeSurface.parentSurface();
                continue;
            }
            if (current instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) current;
                current = machinedSurface.face();
                continue;
            }
            if (current instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) current;
                current = blended.primarySurface();
                continue;
            }
            if (current instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) current;
                current = mappedItem.mappingTarget();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            return current;
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
        if (surface instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) surface;
            return describeUnsupportedPreviewSurface(trimmedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface curveBoundedSurface = (StepCurveBoundedSurface) surface;
            return describeUnsupportedPreviewSurface(curveBoundedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) surface;
            return describeUnsupportedPreviewSurface(orientedSurface.surfaceElement(), builder);
        }
        if (surface instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) surface;
            return describeUnsupportedPreviewSurface(offsetSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) surface;
            return describeUnsupportedPreviewSurface(offsetSurface2.basisSurface(), builder);
        }
        if (surface instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) surface;
            return describeUnsupportedPreviewSurface(surfacePatch.basisSurface(), builder);
        }
        if (surface instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) surface;
            return describeUnsupportedPreviewSurface(compositeSurface.parentSurface(), builder);
        }
        if (surface instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) surface;
            return describeUnsupportedPreviewSurface(machinedSurface.face(), builder);
        }
        if (surface instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) surface;
            return describeUnsupportedPreviewSurface(blended.primarySurface(), builder);
        }
        if (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) {
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
        if (PreviewUvCoords.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > PreviewUvCoords.averageAxialHeight(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = PreviewUvCoords.averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = PreviewUvCoords.averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = PreviewUvCoords.unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = PreviewUvCoords.cylindricalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(PreviewUvCoords.surfacePoint(surface, angles.get(0), lowerHeight)),
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
        if (PreviewUvCoords.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc)) > PreviewUvCoords.averageAxialHeight(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepEdgePayloadBuilder.sampleOrientedEdge(upperArc);
        double lowerHeight = PreviewUvCoords.averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = PreviewUvCoords.averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = PreviewUvCoords.unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = PreviewUvCoords.conicalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(PreviewUvCoords.conicalSurfacePoint(surface, angles.get(0), lowerHeight)),
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
        List<Double> lowerU = PreviewUvCoords.unwrapAngles(surface.position(), lowerPoints);
        double lowerV = PreviewUvCoords.sphericalV(surface.position(), lowerPoints.get(0), surface.radius());
        double upperV = PreviewUvCoords.sphericalV(surface.position(), StepEdgePayloadBuilder.sampleOrientedEdge(upperArc).get(0), surface.radius());
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || lowerU.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateSphericalStrip(surface, lowerV, upperV, lowerU, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = PreviewUvCoords.sphericalNormal(surface.position(), lowerU.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "SPHERICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(PreviewUvCoords.sphericalSurfacePoint(surface.position(), surface.radius(), lowerU.get(0), lowerV)),
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
            List<Double> uValues = unwrapToroidalU(surface, points);
            List<Double> vValues = unwrapToroidalV(surface, points);
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
        if (averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge)) > averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = StepEdgePayloadBuilder.sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = unwrapToroidalU(surface, lowerPoints);
        double lowerV = averageToroidalV(surface, lowerPoints);
        double upperV = averageToroidalV(surface, StepEdgePayloadBuilder.sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = PreviewUvCoords.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepMetadataHelper.faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                PayloadConversionHelper.toPointPayload(PreviewUvCoords.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
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

    public static FacePayload toRectangularCompositeSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRectangularCompositeSurface stepSurface,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        StepEntity basis = stepSurface.parentSurface();
        if (basis instanceof StepCylindricalSurface) {
            StepCylindricalSurface cyl = (StepCylindricalSurface) basis;
            return toCylindricalFacePayload(stepFace, cyl, builder, metadata);
        }
        if (basis instanceof StepConicalSurface) {
            StepConicalSurface cone = (StepConicalSurface) basis;
            return toConicalFacePayload(stepFace, cone, builder, metadata);
        }
        if (basis instanceof StepSphericalSurface) {
            StepSphericalSurface sphere = (StepSphericalSurface) basis;
            return toSphericalFacePayload(stepFace, sphere, builder, metadata);
        }
        if (basis instanceof StepToroidalSurface) {
            StepToroidalSurface torus = (StepToroidalSurface) basis;
            return toToroidalFacePayload(stepFace, torus, builder, metadata);
        }
        if (basis instanceof StepPlane) {
            return toFourSidedPatchFacePayload(stepFace, basis, metadata, builder);
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
        for (FaceBound bound : face.bounds()) {
            if (bound.loop() instanceof EdgeLoop) {
                EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
                for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                    edges.add(orientedEdge.edge());
                }
            }
        }
    }

    public static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.get(0).equals(reversed.get(reversed.size() - 1))) {
            T start = reversed.remove(reversed.size() - 1);
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.get(0));
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
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
                .map(StepPreviewJsonExporter::pointFromStep)
                .collect(Collectors.toList());
        return new EdgePayload(polyline.id(), toPointPayloads(points), null, null);
    }

    public static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .collect(Collectors.toList());
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.get(0).distanceTo(closed.get(closed.size() - 1)) > 1.0e-9) {
            closed.add(closed.get(0));
        }
        return new EdgePayload(polyLoop.id(), toPointPayloads(List.copyOf(closed)), null, null);
    }

    // ─── Shell/vertex utilities ──────────────────────────────────────────

    public static List<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return orientedClosedShell.faces();
        }
        if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return connectedFaceSet.faces();
        }
        if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return connectedFaceSubSet.faces();
        }
        throw new UnsupportedGeometryException(
                "preview export requires shell or connected face set geometry");
    }

    public static boolean isShellEntity(StepEntity entity) {
        return entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell;
    }

    public static boolean isShellLikeEntity(StepEntity entity) {
        return isShellEntity(entity)
                || entity instanceof StepConnectedFaceSet
                || entity instanceof StepConnectedFaceSubSet
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepGeometricSurfaceSet
                || entity instanceof StepPlanarBox
                || entity instanceof StepPlanarExtent
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepSurfacePatch;
    }

    public static PointPayload pointPayloadFromVertex(StepEntity vertex) {
        if (vertex instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) vertex;
            double cx = cp.coordinates().get(0);
            double cy = cp.coordinates().size() > 1 ? cp.coordinates().get(1) : 0.0;
            double cz = cp.coordinates().size() > 2 ? cp.coordinates().get(2) : 0.0;
            return new PointPayload(cx, cy, cz);
        }
        return null;
    }

    public static VectorPayload computeNormal(PointPayload p1, PointPayload p2, PointPayload p3) {
        double nx = (p2.y() - p1.y()) * (p3.z() - p1.z()) - (p2.z() - p1.z()) * (p3.y() - p1.y());
        double ny = (p2.z() - p1.z()) * (p3.x() - p1.x()) - (p2.x() - p1.x()) * (p3.z() - p1.z());
        double nz = (p2.x() - p1.x()) * (p3.y() - p1.y()) - (p2.y() - p1.y()) * (p3.x() - p1.x());
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0e-9) return null;
        return new VectorPayload(nx / len, ny / len, nz / len);
    }

    public static boolean isSampledCurveSource(StepEntity item) {
        return StepValidationHelper.isSampledCurveSource(item);
    }

    public static boolean isStandaloneEdgeSource(StepEntity item) {
        return StepValidationHelper.isStandaloneEdgeSource(item);
    }

    public static StepEntity unwrapStyledItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) current;
                current = styledItem.item();
                continue;
            }
            if (current instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) current;
                current = styledItem.item();
                continue;
            }
            return current;
        }
    }

    public static boolean isRepresentationSolidItem(StepEntity entity) {
        return StepValidationHelper.isRepresentationSolidItem(entity);
    }

    public static ColorPayload toColorPayload(int[] rgb) {
        return PayloadConversionHelper.toColorPayload(rgb);
    }

    public static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        return PayloadConversionHelper.toPbrPayload(metadata);
    }

    public static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        if (surface instanceof Plane) {
            return "PLANE";
        } else if (surface instanceof CylindricalSurface) {
            return "CYLINDRICAL_SURFACE";
        } else if (surface instanceof ConicalSurface) {
            return "CONICAL_SURFACE";
        } else if (surface instanceof SphericalSurface) {
            return "SPHERICAL_SURFACE";
        } else if (surface instanceof ToroidalSurface) {
            return "TOROIDAL_SURFACE";
        } else if (surface instanceof BSplineSurface3) {
            return "BSPLINE_SURFACE";
        } else if (surface instanceof RationalBSplineSurface3) {
            return "RATIONAL_BSPLINE_SURFACE";
        } else if (surface instanceof RuledSurface3) {
            return "RULED_SURFACE";
        } else if (surface instanceof SurfaceOfRevolution3) {
            return "SURFACE_OF_REVOLUTION";
        } else if (surface instanceof OffsetSurface3) {
            return "OFFSET_SURFACE";
        } else if (surface instanceof SurfaceOfLinearExtrusion3) {
            return "SURFACE_OF_LINEAR_EXTRUSION";
        } else if (surface instanceof SurfaceOfConstantRadius3) {
            return "SURFACE_OF_CONSTANT_RADIUS";
        } else if (surface instanceof ParaboloidSurface) {
            return "PARABOLOID_SURFACE";
        } else if (surface instanceof HyperboloidSurface) {
            return "HYPERBOLOID_SURFACE";
        } else if (surface instanceof SurfaceOfTranslation3) {
            return "SURFACE_OF_TRANSLATION";
        } else if (surface instanceof SurfaceOfProjection3) {
            return "SURFACE_OF_PROJECTION";
        } else {
            throw new IllegalArgumentException("Unknown value type: " + surface);
        }
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

    // ─── Cylindrical strip helpers ────────────────────────────────────────

    private static List<PointPayload> triangulateCylindricalStrip(
            CylindricalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }
            CartesianPoint lower0 = PreviewUvCoords.surfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = PreviewUvCoords.surfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = PreviewUvCoords.surfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = PreviewUvCoords.surfacePoint(surface, angle1, upperHeight);
            Vector3 targetNormal = PreviewUvCoords.cylindricalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    // ─── Conical strip triangulation ───────────────────────────────────────

    private static List<PointPayload> triangulateConicalStrip(
            ConicalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }
            CartesianPoint lower0 = PreviewUvCoords.conicalSurfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = PreviewUvCoords.conicalSurfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = PreviewUvCoords.conicalSurfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = PreviewUvCoords.conicalSurfacePoint(surface, angle1, upperHeight);
            Vector3 targetNormal = PreviewUvCoords.conicalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
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
            CartesianPoint p00 = PreviewUvCoords.sphericalSurfacePoint(placement, radius, angle0, lowerV);
            CartesianPoint p10 = PreviewUvCoords.sphericalSurfacePoint(placement, radius, angle1, lowerV);
            CartesianPoint p01 = PreviewUvCoords.sphericalSurfacePoint(placement, radius, angle0, upperV);
            CartesianPoint p11 = PreviewUvCoords.sphericalSurfacePoint(placement, radius, angle1, upperV);
            Vector3 targetNormal = PreviewUvCoords.sphericalNormal(placement, (angle0 + angle1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    // ─── Toroidal strip helpers ──────────────────────────────────────────

    private static List<PointPayload> triangulateToroidalStrip(
            ToroidalSurface surface,
            double lowerV,
            double upperV,
            List<Double> uValues,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < uValues.size() - 1; index++) {
            double u0 = uValues.get(index);
            double u1 = uValues.get(index + 1);
            if (Math.abs(u1 - u0) <= Epsilon.EPS) {
                continue;
            }
            CartesianPoint p00 = PreviewUvCoords.toroidalSurfacePoint(surface, u0, lowerV);
            CartesianPoint p10 = PreviewUvCoords.toroidalSurfacePoint(surface, u1, lowerV);
            CartesianPoint p01 = PreviewUvCoords.toroidalSurfacePoint(surface, u0, upperV);
            CartesianPoint p11 = PreviewUvCoords.toroidalSurfacePoint(surface, u1, upperV);
            Vector3 targetNormal = PreviewUvCoords.toroidalNormal(surface, (u0 + u1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static CartesianPoint toroidalSurfacePoint(ToroidalSurface surface, double u, double v) {
        return toroidalSurfacePoint(surface.position(), surface.majorRadius(), surface.minorRadius(), u, v);
    }

    private static CartesianPoint toroidalSurfacePoint(
            Axis2Placement3D placement,
            double majorRadius,
            double minorRadius,
            double u,
            double v
    ) {
        double radial = majorRadius + minorRadius * Math.cos(v);
        Vector3 xy = placement.xDirection().asVector().scale(Math.cos(u) * radial)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * radial));
        Vector3 z = placement.axis().asVector().scale(minorRadius * Math.sin(v));
        return placement.location().add(xy.add(z));
    }

    private static Vector3 toroidalNormal(ToroidalSurface surface, double u, double v, boolean sameSense) {
        return toroidalNormal(surface.position(), u, v, sameSense);
    }

    private static Vector3 toroidalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * Math.cos(v))
                .add(placement.yDirection().asVector().scale(Math.sin(u) * Math.cos(v)))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static List<Double> unwrapToroidalU(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalU(surface, point);
            if (!values.isEmpty()) {
                double previous = values.get(values.size() - 1);
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static List<Double> unwrapToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalV(surface, point);
            if (!values.isEmpty()) {
                double previous = values.get(values.size() - 1);
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static double averageToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += toroidalV(surface, point);
        }
        return total / points.size();
    }

    private static double toroidalU(ToroidalSurface surface, CartesianPoint point) {
        return toroidalU(surface.position(), point);
    }

    private static double toroidalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static double toroidalV(ToroidalSurface surface, CartesianPoint point) {
        return toroidalV(surface.position(), surface.majorRadius(), point);
    }

    private static double toroidalV(Axis2Placement3D placement, double majorRadius, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        double z = offset.dot(placement.axis().asVector());
        double rho = Math.sqrt(x * x + y * y);
        return Math.atan2(z, rho - majorRadius);
    }

    // ─── Angle/height helpers ────────────────────────────────────────────

    // ─── Triangle orientation ────────────────────────────────────────────

    private static void appendOrientedTriangle(
            List<PointPayload> triangles,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 targetNormal
    ) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.dot(targetNormal) < 0.0) {
            triangles.add(PayloadConversionHelper.toPointPayload(a));
            triangles.add(PayloadConversionHelper.toPointPayload(c));
            triangles.add(PayloadConversionHelper.toPointPayload(b));
            return;
        }
        triangles.add(PayloadConversionHelper.toPointPayload(a));
        triangles.add(PayloadConversionHelper.toPointPayload(b));
        triangles.add(PayloadConversionHelper.toPointPayload(c));
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
