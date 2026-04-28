package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.*;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.annotation.StepAnnotationFillArea;
import com.minicad.step.model.annotation.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSymbol;
import com.minicad.step.model.annotation.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.annotation.StepAnnotationText;
import com.minicad.step.model.annotation.StepAnnotationTextCharacter;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.annotation.StepOverRidingStyledItem;
import com.minicad.step.model.annotation.StepPlanarBox;
import com.minicad.step.model.annotation.StepPlanarExtent;
import com.minicad.step.model.annotation.StepStyledItem;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.manufacturing.StepChamferEdge;
import com.minicad.step.model.manufacturing.StepFilletEdge;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.product.*;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.topology.*;
import com.minicad.step.model.workflow.StepRepresentation;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Face building and geometry collection orchestration for STEP preview export.
 * Extracted from StepPreviewJsonExporter to isolate face and geometry logic.
 */
public final class PreviewFaceBuilder {

    private static final int TOPOLOGY_SURFACE_GRID_SEGMENTS = 16;

    private PreviewFaceBuilder() {}

    // ─── Core face building ──────────────────────────────────────────────

    public static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).toList();
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.getFirst();
            return List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return bounds;
    }

    public static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    public static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                StepPreviewJsonExporter.surfaceTypeName(geometry),
                reason == null ? "preview export returned no mesh" : reason
        );
    }

    // ─── Surface unwrapping ──────────────────────────────────────────────

    public static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            if (current instanceof StepRectangularTrimmedSurface trimmedSurface) {
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface boundedSurface) {
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface orientedSurface) {
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface offsetSurface) {
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOffsetSurface2 offsetSurface2) {
                current = offsetSurface2.basisSurface();
                continue;
            }
            if (current instanceof StepSurfacePatch surfacePatch) {
                current = surfacePatch.basisSurface();
                continue;
            }
            if (current instanceof StepRectangularCompositeSurface compositeSurface) {
                current = compositeSurface.parentSurface();
                continue;
            }
            if (current instanceof StepMachinedSurface machinedSurface) {
                current = machinedSurface.face();
                continue;
            }
            if (current instanceof StepBlendedSurface blended) {
                current = blended.primarySurface();
                continue;
            }
            if (current instanceof StepMappedItem mappedItem) {
                current = mappedItem.mappingTarget();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
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
        if (surface instanceof StepRectangularTrimmedSurface trimmedSurface) {
            return describeUnsupportedPreviewSurface(trimmedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepCurveBoundedSurface curveBoundedSurface) {
            return describeUnsupportedPreviewSurface(curveBoundedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOrientedSurface orientedSurface) {
            return describeUnsupportedPreviewSurface(orientedSurface.surfaceElement(), builder);
        }
        if (surface instanceof StepOffsetSurface offsetSurface) {
            return describeUnsupportedPreviewSurface(offsetSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOffsetSurface2 offsetSurface2) {
            return describeUnsupportedPreviewSurface(offsetSurface2.basisSurface(), builder);
        }
        if (surface instanceof StepSurfacePatch surfacePatch) {
            return describeUnsupportedPreviewSurface(surfacePatch.basisSurface(), builder);
        }
        if (surface instanceof StepRectangularCompositeSurface compositeSurface) {
            return describeUnsupportedPreviewSurface(compositeSurface.parentSurface(), builder);
        }
        if (surface instanceof StepMachinedSurface machinedSurface) {
            return describeUnsupportedPreviewSurface(machinedSurface.face(), builder);
        }
        if (surface instanceof StepBlendedSurface blended) {
            return describeUnsupportedPreviewSurface(blended.primarySurface(), builder);
        }
        if (surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            if (replica.transformation() instanceof com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation) {
                double scale = transformation.scale() == null ? 1.0 : transformation.scale();
                if (Math.abs(scale) <= 1.0e-9) {
                    return "SURFACE_REPLICA zero scale preview is unsupported";
                }
                if (builder != null) {
                    double[] matrix = StepPreviewJsonExporter.matrixForTransformationOperator(transformation, builder);
                    if (StepPreviewJsonExporter.inverseUniformScaleTransform(matrix) == null) {
                        return "SURFACE_REPLICA non-uniform scale preview is unsupported";
                    }
                }
            }
            return describeUnsupportedPreviewSurface(replica.parent(), builder);
        }
        return StepPreviewJsonExporter.surfaceTypeName(surface);
    }

    // ─── Surface-specific face payload builders ──────────────────────────

    public static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }

        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop)) {
            return null;
        }
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface, StepPreviewJsonExporter.sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface, StepPreviewJsonExporter.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = StepPreviewJsonExporter.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepPreviewJsonExporter.sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = cylindricalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                StepPreviewJsonExporter.toPointPayload(surfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                        angles.getFirst(),
                        angles.getLast() - angles.getFirst(),
                        null,
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface.position(), StepPreviewJsonExporter.sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface.position(), StepPreviewJsonExporter.sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = StepPreviewJsonExporter.sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = StepPreviewJsonExporter.sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = conicalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                StepPreviewJsonExporter.toPointPayload(conicalSurfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                        angles.getFirst(),
                        angles.getLast() - angles.getFirst(),
                        null,
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        SphericalSurface surface = builder.buildSphericalSurface(stepSurface.id());
        OrientedEdge lowerArc = outerLoop.edges().get(0);
        OrientedEdge upperArc = outerLoop.edges().get(2);

        List<CartesianPoint> lowerPoints = StepPreviewJsonExporter.sampleOrientedEdge(lowerArc);
        List<Double> lowerU = unwrapAngles(surface.position(), lowerPoints);
        double lowerV = averageSphericalV(surface.position(), surface.radius(), lowerPoints);
        double upperV = averageSphericalV(surface.position(), surface.radius(), StepPreviewJsonExporter.sampleOrientedEdge(upperArc));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || lowerU.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateSphericalStrip(surface, lowerV, upperV, lowerU, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = sphericalNormal(surface.position(), lowerU.getFirst(), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "SPHERICAL_SURFACE",
                StepPreviewJsonExporter.toPointPayload(sphericalSurfacePoint(surface.position(), surface.radius(), lowerU.getFirst(), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                        lowerU.getFirst(),
                        lowerU.getLast() - lowerU.getFirst(),
                        null,
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = StepPreviewJsonExporter.sampleOrientedEdge(edge);
            List<Double> uValues = unwrapToroidalU(surface, points);
            List<Double> vValues = unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.getLast() - uValues.getFirst());
            double vRange = Math.abs(vValues.getLast() - vValues.getFirst());
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.getFirst();
        OrientedEdge upperVEdge = varyingUEdges.getLast();
        if (averageToroidalV(surface, StepPreviewJsonExporter.sampleOrientedEdge(lowerVEdge)) > averageToroidalV(surface, StepPreviewJsonExporter.sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.getLast();
            upperVEdge = varyingUEdges.getFirst();
        }

        List<CartesianPoint> lowerPoints = StepPreviewJsonExporter.sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = unwrapToroidalU(surface, lowerPoints);
        double lowerV = averageToroidalV(surface, lowerPoints);
        double upperV = averageToroidalV(surface, StepPreviewJsonExporter.sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = toroidalNormal(surface, uValues.getFirst(), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                StepPreviewJsonExporter.toPointPayload(toroidalSurfacePoint(surface, uValues.getFirst(), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                        uValues.getFirst(),
                        uValues.getLast() - uValues.getFirst(),
                        null,
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "RATIONAL_B_SPLINE_SURFACE",
                StepPreviewJsonExporter.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                        null, null, null, null, null, null, null
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                StepPreviewJsonExporter.surfaceTypeName(geometry),
                StepPreviewJsonExporter.toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                        null, null, null, null, null, null, null
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                        null, null, null, null, null, null, null
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                        null, null, null, null, null, null, null
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
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                "FREE_FORM_SURFACE",
                StepPreviewJsonExporter.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
        if (basis instanceof StepCylindricalSurface cyl) {
            return toCylindricalFacePayload(stepFace, cyl, builder, metadata);
        }
        if (basis instanceof StepConicalSurface cone) {
            return toConicalFacePayload(stepFace, cone, builder, metadata);
        }
        if (basis instanceof StepSphericalSurface sphere) {
            return toSphericalFacePayload(stepFace, sphere, builder, metadata);
        }
        if (basis instanceof StepToroidalSurface torus) {
            return toToroidalFacePayload(stepFace, torus, builder, metadata);
        }
        if (basis instanceof StepPlane) {
            return toFourSidedPatchFacePayload(stepFace, basis, metadata, builder);
        }
        return null;
    }

    // ─── Geometry collection orchestration ───────────────────────────────

    public static GeometryCollection buildLegacyGeometry(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        Set<Integer> solidIds = new TreeSet<>();
        Map<Integer, EdgePayload> standaloneEdges = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            collectShellLikeIds(entity, shellIds);
            if (entity instanceof StepSweptAreaSolid
                    || entity instanceof StepSolidReplica
                    || entity instanceof StepCsgSolid
                    || entity instanceof StepCsgPrimitive
                    || entity instanceof StepBooleanClippingResult
                    || entity instanceof StepBooleanResult
                    || entity instanceof StepSweptDiskSolid
                    || entity instanceof StepExtrudedAreaSolidTapered
                    || entity instanceof StepRevolvedAreaSolidTapered
                    || entity instanceof StepSurfaceCurveSweptAreaSolid
                    || entity instanceof StepPolygonalBoundedHalfSpace
                    || entity instanceof StepComplexClippingResult
                    || entity instanceof StepHalfSpaceSolid
                    || entity instanceof StepCsgVolume
                    || entity instanceof StepBlockVolume
                    || entity instanceof StepFiniteElementMesh
                    || entity instanceof StepFlatPattern
                    || entity instanceof StepBrepWithVoids
                    || entity instanceof StepManifoldSolidBrep
                    || entity instanceof StepFacettedBrep
                    || entity instanceof StepNonManifoldSolidBrep
                    || entity instanceof StepAdvancedBrep
                    || entity instanceof StepMappedItem
                    || entity instanceof StepSolidModel
                    || entity instanceof StepSurfacePatch
                    || entity instanceof StepExtrudedFaceSolid
                    || entity instanceof StepRevolvedFaceSolid
                    || entity instanceof StepSweptFaceSolid
                    || entity instanceof StepCylinderVolume
                    || entity instanceof StepSphereVolume
                    || entity instanceof StepTorusVolume
                    || entity instanceof StepPrismVolume
                    || entity instanceof StepRightCircularConeVolume
                    || entity instanceof StepTessellatedFace
                    || entity instanceof StepTessellatedFaceSet
                    || entity instanceof StepTriangulatedFace
                    || entity instanceof StepComplexTriangulatedFace
                    || entity instanceof StepCubicBezierTriangulatedFace) {
                solidIds.add(entity.id());
            }
            if (isStandaloneEdgeSource(entity)) {
                collectStandaloneEdges(entity, standaloneEdges, resolved, builder, metadata);
            }
        }
        // Remove shells that are referenced by B-rep solids to avoid duplicate processing
        for (Integer solidId : solidIds) {
            StepEntity solidEntity = resolved.get(solidId);
            if (solidEntity instanceof StepManifoldSolidBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepFacettedBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepNonManifoldSolidBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepAdvancedBrep brep) {
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            } else if (solidEntity instanceof StepBrepWithVoids brep) {
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            }
        }
        GeometryCollection shellGeometry = buildGeometryForShells(shellIds, resolved, builder, metadata, Map.of());
        GeometryCollection solidGeometry = buildGeometryForSolids(solidIds, resolved, builder, metadata, Map.of());
        GeometryCollection edgeGeometry = new GeometryCollection(List.copyOf(standaloneEdges.values()), List.of(), List.of());
        return mergeGeometry(mergeGeometry(shellGeometry, solidGeometry), edgeGeometry);
    }

    public static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PreviewFaceBuilder.class);
        log.debug("stage={} shellCount={}", "geometry_shells_start", shellIds.size());
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Integer> uniqueEdgeIds = new LinkedHashSet<>();
        int processedFaces = 0;

        for (Integer shellId : shellIds) {
            StepEntity shellEntity = resolved.get(shellId);
            if (shellEntity instanceof StepTessellatedFaceSet tessellated) {
                List<FacePayload> tessFaces = StepPreviewJsonExporter.buildTessellatedFacePayloads(tessellated, metadata.forItem(shellId));
                faces.addAll(tessFaces);
                log.debug("stage={} shellId={}, tessellatedFaceCount={}", "geometry_tessellated_shell", shellId, tessFaces.size());
                continue;
            }
            if (shellEntity instanceof StepTessellatedFace tessellatedFace) {
                FacePayload payload = StepPreviewJsonExporter.buildTessellatedFacePayload(tessellatedFace, metadata.forItem(shellId));
                if (payload != null) {
                    faces.add(payload);
                }
                log.debug("stage={} shellId={}, tessellatedFaceBuilt={}", "geometry_tessellated_face", shellId, payload != null);
                continue;
            }
            List<StepFaceEntity> shellFaces = shellFaces(shellEntity);
            log.debug("stage={} shellId={}, shellFaceCount={}", "geometry_shell_start", shellId, shellFaces.size());
            for (StepFaceEntity stepFace : shellFaces) {
                PreviewFaceResult previewFace = StepPreviewJsonExporter.buildPreviewFaceResult(
                        stepFace,
                        builder,
                        StepPreviewJsonExporter.mergeMetadata(inheritedShellMetadata.get(shellId), metadata.forItem(stepFace.id()))
                );
                processedFaces++;
                if (previewFace.face() == null) {
                    unsupportedFaces.add(previewFace.unsupportedFace());
                    if (unsupportedFaces.size() <= 10 || unsupportedFaces.size() % 25 == 0) {
                        log.debug("stage={} faceId={}, processedFaces={}, unsupportedFaces={}, reason={}", "geometry_face_unsupported",
                                stepFace.id(), processedFaces, unsupportedFaces.size(), (previewFace.unsupportedFace() == null ? "null" : previewFace.unsupportedFace().reason()));
                    }
                    continue;
                }
                faces.add(previewFace.face());
                if (processedFaces % 25 == 0) {
                    log.debug("stage={} processedFaces={}, supportedFaces={}, unsupportedFaces={}, uniqueEdges={}", "geometry_face_progress",
                            processedFaces, faces.size(), unsupportedFaces.size(), uniqueEdgeIds.size());
                }
                for (com.minicad.step.model.topology.StepFaceBound bound : stepFace.bounds()) {
                    if (bound.loop() instanceof com.minicad.step.model.topology.StepEdgeLoop edgeLoop) {
                        for (StepOrientedEdge edge : edgeLoop.edges()) {
                            uniqueEdgeIds.add(edge.edgeElement().id());
                        }
                    }
                }
            }
        }

        List<EdgePayload> edges = new ArrayList<>();
        int processedEdges = 0;
        for (Integer edgeId : uniqueEdgeIds) {
            edges.add(StepPreviewJsonExporter.buildEdgePayload(edgeId, resolved, builder, metadata));
            processedEdges++;
            if (processedEdges % 100 == 0) {
                log.debug("stage={} processedEdges={}, totalUniqueEdges={}", "geometry_edge_progress",
                        processedEdges, uniqueEdgeIds.size());
            }
        }
        log.debug("stage={} shellCount={}, processedFaces={}, supportedFaces={}, unsupportedFaces={}, edges={}", "geometry_shells_done",
                shellIds.size(), processedFaces, faces.size(), unsupportedFaces.size(), edges.size());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static GeometryCollection buildGeometryForSolids(
            Set<Integer> solidIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedSolidMetadata
    ) {
        List<EdgePayload> edges = new ArrayList<>();
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Edge> uniqueEdges = new LinkedHashSet<>();

        for (Integer solidId : solidIds) {
            StepEntity entity = resolved.get(solidId);
            StepMetadataExtractor.DisplayMetadata itemMetadata = StepPreviewJsonExporter.mergeMetadata(
                    inheritedSolidMetadata.get(solidId),
                    metadata.forItem(solidId)
            );
            try {
                Solid solid = builder.buildSolid(solidId);
                String baseName = entity == null ? null : entity.name();
                int faceIndex = 0;
                for (Face face : solid.outerShell().faces()) {
                    faces.add(StepPreviewJsonExporter.facePayloadFromTopologyFace(
                            solidId * 1000 + faceIndex++,
                            face,
                            baseName,
                            itemMetadata
                    ));
                    collectTopologyEdges(face, uniqueEdges);
                }
                for (var voidShell : solid.voidShells()) {
                    for (Face face : voidShell.faces()) {
                        faces.add(StepPreviewJsonExporter.facePayloadFromTopologyFace(
                                solidId * 1000 + faceIndex++,
                                face,
                                baseName,
                                itemMetadata
                        ));
                        collectTopologyEdges(face, uniqueEdges);
                    }
                }
            } catch (UnsupportedGeometryException | StepResolutionException | TopologyException ex) {
                unsupportedFaces.add(new UnsupportedFacePayload(
                        solidId,
                        entity == null ? null : entity.name(),
                        entity == null ? "SOLID" : StepPreviewJsonExporter.surfaceTypeName(entity),
                        ex.getMessage()
                ));
            }
        }

        int edgeIndex = 0;
        for (Edge edge : uniqueEdges) {
            edges.add(buildTopologyEdgePayload(-(edgeIndex + 1), edge));
            edgeIndex++;
        }
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static GeometryCollection mergeGeometry(GeometryCollection left, GeometryCollection right) {
        List<EdgePayload> edges = new ArrayList<>(left.edges());
        edges.addAll(right.edges());
        List<FacePayload> faces = new ArrayList<>(left.faces());
        faces.addAll(right.faces());
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>(left.unsupportedFaces());
        unsupportedFaces.addAll(right.unsupportedFaces());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    public static void collectShellLikeIds(StepEntity item, Set<Integer> shellIds) {
        if (item instanceof StepStyledItem styledItem) {
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (item instanceof StepOverRidingStyledItem styledItem) {
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (isShellLikeEntity(item)) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSolidBrep
                || item instanceof StepFacettedBrep
                || item instanceof StepNonManifoldSolidBrep
                || item instanceof StepAdvancedBrep
                || item instanceof StepBrepWithVoids
                || item instanceof StepMappedItem
                || item instanceof StepSolidModel
                || item instanceof StepSurfacePatch) {
            return;
        }
        if (item instanceof StepShellBasedSurfaceModel surfaceModel) {
            for (StepEntity shell : surfaceModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepTessellatedFaceSet) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepTessellatedFace) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSurfaceModel manifoldModel) {
            for (StepEntity shell : manifoldModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepFaceBasedSurfaceModel faceModel) {
            for (StepEntity faceSet : faceModel.faceSets()) {
                collectShellLikeIds(faceSet, shellIds);
            }
        }
    }

    public static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        if (item instanceof StepStyledItem styledItem) {
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepOverRidingStyledItem styledItem) {
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepPolyline polyline) {
            edges.putIfAbsent(polyline.id(), toPolylineEdgePayload(polyline));
            return;
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepShellBasedWireframeModel wireframeModel) {
            for (StepEntity boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeBasedWireframeModel wireframeModel) {
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepConnectedEdgeSet connectedEdgeSet) {
            for (StepEntity edge : connectedEdgeSet.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeCurve edgeCurve) {
            edges.putIfAbsent(edgeCurve.id(), StepPreviewJsonExporter.buildEdgePayload(edgeCurve.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepFilletEdge filletEdge) {
            edges.putIfAbsent(filletEdge.id(), StepPreviewJsonExporter.buildEdgePayload(filletEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepChamferEdge chamferEdge) {
            edges.putIfAbsent(chamferEdge.id(), StepPreviewJsonExporter.buildEdgePayload(chamferEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepPath path) {
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOpenPath path) {
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepSubpath subpath) {
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOrientedPath orientedPath) {
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepWireShell wireShell) {
            for (StepEntity loop : wireShell.loops()) {
                collectStandaloneEdges(loop, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeWire edgeWire) {
            for (StepEntity edge : edgeWire.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSurfaceSet surfaceSet) {
            for (StepEntity element : surfaceSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeLoop edgeLoop) {
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), StepPreviewJsonExporter.buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepPolyLoop polyLoop) {
            edges.putIfAbsent(polyLoop.id(), toPolyLoopEdgePayload(polyLoop));
            return;
        }
        if (item instanceof StepVertexShell || item instanceof StepVertexLoop) {
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationFillArea fillArea) {
            for (StepEntity boundary : fillArea.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            collectMappedAnnotationEdges(
                    annotationSymbol.id(),
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            if (!collectMappedAnnotationCarrierEdges(
                    symbolOccurrence.id(),
                    "ANNOTATION_SYMBOL_OCCURRENCE",
                    symbolOccurrence.id(),
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(symbolOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            if (!collectMappedAnnotationCarrierEdges(
                    subfigureOccurrence.id(),
                    "ANNOTATION_SUBFIGURE_OCCURRENCE",
                    subfigureOccurrence.id(),
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(subfigureOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationText annotationText) {
            collectMappedAnnotationEdges(
                    annotationText.id(),
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            collectMappedAnnotationEdges(
                    annotationTextCharacter.id(),
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepDimensionCurve dimensionCurve) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepLeaderCurve leaderCurve) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepProjectionCurve projectionCurve) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else if (collectMappedAnnotationCarrierEdges(
                    annotationOccurrence.id(),
                    "DRAUGHTING_ANNOTATION_OCCURRENCE",
                    annotationOccurrence.id(),
                    annotationOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                return;
            } else {
                collectStandaloneEdges(annotationOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepTerminatorSymbol terminatorSymbol) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepFilletEdge filletEdge) {
            collectStandaloneEdges(filletEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepChamferEdge chamferEdge) {
            collectStandaloneEdges(chamferEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepSubedge subedge) {
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (isSampledCurveSource(item)) {
            EdgePayload sampled = StepPreviewJsonExporter.sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            }
        }
    }

    // ─── Representation geometry ─────────────────────────────────────────

    public static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                if (item instanceof StepMappedItem mappedItem) {
                    geometry = mergeGeometry(
                            geometry,
                            expandMappedItemGeometry(mappedItem, resolved, builder, metadata, visitingRepresentations)
                    );
                }
            }
        }
        return geometry;
    }

    public static GeometryCollection buildRelatedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepRepresentationRelationshipWithTransformation relationship)) {
                continue;
            }
            if (!relationship.rep1().shapeRepresentation()
                    || !relationship.rep2().shapeRepresentation()
                    || relationship.rep2().id() != representation.id()) {
                continue;
            }
            double[] matrix = StepAssemblyGraphBuilder.matrixFor(relationship.transformationOperator());
            RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                    relationship.rep1(),
                    relationship.rep1().name(),
                    resolved,
                    builder,
                    metadata,
                    visitingRepresentations
            );
            StepMetadataExtractor.DisplayMetadata relationshipMetadata = metadata.forItem(relationship.id());
            List<EdgePayload> edges = source.payload().edges().stream()
                    .map(edge -> StepPreviewJsonExporter.transformMappedEdge(edge, relationship.id(), matrix))
                    .toList();
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> StepPreviewJsonExporter.transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .toList();
            geometry = mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
        }
        return geometry;
    }

    public static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        double[] matrix = StepPreviewJsonExporter.mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                mappingSource.mappedRepresentation(),
                mappingSource.mappedRepresentation().name(),
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(mappedItem.id());
        List<EdgePayload> edges = source.payload().edges().stream()
                .map(edge -> StepPreviewJsonExporter.transformMappedEdge(edge, mappedItem.id(), matrix))
                .toList();
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> StepPreviewJsonExporter.transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .toList();
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }

    public static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (!isRepresentationSolidItem(unwrapped)) {
                    collectShellLikeIds(item, shellIds);
                }
            }
        }
        return shellIds;
    }

    public static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> solidIds = new TreeSet<>();
        for (StepRepresentation candidate : StepPreviewJsonExporter.linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
    }

    // ─── Edge/loop building ──────────────────────────────────────────────

    public static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop vertexLoop) {
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop polyLoop) {
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
                sampled.add(sampled.getFirst());
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop edgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = StepPreviewJsonExporter.sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
            sampled.add(sampled.getFirst());
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    public static void collectTopologyEdges(Face face, Set<Edge> edges) {
        for (FaceBound bound : face.bounds()) {
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
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
        if (reversed.getFirst().equals(reversed.getLast())) {
            T start = reversed.removeLast();
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.getFirst());
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
                null
        );
    }

    public static EdgePayload toPolylineEdgePayload(StepPolyline polyline) {
        List<CartesianPoint> points = polyline.points().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .toList();
        return new EdgePayload(polyline.id(), toPointPayloads(points), null);
    }

    public static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .toList();
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.getFirst().distanceTo(closed.getLast()) > 1.0e-9) {
            closed.add(closed.getFirst());
        }
        return new EdgePayload(polyLoop.id(), toPointPayloads(List.copyOf(closed)), null);
    }

    // ─── Shell/vertex utilities ──────────────────────────────────────────

    public static List<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
            return orientedClosedShell.faces();
        }
        if (entity instanceof StepConnectedFaceSet connectedFaceSet) {
            return connectedFaceSet.faces();
        }
        if (entity instanceof StepConnectedFaceSubSet connectedFaceSubSet) {
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
        if (vertex instanceof StepCartesianPoint cp) {
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
        return item instanceof StepLine
                || item instanceof StepCircle
                || item instanceof StepEllipse
                || item instanceof StepConicCurve
                || item instanceof StepBezierCurve
                || item instanceof StepUniformCurve
                || item instanceof StepQuasiUniformCurve
                || item instanceof StepPiecewiseBezierCurve
                || item instanceof StepBSplineCurveWithKnots
                || item instanceof StepBSplineCurve
                || item instanceof com.minicad.step.model.geometry.StepRationalBSplineCurve
                || item instanceof StepSurfaceCurve
                || item instanceof StepSeamCurve
                || item instanceof StepTrimmedCurve
                || item instanceof StepPolyline
                || item instanceof com.minicad.step.model.geometry.StepCompositeCurve
                || item instanceof com.minicad.step.model.geometry.StepCompositeCurveOnSurface
                || item instanceof StepCompositeCurveOnSurface3D
                || item instanceof StepOffsetCurve2D
                || item instanceof StepOffsetCurve3D
                || item instanceof StepPcurve
                || item instanceof StepDegeneratePcurve
                || item instanceof StepOrientedCurve
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepClothoid
                || item instanceof StepIndexedPolyCurve
                || item instanceof StepDegenerateCurve
                || item instanceof StepBSplineCurveWithKnotsAndBreakpoints
                || item instanceof StepLineSegment
                || item instanceof StepEdgeCurve
                || item instanceof StepSurfacedEdgeCurve
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepCurve
                || item instanceof StepBoundedCurve
                || item instanceof StepCircle2D
                || item instanceof StepEllipse2D
                || item instanceof StepPolyline2D
                || item instanceof StepTrimmedCurve2D
                || item instanceof StepCompositeCurve2D
                || item instanceof StepBezierCurve2D
                || item instanceof StepQuasiUniformCurve2D
                || item instanceof StepUniformCurve2D
                || item instanceof StepPiecewiseBezierCurve2D
                || item instanceof StepIndexedPolyCurve2D
                || item instanceof StepDegenerateCurve2D
                || item instanceof StepBSplineCurve2D
                || item instanceof StepRationalBSplineCurve2D
                || item instanceof StepLine2D
                || item instanceof StepCurve2D
                || item instanceof StepHyperbola2D
                || item instanceof StepParabola2D
                || (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName()));
    }

    public static boolean isStandaloneEdgeSource(StepEntity item) {
        return item instanceof StepPolyline
                || item instanceof StepGeometricCurveSet
                || item instanceof StepGeometricSet
                || item instanceof StepShellBasedWireframeModel
                || item instanceof StepEdgeBasedWireframeModel
                || item instanceof StepConnectedEdgeSet
                || item instanceof StepEdgeWire
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepWireShell
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepAnnotationFillArea
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepFilletEdge
                || item instanceof StepChamferEdge
                || item instanceof StepSubedge
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepGeometricSurfaceSet;
    }

    public static StepEntity unwrapStyledItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepStyledItem styledItem) {
                current = styledItem.item();
                continue;
            }
            if (current instanceof StepOverRidingStyledItem styledItem) {
                current = styledItem.item();
                continue;
            }
            return current;
        }
    }

    public static boolean isRepresentationSolidItem(StepEntity entity) {
        return entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepSweptDiskSolid
                || entity instanceof StepExtrudedAreaSolidTapered
                || entity instanceof StepRevolvedAreaSolidTapered
                || entity instanceof StepSurfaceCurveSweptAreaSolid
                || entity instanceof StepPolygonalBoundedHalfSpace
                || entity instanceof StepComplexClippingResult
                || entity instanceof StepHalfSpaceSolid
                || entity instanceof StepCsgVolume
                || entity instanceof StepBlockVolume
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepMappedItem
                || entity instanceof StepSolidModel
                || entity instanceof StepSurfacePatch;
    }

    public static ColorPayload toColorPayload(int[] rgb) {
        if (rgb == null) {
            return null;
        }
        return new ColorPayload(rgb[0], rgb[1], rgb[2]);
    }

    public static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        return new PbrPayload(metadata.diffuse(), metadata.specular(), metadata.specularExponent(), metadata.specularColor());
    }

    public static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        return switch (surface) {
            case Plane ignored -> "PLANE";
            case CylindricalSurface ignored -> "CYLINDRICAL_SURFACE";
            case ConicalSurface ignored -> "CONICAL_SURFACE";
            case SphericalSurface ignored -> "SPHERICAL_SURFACE";
            case ToroidalSurface ignored -> "TOROIDAL_SURFACE";
            case BSplineSurface3 ignored -> "BSPLINE_SURFACE";
            case RationalBSplineSurface3 ignored -> "RATIONAL_BSPLINE_SURFACE";
            case RuledSurface3 ignored -> "RULED_SURFACE";
            case SurfaceOfRevolution3 ignored -> "SURFACE_OF_REVOLUTION";
            case OffsetSurface3 ignored -> "OFFSET_SURFACE";
            case SurfaceOfLinearExtrusion3 ignored -> "SURFACE_OF_LINEAR_EXTRUSION";
            case SurfaceOfConstantRadius3 ignored -> "SURFACE_OF_CONSTANT_RADIUS";
            case ParaboloidSurface ignored -> "PARABOLOID_SURFACE";
            case HyperboloidSurface ignored -> "HYPERBOLOID_SURFACE";
            case SurfaceOfTranslation3 ignored -> "SURFACE_OF_TRANSLATION";
            case SurfaceOfProjection3 ignored -> "SURFACE_OF_PROJECTION";
        };
    }

    // ─── Local helper methods (copied from StepPreviewJsonExporter) ──────
    // These are private in StepPreviewJsonExporter and cannot be accessed.
    // Duplicated here to keep StepPreviewJsonExporter unchanged.

    private static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PreviewFaceBuilder::toPointPayload).toList();
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
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize();
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
            CartesianPoint lower0 = surfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = surfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = surfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = surfacePoint(surface, angle1, upperHeight);
            Vector3 targetNormal = cylindricalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static CartesianPoint surfacePoint(CylindricalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * surface.radius())
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * surface.radius()));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 cylindricalNormal(CylindricalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        return sameSense ? radial : radial.scale(-1.0);
    }

    // ─── Conical strip helpers ───────────────────────────────────────────

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
            CartesianPoint lower0 = conicalSurfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = conicalSurfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = conicalSurfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = conicalSurfacePoint(surface, angle1, upperHeight);
            Vector3 targetNormal = conicalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static CartesianPoint conicalSurfacePoint(ConicalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius() + height * Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 conicalNormal(ConicalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        double slope = Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        Vector3 normal = radial.subtract(placement.axis().asVector().scale(slope));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    // ─── Spherical strip helpers ─────────────────────────────────────────

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
            CartesianPoint p00 = sphericalSurfacePoint(placement, radius, angle0, lowerV);
            CartesianPoint p10 = sphericalSurfacePoint(placement, radius, angle1, lowerV);
            CartesianPoint p01 = sphericalSurfacePoint(placement, radius, angle0, upperV);
            CartesianPoint p11 = sphericalSurfacePoint(placement, radius, angle1, upperV);
            Vector3 targetNormal = sphericalNormal(placement, (angle0 + angle1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static double sphericalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static double sphericalV(Axis2Placement3D placement, double radius, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double z = offset.dot(placement.axis().asVector());
        double normalized = radius <= 1.0e-12 ? 0.0 : z / radius;
        normalized = Math.max(-1.0, Math.min(1.0, normalized));
        return Math.asin(normalized);
    }

    private static double averageSphericalV(Axis2Placement3D placement, double radius, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += sphericalV(placement, radius, point);
        }
        return total / points.size();
    }

    private static CartesianPoint sphericalSurfacePoint(Axis2Placement3D placement, double radius, double u, double v) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return placement.location().add(normal.scale(radius));
    }

    private static Vector3 sphericalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
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
            CartesianPoint p00 = toroidalSurfacePoint(surface, u0, lowerV);
            CartesianPoint p10 = toroidalSurfacePoint(surface, u1, lowerV);
            CartesianPoint p01 = toroidalSurfacePoint(surface, u0, upperV);
            CartesianPoint p11 = toroidalSurfacePoint(surface, u1, upperV);
            Vector3 targetNormal = toroidalNormal(surface, (u0 + u1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
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
                double previous = values.getLast();
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
                double previous = values.getLast();
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

    private static List<Double> unwrapAngles(CylindricalSurface surface, List<CartesianPoint> points) {
        return unwrapAngles(surface.position(), points);
    }

    private static List<Double> unwrapAngles(Axis2Placement3D placement, List<CartesianPoint> points) {
        List<Double> angles = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double angle = cylindricalAngle(placement, point);
            if (!angles.isEmpty()) {
                double previous = angles.getLast();
                while (angle - previous > Math.PI) {
                    angle -= Math.PI * 2.0;
                }
                while (angle - previous < -Math.PI) {
                    angle += Math.PI * 2.0;
                }
            }
            angles.add(angle);
        }
        return List.copyOf(angles);
    }

    private static double averageAxialHeight(CylindricalSurface surface, List<CartesianPoint> points) {
        return averageAxialHeight(surface.position(), points);
    }

    private static double averageAxialHeight(Axis2Placement3D placement, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += axialHeight(placement, point);
        }
        return total / points.size();
    }

    private static double axialHeight(Axis2Placement3D placement, CartesianPoint point) {
        return point.subtract(placement.location()).dot(placement.axis().asVector());
    }

    private static double cylindricalAngle(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

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
            triangles.add(StepPreviewJsonExporter.toPointPayload(a));
            triangles.add(StepPreviewJsonExporter.toPointPayload(c));
            triangles.add(StepPreviewJsonExporter.toPointPayload(b));
            return;
        }
        triangles.add(StepPreviewJsonExporter.toPointPayload(a));
        triangles.add(StepPreviewJsonExporter.toPointPayload(b));
        triangles.add(StepPreviewJsonExporter.toPointPayload(c));
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

    // ─── Mapped annotation edge collection (delegates to PreviewCurveEvaluator) ──

    private static void collectMappedAnnotationEdges(
            int mappedOwnerId,
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            String sourceType,
            Integer sourceStepId,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        double[] matrix = StepPreviewJsonExporter.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        RepresentationBuildResult source = StepPreviewJsonExporter.buildRepresentationPayload(
                representation,
                representation.name(),
                resolved,
                builder,
                StepMetadataExtractor.fromResolved(resolved),
                new LinkedHashSet<>()
        );
        for (EdgePayload edge : source.payload().edges()) {
            EdgePayload transformed = StepPreviewJsonExporter.transformMappedEdge(edge, mappedOwnerId, matrix, sourceType, sourceStepId);
            edges.putIfAbsent(transformed.stepId(), transformed);
        }
    }

    private static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationText annotationText) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        if (item instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        return false;
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
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
                StepPreviewJsonExporter.faceDisplayName(stepFace),
                surfaceType,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                null,
                null
        );
    }
}
