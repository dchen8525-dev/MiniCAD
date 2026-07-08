package com.minicad.export.glb;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.geometry.*;
import com.minicad.helper.geometry.MathUtilityHelper;
import com.minicad.helper.metadata.StepMetadataExtractor;
import com.minicad.preview.builder.PreviewFaceBuilder;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayloadCopies;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.preview.sampling.TriangulationHelper;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.core.base.StepFaceEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mesh export utilities for preview generation.
 * Handles vertex data, face indices, triangle generation, and mesh serialization.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
public final class PreviewMeshExporter {

    private static final int TOPOLOGY_SURFACE_GRID_SEGMENTS = 16;

    private PreviewMeshExporter() {}

    // ─── Face Payload from Topology ────────────────────────────────────────────────

    /**
     * Creates a FacePayload from a topology Face object.
     */
    public static FacePayload facePayloadFromTopologyFace(
            int stepId,
            Face face,
            String name,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        SurfaceGeometry surface = face.surface();
        boolean sameSense = face.sameSense();

        if (surface instanceof Plane) {
            return buildPlaneFacePayload(stepId, name, metadata, surface, sameSense, face);
        }
        if (surface instanceof ParaboloidSurface) {
            return buildParaboloidFacePayload(stepId, name, metadata, surface, sameSense, face);
        }
        if (surface instanceof HyperboloidSurface) {
            return buildHyperboloidFacePayload(stepId, name, metadata, surface, sameSense, face);
        }
        if (surface instanceof SurfaceOfTranslation3) {
            return buildTranslationFacePayload(stepId, name, metadata, surface, sameSense, face);
        }
        if (surface instanceof SurfaceOfProjection3) {
            return buildProjectionFacePayload(stepId, name, metadata, surface, sameSense, face);
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cyl = (CylindricalSurface) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "CYLINDRICAL_SURFACE", "cylindrical_surface",
                    cyl.position(), null, null, cyl.radius(), 0.0, 0.0, face.bounds());
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface cone = (ConicalSurface) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "CONICAL_SURFACE", "conical_surface",
                    cone.position(), null, null, cone.radius(), cone.semiAngle(), 0.0, face.bounds());
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphere = (SphericalSurface) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "SPHERICAL_SURFACE", "spherical_surface",
                    sphere.position(), null, null, sphere.radius(), 0.0, 0.0, face.bounds());
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface torus = (ToroidalSurface) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "TOROIDAL_SURFACE", "toroidal_surface",
                    torus.position(), null, null, torus.majorRadius(), torus.minorRadius(), 0.0, face.bounds());
        }
        if (surface instanceof BSplineSurface3) {
            BSplineSurface3 bspline = (BSplineSurface3) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "BSPLINE_SURFACE", "bspline_surface",
                    null, bspline.uDegree(), bspline.vDegree(), 0.0, 0.0, 0.0, face.bounds());
        }
        if (surface instanceof RationalBSplineSurface3) {
            RationalBSplineSurface3 rational = (RationalBSplineSurface3) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "RATIONAL_BSPLINE_SURFACE", "rational_bspline_surface",
                    null, rational.uDegree(), rational.vDegree(), 0.0, 0.0, 0.0, face.bounds());
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "SURFACE_OF_LINEAR_EXTRUSION", "linear_extrusion",
                    null, null, null, 0.0, 0.0, 0.0, face.bounds());
        }
        if (surface instanceof SurfaceOfRevolution3) {
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "SURFACE_OF_REVOLUTION", "surface_of_revolution",
                    null, null, null, 0.0, 0.0, 0.0, face.bounds());
        }
        if (surface instanceof RuledSurface3) {
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "RULED_SURFACE", "ruled_surface",
                    null, null, null, 0.0, 0.0, 0.0, face.bounds());
        }
        if (surface instanceof SurfaceOfConstantRadius3) {
            SurfaceOfConstantRadius3 constRadius = (SurfaceOfConstantRadius3) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "SURFACE_OF_CONSTANT_RADIUS", "constant_radius_surface",
                    null, null, null, constRadius.radius(), 0.0, 0.0, face.bounds());
        }
        if (surface instanceof OffsetSurface3) {
            OffsetSurface3 offset = (OffsetSurface3) surface;
            return newFacePayloadFromGrid(surface, stepId, name, sameSense, metadata,
                    "OFFSET_SURFACE", "offset_surface",
                    null, null, null, 0.0, offset.distance(), 0.0, face.bounds());
        }

        // Generic grid-based triangulation for other surfaces
        return buildGenericGridFacePayload(stepId, name, metadata, surface, sameSense, face);
    }

    // ─── Surface-Specific Payload Builders ──────────────────────────────────────────

    private static FacePayload buildPlaneFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        Plane plane = (Plane) surface;
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(
                    PreviewFaceBuilder.sampleLoop(bound))));
        }
        Direction3 normal = plane.normal();
        if (!sameSense) {
            normal = normal.reverse();
        }
        return new FacePayload(
                stepId, name, "PLANE",
                PayloadConversionHelper.toPointPayload(plane.origin()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, List.of(),
                new FaceSurfacePayload(
                        "plane_face",
                        List.of(plane.origin().x(), plane.origin().y(), plane.origin().z()),
                        List.of(plane.normal().x(), plane.normal().y(), plane.normal().z()),
                        basisDirectionForNormal(normal),
                        0.0, null, null, 0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload buildParaboloidFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        ParaboloidSurface paraboloid = (ParaboloidSurface) surface;
        Axis2Placement3D pos = paraboloid.position();
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = buildLoopPayloads(face);
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        return new FacePayload(
                stepId, name, "PARABOLOID_SURFACE",
                new PointPayload(pos.location().x(), pos.location().y(), pos.location().z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                new FaceSurfacePayload(
                        "paraboloid_surface",
                        List.of(pos.location().x(), pos.location().y(), pos.location().z()),
                        List.of(pos.axis().x(), pos.axis().y(), pos.axis().z()),
                        List.of(pos.xDirection().x(), pos.xDirection().y(), pos.xDirection().z()),
                        paraboloid.focalLength(), null, null, 0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload buildHyperboloidFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        HyperboloidSurface hyperboloid = (HyperboloidSurface) surface;
        Axis2Placement3D pos = hyperboloid.position();
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = buildLoopPayloads(face);
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        return new FacePayload(
                stepId, name, "HYPERBOLOID_SURFACE",
                new PointPayload(pos.location().x(), pos.location().y(), pos.location().z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                new FaceSurfacePayload(
                        "hyperboloid_surface",
                        List.of(pos.location().x(), pos.location().y(), pos.location().z()),
                        List.of(pos.axis().x(), pos.axis().y(), pos.axis().z()),
                        List.of(pos.xDirection().x(), pos.xDirection().y(), pos.xDirection().z()),
                        hyperboloid.radius(), null, hyperboloid.semiAxis(), 0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload buildTranslationFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        SurfaceOfTranslation3 translation = (SurfaceOfTranslation3) surface;
        Vector3 dir = translation.direction();
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = buildLoopPayloads(face);
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) return null;
        return new FacePayload(
                stepId, name, "SURFACE_OF_TRANSLATION",
                new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                new FaceSurfacePayload(
                        "surface_of_translation",
                        null, List.of(dir.x(), dir.y(), dir.z()), null,
                        0.0, null, null, 0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload buildProjectionFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        SurfaceOfProjection3 projection = (SurfaceOfProjection3) surface;
        Vector3 dir = projection.projectionDirection();
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = buildLoopPayloads(face);
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) return null;
        return new FacePayload(
                stepId, name, "SURFACE_OF_PROJECTION",
                new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                new FaceSurfacePayload(
                        "surface_of_projection",
                        null, List.of(dir.x(), dir.y(), dir.z()), null,
                        0.0, null, null, 0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload buildGenericGridFacePayload(
            int stepId, String name, StepMetadataExtractor.DisplayMetadata metadata,
            SurfaceGeometry surface, boolean sameSense, Face face
    ) {
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        if (grid.isEmpty()) {
            throw new UnsupportedGeometryException(
                    PreviewFaceBuilder.surfaceTypeNameForGeometry(surface) + " produced no sample grid");
        }
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) {
            throw new UnsupportedGeometryException(
                    PreviewFaceBuilder.surfaceTypeNameForGeometry(surface) + " triangulation produced no cells");
        }
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = buildLoopPayloads(face);
        return new FacePayload(
                stepId, name,
                PreviewFaceBuilder.surfaceTypeNameForGeometry(surface),
                new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                null, null
        );
    }

    // ─── Grid-Based Face Payload ─────────────────────────────────────────────────────

    private static FacePayload newFacePayloadFromGrid(
            SurfaceGeometry surface,
            int stepId,
            String name,
            boolean sameSense,
            StepMetadataExtractor.DisplayMetadata metadata,
            String displayName,
            String surfaceType,
            Axis2Placement3D position,
            Integer uDegree,
            Integer vDegree,
            double scalarA,
            double scalarB,
            double scalarC,
            List<FaceBound> bounds
    ) {
        List<List<CartesianPoint>> grid = sampleTopologySurfaceGrid(surface);
        if (grid.isEmpty()) return null;
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) return null;

        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);

        PointPayload anchor = triangles.get(0);
        List<Double> origin = null;
        List<Double> axis = null;
        List<Double> basisDir = null;
        if (position != null) {
            origin = List.of(position.location().x(), position.location().y(), position.location().z());
            axis = List.of(position.axis().x(), position.axis().y(), position.axis().z());
            basisDir = List.of(position.xDirection().x(), position.xDirection().y(), position.xDirection().z());
            anchor = PayloadConversionHelper.toPointPayload(position.location());
        }

        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(
                    PreviewFaceBuilder.sampleLoop(bound))));
        }

        return new FacePayload(
                stepId, displayName, displayName,
                new PointPayload(anchor.x(), anchor.y(), anchor.z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops, triangles,
                new FaceSurfacePayload(
                        surfaceType, origin, axis, basisDir,
                        scalarA, Double.valueOf(scalarB), Double.valueOf(scalarC),
                        0.0, 0.0, 0.0, 0.0,
                        uDegree, vDegree, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    // ─── Parametric Triangulation ────────────────────────────────────────────────────

    public static List<PointPayload> triangulateParametricFace(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean sameSense
    ) {
        ParametricLoopPayload outer = loops.stream()
                .filter(ParametricLoopPayload::outer)
                .findFirst()
                .orElse(null);
        if (outer == null) {
            return List.of();
        }

        List<ParametricLoopPayload> holes = loops.stream()
                .filter(loop -> !loop.outer())
                .collect(Collectors.toList());

        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);

                if (!TriangulationHelper.contains(outer.points(), center)) {
                    continue;
                }

                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (TriangulationHelper.contains(hole.points(), center)) {
                        insideHole = true;
                        break;
                    }
                }
                if (insideHole) {
                    continue;
                }

                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (!sameSense) {
                    normal = normal.scale(-1.0);
                }
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p10, p11, normal);
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p11, p01, normal);
            }
        }
        return List.copyOf(triangles);
    }

    public static List<PointPayload> triangulateParametricFaceAdaptive(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int baseUSegments,
            int baseVSegments,
            boolean sameSense
    ) {
        int uSegments = baseUSegments;
        int vSegments = baseVSegments;
        for (int attempt = 0; attempt < 4; attempt++) {
            List<PointPayload> triangles = triangulateParametricFace(
                    mapper, loops, bounds, uSegments, vSegments, sameSense);
            if (!triangles.isEmpty()) {
                return triangles;
            }
            if (uSegments >= 512 && vSegments >= 256) {
                break;
            }
            uSegments = Math.min(uSegments * 2, 512);
            vSegments = Math.min(vSegments * 2, 256);
        }
        return List.of();
    }

    // ─── Parametric Loop Utilities ───────────────────────────────────────────────────

    public static List<ParametricLoopPayload> normalizeLoopRoles(
            StepFaceEntity stepFace,
            StepEntity geometry,
            List<ParametricLoopPayload> loops
    ) {
        if (loops.isEmpty() || loops.stream().anyMatch(ParametricLoopPayload::outer)) {
            return loops;
        }
        int outerIndex = -1;
        double outerArea = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < loops.size(); index++) {
            double area = Math.abs(TriangulationHelper.signedArea(loops.get(index).points()));
            if (area > outerArea + Epsilon.EPS) {
                outerArea = area;
                outerIndex = index;
            }
        }
        if (outerIndex < 0) {
            return loops;
        }
        List<ParametricLoopPayload> normalized = new ArrayList<>(loops.size());
        for (int index = 0; index < loops.size(); index++) {
            normalized.add(new ParametricLoopPayload(index == outerIndex, loops.get(index).points()));
        }
        return List.copyOf(normalized);
    }

    public static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV)
                || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    public static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
        if (points.size() < 2) {
            return points;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        List<UvPoint> normalized = new ArrayList<>(points.size());
        UvPoint previous = null;
        for (UvPoint point : points) {
            double u = point.u();
            double v = point.v();
            if (previous != null) {
                if (uPeriod != null) {
                    u = MathUtilityHelper.unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = MathUtilityHelper.unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.get(0);
            UvPoint last = normalized.get(normalized.size() - 1);
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = MathUtilityHelper.unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = MathUtilityHelper.unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    public static List<LoopPayload> toParametricLoopPayloads(
            List<ParametricLoopPayload> loops,
            ParametricSurfaceMapper mapper
    ) {
        List<LoopPayload> payloads = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            List<PointPayload> points = new ArrayList<>(loop.points().size());
            for (UvPoint point : loop.points()) {
                points.add(PayloadConversionHelper.toPointPayload(mapper.pointAt(point.u(), point.v())));
            }
            payloads.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(payloads);
    }

    // ─── Grid Sampling ──────────────────────────────────────────────────────────────

    public static List<List<CartesianPoint>> sampleTopologySurfaceGrid(SurfaceGeometry surface) {
        return surface.sampleGrid(TOPOLOGY_SURFACE_GRID_SEGMENTS, TOPOLOGY_SURFACE_GRID_SEGMENTS);
    }

    public static List<List<CartesianPoint>> sampleSurfaceGrid(BSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    public static List<List<CartesianPoint>> sampleSurfaceGrid(RationalBSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    // ─── Face Surface Payload ───────────────────────────────────────────────────────

    public static FaceSurfacePayload faceSurfacePayload(
            StepEntity geometry,
            UvBounds uvBounds,
            StepCadBuilder builder
    ) {
        StepEntity surfaceGeometry = PreviewFaceBuilder.unwrapParametricPreviewSurface(geometry);

        if (surfaceGeometry instanceof StepPlane) {
            StepPlane stepPlane = (StepPlane) surfaceGeometry;
            Plane plane = builder.buildPlane(stepPlane.id());
            Direction3 normal = plane.normal();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "plane_face",
                    List.of(plane.origin().x(), plane.origin().y(), plane.origin().z()),
                    List.of(normal.x(), normal.y(), normal.z()),
                    basisDirectionForNormal(normal),
                    0.0, null, null,
                    uvBounds.minU(), uvBounds.maxU(),
                    uvBounds.minV(), uvBounds.maxV(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) surfaceGeometry;
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "cylindrical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(), null, null,
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) surfaceGeometry;
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "conical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(), null, surface.semiAngle(),
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) surfaceGeometry;
            SphericalSurface surface = builder.buildSphericalSurface(sphericalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "spherical_surface",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(), null, null,
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) surfaceGeometry;
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "toroidal_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.majorRadius(), surface.minorRadius(), null,
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) surfaceGeometry;
            SurfaceOfLinearExtrusion3 surface = builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            Direction3 axis = surface.extrusionVector().normalize().asDirection();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_linear_extrusion",
                    null,
                    List.of(axis.x(), axis.y(), axis.z()),
                    null,
                    0.0, null, null,
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) surfaceGeometry;
            SurfaceOfRevolution3 surface = builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_revolution",
                    List.of(surface.axisOrigin().x(), surface.axisOrigin().y(), surface.axisOrigin().z()),
                    List.of(surface.axisDirection().x(), surface.axisDirection().y(), surface.axisDirection().z()),
                    null,
                    0.0, null, null,
                    uvBounds.minV(), uvBounds.maxV(),
                    uvBounds.minU(), uvBounds.uSpan(),
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) surfaceGeometry;
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "rational_bspline_surface",
                    null, null, null,
                    0.0, null, null,
                    surface.uStart(), surface.uEnd(),
                    surface.vStart(), surface.vEnd(),
                    surface.uDegree(), surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(), surface.vMultiplicities(),
                    surface.uKnots(), surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepBSplineSurfaceWithKnots
                || surfaceGeometry instanceof StepBSplineSurface
                || surfaceGeometry instanceof StepBezierSurface
                || surfaceGeometry instanceof StepUniformSurface
                || surfaceGeometry instanceof StepQuasiUniformSurface
                || surfaceGeometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = buildBsplineSurface(surfaceGeometry, builder);
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "bspline_surface",
                    null, null, null,
                    0.0, null, null,
                    surface.uStart(), surface.uEnd(),
                    surface.vStart(), surface.vEnd(),
                    surface.uDegree(), surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(), surface.vMultiplicities(),
                    surface.uKnots(), surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        return null;
    }

    public static FaceSurfacePayload withSurfaceSourceMetadata(FaceSurfacePayload base, StepEntity geometry) {
        if (base == null || geometry == null) {
            return base;
        }
        String basisType = null;
        Integer basisStepId = null;
        Boolean orientation = null;
        Double offsetDistance = null;
        Double trimU1 = null;
        Double trimU2 = null;
        Double trimV1 = null;
        Double trimV2 = null;
        Boolean implicitOuter = null;
        Double transformScale = null;

        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            basisType = StepPreviewJsonExporter.surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            basisType = StepPreviewJsonExporter.surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            basisType = StepPreviewJsonExporter.surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            basisType = StepPreviewJsonExporter.surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica
                && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            basisType = StepPreviewJsonExporter.surfaceTypeName(replica.parent());
            basisStepId = replica.parent().id();
            if (replica.transformation() instanceof StepCartesianTransformationOperator) {
                StepCartesianTransformationOperator transformation =
                        (StepCartesianTransformationOperator) replica.transformation();
                transformScale = transformation.scale();
            }
        }

        return new FaceSurfacePayload(
                base.type(),
                base.center(),
                base.axis(),
                base.xDirection(),
                base.radius(),
                base.minorRadius(),
                base.semiAngle(),
                base.lowerHeight(),
                base.upperHeight(),
                base.startAngle(),
                base.sweepAngle(),
                base.uDegree(),
                base.vDegree(),
                base.controlPoints(),
                base.uMultiplicities(),
                base.vMultiplicities(),
                base.uKnots(),
                base.vKnots(),
                StepPreviewJsonExporter.surfaceTypeName(geometry),
                geometry.id(),
                basisType,
                basisStepId,
                orientation,
                offsetDistance,
                trimU1, trimU2, trimV1, trimV2,
                implicitOuter,
                transformScale
        );
    }

    // ─── B-Spline Surface Building ──────────────────────────────────────────────────

    public static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface) {
            StepBSplineSurface splineSurface = (StepBSplineSurface) geometry;
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface =
                    (StepBSplineSurfaceWithKnotsAndBreakpoints) geometry;
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(
                StepPreviewJsonExporter.surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }

    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().get(0).size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof StepCartesianPoint) {
                    StepCartesianPoint cartesianPoint = (StepCartesianPoint) pt;
                    pointRow.add(builder.buildPoint(cartesianPoint.id()));
                } else {
                    throw new UnsupportedGeometryException(
                            "FREE_FORM_SURFACE control points must be Cartesian points");
                }
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        int uDegree = surface.degreeU();
        int vDegree = surface.degreeV();
        int uKnotCount = uCount + uDegree + 1;
        int vKnotCount = vCount + vDegree + 1;
        List<Double> uKnots = new ArrayList<>();
        for (int i = 0; i < uKnotCount; i++) {
            uKnots.add((double) i / (uKnotCount - 1));
        }
        List<Double> vKnots = new ArrayList<>();
        for (int i = 0; i < vKnotCount; i++) {
            vKnots.add((double) i / (vKnotCount - 1));
        }
        List<Integer> uMults = List.of(1);
        List<Integer> vMults = List.of(1);
        return new BSplineSurface3(uDegree, vDegree, controlPoints, uMults, vMults, uKnots, vKnots);
    }

    // ─── Helper Methods ──────────────────────────────────────────────────────────────

    private static List<LoopPayload> buildLoopPayloads(Face face) {
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(
                    PreviewFaceBuilder.sampleLoop(bound))));
        }
        return loops;
    }

    private static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize().asDirection();
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }
}