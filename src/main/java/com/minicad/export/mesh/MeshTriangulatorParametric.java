package com.minicad.export.mesh;

import com.minicad.geometry.*;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.core.base.StepFaceEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Handles parametric surface triangulation for mesh export.
 * Extracted from StepMeshExporter.Triangulator for better code organization.
 */
final class MeshTriangulatorParametric {

    private static final double PLANAR_EPS = 1e-9;
    private static final int DEFAULT_CURVE_SEGMENTS = 32;

    private MeshTriangulatorParametric() {
    }

    // --- Inner classes for UV geometry ---
    
    static final class UvPoint {
        private final double u;
        private final double v;

        UvPoint(double u, double v) {
            this.u = u;
            this.v = v;
        }
        double u() { return u; }
        double v() { return v; }
        @Override public boolean equals(Object o) { 
            return this == o || o != null && getClass() == o.getClass() 
                && Double.compare(u, ((UvPoint) o).u) == 0 
                && Double.compare(v, ((UvPoint) o).v) == 0; 
        }
        @Override public int hashCode() { return Objects.hash(u, v); }
    }

    static final class ParametricLoop {
        private final boolean outer;
        private final List<UvPoint> points;

        ParametricLoop(boolean outer, List<UvPoint> points) {
            this.outer = outer;
            this.points = points == null ? null : List.copyOf(points);
        }
        boolean outer() { return outer; }
        List<UvPoint> points() { return points; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParametricLoop that = (ParametricLoop) o;
            return outer == that.outer && Objects.equals(points, that.points);
        }
        @Override public int hashCode() { return Objects.hash(outer, points); }
    }

    static class UvBounds {
        private final double minU;
        private final double maxU;
        private final double minV;
        private final double maxV;

        UvBounds(double minU, double maxU, double minV, double maxV) {
            this.minU = minU;
            this.maxU = maxU;
            this.minV = minV;
            this.maxV = maxV;
        }
        double minU() { return minU; }
        double maxU() { return maxU; }
        double minV() { return minV; }
        double maxV() { return maxV; }
        double uSpan() { return maxU - minU; }
        double vSpan() { return maxV - minV; }
    }

    @FunctionalInterface
    interface SurfacePointSampler {
        CartesianPoint pointAt(double u, double v);
    }

    interface ParametricMapper {
        CartesianPoint pointAt(double u, double v);
        Vector3 normalAt(double u, double v);
        UvPoint project(CartesianPoint point, UvPoint previous);
        default Double uPeriod() { return null; }
        default Double vPeriod() { return null; }
    }

    // --- Main triangulation methods ---
    
    /**
     * Triangulates a parametric face using grid-based approach.
     *
     * @param face the face to triangulate
     * @param surface the surface geometry
     * @param flipped whether to flip the face normal
     * @param triangleCountSupplier supplies current triangle count
     * @param addVertex callback to add a vertex, returns vertex index
     * @param addTriangle callback to add a triangle (three vertex indices)
     * @return true if triangulation succeeded
     */
    static boolean triangulateParametricFace(
            Face face,
            SurfaceGeometry surface,
            boolean flipped,
            Supplier<Integer> triangleCountSupplier,
            BiFunction<CartesianPoint, Vector3, Integer> addVertex,
            Consumer<int[]> addTriangle
    ) {
        ParametricMapper mapper = mapperFor(surface);
        if (mapper == null) {
            return false;
        }
        List<ParametricLoop> loops = buildParametricLoops(face, mapper, surface);
        if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoop::outer)) {
            return false;
        }
        List<ParametricLoop> normalizedLoops = normalizeLoopPeriods(normalizeLoopRoles(loops), mapper);
        UvBounds bounds = boundsOf(normalizedLoops);
        if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
            return false;
        }
        int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
        int vSegments = Math.max(12, Math.min(48, sampleCount * 2));
        int trianglesBefore = triangleCountSupplier.get();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!containsParametricLoops(normalizedLoops, center)) {
                    continue;
                }
                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (flipped) {
                    normal = normal.negate();
                }
                appendOrientedTriangle(p00, p10, p11, normal, flipped, addVertex, addTriangle);
                appendOrientedTriangle(p00, p11, p01, normal, flipped, addVertex, addTriangle);
            }
        }
        return triangleCountSupplier.get() > trianglesBefore;
    }

    /**
     * Triangulates a semantic parametric face using grid-based approach.
     *
     * @param stepFace the STEP face entity
     * @param faceGeometry the face geometry entity
     * @param surface the surface geometry
     * @param builder the CAD builder
     * @param flipped whether to flip the face normal
     * @param triangleCountSupplier supplies current triangle count
     * @param addVertex callback to add a vertex, returns vertex index
     * @param addTriangle callback to add a triangle (three vertex indices)
     * @return true if triangulation succeeded
     */
    static boolean triangulateSemanticParametricFace(
            StepFaceEntity stepFace,
            StepEntity faceGeometry,
            SurfaceGeometry surface,
            StepCadBuilder builder,
            boolean flipped,
            Supplier<Integer> triangleCountSupplier,
            BiFunction<CartesianPoint, Vector3, Integer> addVertex,
            Consumer<int[]> addTriangle
    ) {
        ParametricMapper mapper = mapperFor(surface);
        if (mapper == null) {
            return false;
        }
        List<ParametricLoop> loops = buildSemanticParametricLoops(stepFace, faceGeometry, mapper, builder);
        if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoop::outer)) {
            return false;
        }
        List<ParametricLoop> normalizedLoops = normalizeLoopPeriods(normalizeLoopRoles(loops), mapper);
        UvBounds bounds = boundsOf(normalizedLoops);
        if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
            return false;
        }
        int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
        int vSegments = Math.max(12, Math.min(48, sampleCount * 2));
        int trianglesBefore = triangleCountSupplier.get();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!containsParametricLoops(normalizedLoops, center)) {
                    continue;
                }
                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (flipped) {
                    normal = normal.negate();
                }
                appendOrientedTriangle(p00, p10, p11, normal, flipped, addVertex, addTriangle);
                appendOrientedTriangle(p00, p11, p01, normal, flipped, addVertex, addTriangle);
            }
        }
        return triangleCountSupplier.get() > trianglesBefore;
    }

    // --- ParametricMapper factory ---
    
    static ParametricMapper mapperFor(SurfaceGeometry surface) {
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cylinder = (CylindricalSurface) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return cylinder.pointAt(u, v);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylinder.normalAt(u);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(cylinder.position().location());
                    double v = offset.dot(cylinder.position().axis().asVector());
                    Vector3 radial = offset.subtract(cylinder.position().axis().asVector().scale(v));
                    double u = Math.atan2(
                            radial.dot(cylinder.position().yDirection().asVector()),
                            radial.dot(cylinder.position().xDirection().asVector())
                    );
                    return new UvPoint(u, v);
                }
                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface cone = (ConicalSurface) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return cone.pointAt(u, v);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return cone.normalAt(u);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(cone.position().location());
                    double v = offset.dot(cone.position().axis().asVector());
                    Vector3 radial = offset.subtract(cone.position().axis().asVector().scale(v));
                    double u = Math.atan2(
                            radial.dot(cone.position().yDirection().asVector()),
                            radial.dot(cone.position().xDirection().asVector())
                    );
                    return new UvPoint(u, v);
                }
                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface torus = (ToroidalSurface) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return torus.pointAt(u, v);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return torus.normalAt(u, v);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(torus.position().location());
                    double localX = offset.dot(torus.position().xDirection().asVector());
                    double localY = offset.dot(torus.position().yDirection().asVector());
                    double localZ = offset.dot(torus.position().axis().asVector());
                    double u = Math.atan2(localY, localX);
                    double radialDist = Math.sqrt(localX * localX + localY * localY);
                    double v = Math.atan2(localZ, radialDist - torus.majorRadius());
                    return new UvPoint(u, v);
                }
                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphere = (SphericalSurface) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return sphere.pointAt(u, v);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return sphere.normalAt(u, v);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(sphere.position().location());
                    double radial = offset.norm();
                    if (radial <= PLANAR_EPS) {
                        return null;
                    }
                    double u = Math.atan2(
                            offset.dot(sphere.position().yDirection().asVector()),
                            offset.dot(sphere.position().xDirection().asVector())
                    );
                    double v = Math.acos(offset.dot(sphere.position().axis().asVector()) / radial);
                    return new UvPoint(u, v);
                }
                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (surface instanceof SurfaceOfRevolution3) {
            SurfaceOfRevolution3 revolution = (SurfaceOfRevolution3) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return revolution.pointAt(v, u);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return revolution.normalAt(v, u);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return approximateUv(point, previous, 96, 64,
                            0.0, Math.PI * 2.0,
                            -4.0, 4.0,
                            true,
                            (u, v) -> revolution.pointAt(v, u));
                }
                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            SurfaceOfLinearExtrusion3 extrusion = (SurfaceOfLinearExtrusion3) surface;
            return new ParametricMapper() {
                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return extrusion.pointAt(u, v);
                }
                @Override
                public Vector3 normalAt(double u, double v) {
                    return extrusion.normalAt(u, v);
                }
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return approximateUv(point, previous, 64, 48,
                            -4.0, 4.0,
                            0.0, 1.0,
                            false,
                            extrusion::pointAt);
                }
            };
        }
        return null;
    }

    // --- UV approximation ---
    
    private static UvPoint approximateUv(
            CartesianPoint point,
            UvPoint previous,
            int uSteps,
            int vSteps,
            double uMin,
            double uMax,
            double vMin,
            double vMax,
            boolean uPeriodic,
            SurfacePointSampler sampler
    ) {
        UvPoint best = null;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int ui = 0; ui <= uSteps; ui++) {
            double u = uMin + (uMax - uMin) * ui / uSteps;
            for (int vi = 0; vi <= vSteps; vi++) {
                double v = vMin + (vMax - vMin) * vi / vSteps;
                CartesianPoint sample = sampler.pointAt(u, v);
                double distance = sample.distanceTo(point);
                if (best == null || distance < bestDistance) {
                    best = new UvPoint(u, v);
                    bestDistance = distance;
                }
            }
        }
        if (best == null) {
            return null;
        }
        for (int pass = 0; pass < 3; pass++) {
            double uRadius = (uMax - uMin) / Math.pow(8.0, pass + 1);
            double vRadius = (vMax - vMin) / Math.pow(8.0, pass + 1);
            UvPoint refined = best;
            for (int ui = -4; ui <= 4; ui++) {
                double u = best.u() + uRadius * ui / 4.0;
                if (uPeriodic) {
                    u = wrapPeriodic(u - uMin, uMax - uMin) + uMin;
                } else {
                    u = clamp(u, uMin, uMax);
                }
                for (int vi = -4; vi <= 4; vi++) {
                    double v = best.v() + vRadius * vi / 4.0;
                    v = clamp(v, vMin, vMax);
                    CartesianPoint sample = sampler.pointAt(u, v);
                    double distance = sample.distanceTo(point);
                    if (distance < bestDistance) {
                        refined = new UvPoint(u, v);
                        bestDistance = distance;
                    }
                }
            }
            best = refined;
        }
        if (previous != null && uPeriodic) {
            return new UvPoint(unwrapPeriodic(best.u(), previous.u(), uMax - uMin), best.v());
        }
        return best;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double wrapPeriodic(double value, double period) {
        double wrapped = value % period;
        return wrapped < 0.0 ? wrapped + period : wrapped;
    }

    private static double unwrapPeriodic(double value, double reference, double period) {
        double unwrapped = value;
        while (unwrapped - reference > period * 0.5) {
            unwrapped -= period;
        }
        while (unwrapped - reference < -period * 0.5) {
            unwrapped += period;
        }
        return unwrapped;
    }

    // --- Loop building ---
    
    static List<ParametricLoop> buildParametricLoops(Face face, ParametricMapper mapper, SurfaceGeometry surface) {
        List<ParametricLoop> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            List<UvPoint> uvPoints = extractLoopUvPoints(bound.loop(), mapper, surface);
            if (!bound.orientation()) {
                uvPoints = reverseLoop(uvPoints);
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            if (uvPoints.size() < 3) {
                continue;
            }
            if (!sameUv(uvPoints.get(0), uvPoints.get(uvPoints.size() - 1))) {
                uvPoints.add(uvPoints.get(0));
            } else {
                uvPoints.set(uvPoints.size() - 1, uvPoints.get(0));
            }
            loops.add(new ParametricLoop(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> reverseLoop(List<UvPoint> points) {
        if (points.isEmpty()) {
            return points;
        }
        List<UvPoint> reversed = new ArrayList<>(points);
        Collections.reverse(reversed);
        return List.copyOf(reversed);
    }

    static List<ParametricLoop> buildSemanticParametricLoops(
            StepFaceEntity stepFace,
            StepEntity faceGeometry,
            ParametricMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoop> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.topology.StepFaceBound::outer);
        for (com.minicad.step.model.topology.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.topology.StepEdgeLoop)) {
                return List.of();
            }
            com.minicad.step.model.topology.StepEdgeLoop edgeLoop = (com.minicad.step.model.topology.StepEdgeLoop) bound.loop();
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.topology.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleSemanticOrientedEdge(orientedEdge, faceGeometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (!bound.orientation()) {
                loopPoints = reverseLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (loopPoints.size() < 3) {
                return List.of();
            }
            if (!sameUv(loopPoints.get(0), loopPoints.get(loopPoints.size() - 1))) {
                loopPoints.add(loopPoints.get(0));
            }
            loops.add(new ParametricLoop(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> extractLoopUvPoints(Loop loop, ParametricMapper mapper, SurfaceGeometry surface) {
        if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
            List<UvPoint> uvPoints = new ArrayList<>();
            UvPoint previous = null;
            for (CartesianPoint point : polyLoop.points()) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            return uvPoints;
        }
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            List<UvPoint> uvPoints = new ArrayList<>();
            for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = extractEdgeUvPoints(orientedEdge, mapper, surface);
                if (edgePoints.size() < 2) {
                    return List.of();
                }
                int startIndex = uvPoints.isEmpty() ? 0 : 1;
                for (int i = startIndex; i < edgePoints.size(); i++) {
                    uvPoints.add(edgePoints.get(i));
                }
            }
            if (uvPoints.size() > 1 && sameUv(uvPoints.get(0), uvPoints.get(uvPoints.size() - 1))) {
                uvPoints.remove(uvPoints.size() - 1);
            }
            return uvPoints;
        }
        return List.of();
    }

    private static List<UvPoint> sampleSemanticOrientedEdge(
            com.minicad.step.model.topology.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricMapper mapper,
            StepCadBuilder builder
    ) {
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        StepEntity associated = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> pcurves = matchingPcurves(associatedGeometry(associated), faceGeometry);
        CartesianPoint startPoint3d = mapPointIntoFaceGeometry(pointFromStep(orientedEdge.orientation()
                ? orientedEdge.edgeElement().start().point()
                : orientedEdge.edgeElement().end().point()), faceGeometry, builder);
        CartesianPoint endPoint3d = mapPointIntoFaceGeometry(pointFromStep(orientedEdge.orientation()
                ? orientedEdge.edgeElement().end().point()
                : orientedEdge.edgeElement().start().point()), faceGeometry, builder);
        UvPoint projectedStart = mapper.project(startPoint3d, null);
        UvPoint projectedEnd = projectedStart == null
                ? mapper.project(endPoint3d, null)
                : mapper.project(endPoint3d, projectedStart);
        List<UvPoint> best = List.of();
        double bestScore = Double.POSITIVE_INFINITY;
        for (StepEntity pcurve : pcurves) {
            Object built = builder.buildPcurve2(pcurve.id());
            if (!(built instanceof Curve2)) {
                continue;
            }
            Curve2 curve2 = (Curve2) built;
            List<UvPoint> sampled = sampleCurve2(curve2, projectedStart, projectedEnd);
            if (sampled.isEmpty()) {
                continue;
            }
            double score = score(projectedStart, projectedEnd, sampled);
            if (best.isEmpty() || score < bestScore) {
                best = sampled;
                bestScore = score;
            }
        }
        if (!best.isEmpty()) {
            return best;
        }
        Edge edge = builder.buildEdge(orientedEdge.edgeElement().id());
        List<CartesianPoint> points3d = edge.sample(DEFAULT_CURVE_SEGMENTS);
        if (!orientedEdge.orientation()) {
            points3d = new ArrayList<>(points3d);
            Collections.reverse(points3d);
        }
        CartesianPoint startPt = mapPointIntoFaceGeometry(points3d.get(0), faceGeometry, builder);
        CartesianPoint endPt = mapPointIntoFaceGeometry(points3d.get(points3d.size() - 1), faceGeometry, builder);
        UvPoint startUv = mapper.project(startPt, null);
        UvPoint endUv = mapper.project(endPt, startUv);
        if (startUv == null || endUv == null) {
            return List.of();
        }
        List<UvPoint> uvPoints = new ArrayList<>();
        for (int i = 0; i < points3d.size(); i++) {
            double t = (double) i / (points3d.size() - 1);
            double u = startUv.u() + (endUv.u() - startUv.u()) * t;
            double v = startUv.v() + (endUv.v() - startUv.v()) * t;
            uvPoints.add(new UvPoint(u, v));
        }
        return List.copyOf(uvPoints);
    }

    private static CartesianPoint mapPointIntoFaceGeometry(
            CartesianPoint point,
            StepEntity faceGeometry,
            StepCadBuilder builder
    ) {
        StepEntity current = faceGeometry;
        CartesianPoint mapped = point;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            if (current instanceof com.minicad.step.model.geometry.StepRectangularTrimmedSurface) {
                com.minicad.step.model.geometry.StepRectangularTrimmedSurface trimmedSurface = (com.minicad.step.model.geometry.StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepCurveBoundedSurface) {
                com.minicad.step.model.geometry.StepCurveBoundedSurface boundedSurface = (com.minicad.step.model.geometry.StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepOrientedSurface) {
                com.minicad.step.model.geometry.StepOrientedSurface orientedSurface = (com.minicad.step.model.geometry.StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepOffsetSurface) {
                com.minicad.step.model.geometry.StepOffsetSurface offsetSurface = (com.minicad.step.model.geometry.StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.product.StepGeometricReplica) {
                com.minicad.step.model.product.StepGeometricReplica replica = (com.minicad.step.model.product.StepGeometricReplica) current;
                if ("SURFACE_REPLICA".equals(replica.entityName())) {
                    mapped = StepMeshExporter.transformPoint3(mapped, replica.transformation(), builder);
                    current = replica.parent();
                    continue;
                }
            }
            break;
        }
        return mapped;
    }

    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        StepEntity current = edgeGeometry;
        for (int depth = 0; depth < 16; depth++) {
            if (current instanceof com.minicad.step.model.geometry.StepOrientedCurve) {
                com.minicad.step.model.geometry.StepOrientedCurve orientedCurve = (com.minicad.step.model.geometry.StepOrientedCurve) current;
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof com.minicad.step.model.product.StepGeometricReplica) {
                com.minicad.step.model.product.StepGeometricReplica replica = (com.minicad.step.model.product.StepGeometricReplica) current;
                if ("CURVE_REPLICA".equals(replica.entityName())) {
                    current = replica.parent();
                    continue;
                }
            }
            return current;
        }
        return current;
    }

    private static List<StepEntity> associatedGeometry(StepEntity edgeGeometry) {
        if (edgeGeometry instanceof com.minicad.step.model.geometry.StepSurfaceCurve) {
            com.minicad.step.model.geometry.StepSurfaceCurve surfaceCurve = (com.minicad.step.model.geometry.StepSurfaceCurve) edgeGeometry;
            return surfaceCurve.associatedGeometry();
        }
        if (edgeGeometry instanceof com.minicad.step.model.geometry.StepSeamCurve) {
            com.minicad.step.model.geometry.StepSeamCurve seamCurve = (com.minicad.step.model.geometry.StepSeamCurve) edgeGeometry;
            return seamCurve.associatedGeometry();
        }
        return List.of();
    }

    private static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof com.minicad.step.model.geometry.StepPcurve) {
                com.minicad.step.model.geometry.StepPcurve pcurve = (com.minicad.step.model.geometry.StepPcurve) associated;
                if (acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                    matches.add(pcurve);
                }
            } else if (associated instanceof com.minicad.step.model.geometry.StepDegeneratePcurve) {
                com.minicad.step.model.geometry.StepDegeneratePcurve pcurve = (com.minicad.step.model.geometry.StepDegeneratePcurve) associated;
                if (acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                    matches.add(pcurve);
                }
            }
        }
        return List.copyOf(matches);
    }

    private static Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        StepEntity current = faceGeometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            ids.add(current.id());
            if (current instanceof com.minicad.step.model.geometry.StepRectangularTrimmedSurface) {
                com.minicad.step.model.geometry.StepRectangularTrimmedSurface trimmedSurface = (com.minicad.step.model.geometry.StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepCurveBoundedSurface) {
                com.minicad.step.model.geometry.StepCurveBoundedSurface boundedSurface = (com.minicad.step.model.geometry.StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepOrientedSurface) {
                com.minicad.step.model.geometry.StepOrientedSurface orientedSurface = (com.minicad.step.model.geometry.StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof com.minicad.step.model.geometry.StepOffsetSurface) {
                com.minicad.step.model.geometry.StepOffsetSurface offsetSurface = (com.minicad.step.model.geometry.StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof com.minicad.step.model.product.StepGeometricReplica) {
                com.minicad.step.model.product.StepGeometricReplica replica = (com.minicad.step.model.product.StepGeometricReplica) current;
                if ("SURFACE_REPLICA".equals(replica.entityName())) {
                    current = replica.parent();
                    continue;
                }
            }
            break;
        }
        return Set.copyOf(ids);
    }

    private static CartesianPoint pointFromStep(com.minicad.step.model.geometry.StepCartesianPoint point) {
        return new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                point.coordinates().get(2)
        );
    }

    private static List<UvPoint> extractEdgeUvPoints(OrientedEdge orientedEdge, ParametricMapper mapper, SurfaceGeometry surface) {
        List<UvPoint> pcurvePoints = extractSurfaceCurveUvPoints(orientedEdge, mapper, surface);
        if (!pcurvePoints.isEmpty()) {
            return pcurvePoints;
        }
        List<CartesianPoint> points3d = orientSamples(orientedEdge, orientedEdge.edge().curve().sample(DEFAULT_CURVE_SEGMENTS));
        List<UvPoint> uvPoints = new ArrayList<>();
        UvPoint previous = null;
        for (CartesianPoint point : points3d) {
            UvPoint uv = mapper.project(point, previous);
            if (uv == null) {
                return List.of();
            }
            uvPoints.add(uv);
            previous = uv;
        }
        return uvPoints;
    }

    private static List<CartesianPoint> orientSamples(OrientedEdge orientedEdge, List<CartesianPoint> samples) {
        if (samples.isEmpty()) {
            return List.of(
                    orientedEdge.startVertex().point(),
                    orientedEdge.endVertex().point()
            );
        }
        List<CartesianPoint> oriented = new ArrayList<>(samples);
        CartesianPoint expectedStart = orientedEdge.startVertex().point();
        CartesianPoint expectedEnd = orientedEdge.endVertex().point();
        double forward = samples.get(0).distanceTo(expectedStart) + samples.get(samples.size() - 1).distanceTo(expectedEnd);
        double backward = samples.get(0).distanceTo(expectedEnd) + samples.get(samples.size() - 1).distanceTo(expectedStart);
        if (backward < forward) {
            Collections.reverse(oriented);
        }
        if (!oriented.get(0).equals(expectedStart)) {
            oriented.set(0, expectedStart);
        }
        if (!oriented.get(oriented.size() - 1).equals(expectedEnd)) {
            oriented.set(oriented.size() - 1, expectedEnd);
        }
        return List.copyOf(oriented);
    }

    private static List<UvPoint> extractSurfaceCurveUvPoints(OrientedEdge orientedEdge, ParametricMapper mapper, SurfaceGeometry surface) {
        Curve3 curve = orientedEdge.edge().curve();
        if (!(curve instanceof SurfaceCurve3)) {
            return List.of();
        }
        SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
        List<SurfaceCurve3.ParametricCurve> bindings = matchingParametricCurves(surfaceCurve, surface);
        if (bindings.isEmpty()) {
            return List.of();
        }
        UvPoint projectedStart = mapper.project(orientedEdge.startVertex().point(), null);
        UvPoint projectedEnd = projectedStart == null
                ? mapper.project(orientedEdge.endVertex().point(), null)
                : mapper.project(orientedEdge.endVertex().point(), projectedStart);
        List<UvPoint> best = List.of();
        double bestScore = Double.POSITIVE_INFINITY;
        for (SurfaceCurve3.ParametricCurve binding : bindings) {
            List<UvPoint> sampled = sampleCurve2(binding.curve2(), projectedStart, projectedEnd);
            if (sampled.isEmpty()) {
                continue;
            }
            List<UvPoint> uvPoints = normalizePeriodicLoop(sampled, mapper);
            List<UvPoint> oriented = orientUvSamples(orientedEdge, uvPoints, mapper);
            if (oriented.size() < 2) {
                continue;
            }
            double score = uvDistance(oriented.get(0), projectedStart) + uvDistance(oriented.get(oriented.size() - 1), projectedEnd);
            if (best.isEmpty() || score < bestScore) {
                best = oriented;
                bestScore = score;
            }
        }
        return best;
    }

    private static List<SurfaceCurve3.ParametricCurve> matchingParametricCurves(SurfaceCurve3 surfaceCurve, SurfaceGeometry surface) {
        List<SurfaceCurve3.ParametricCurve> matches = new ArrayList<>();
        for (SurfaceCurve3.ParametricCurve binding : surfaceCurve.parametricCurves()) {
            if (binding.surface().equals(surface)) {
                matches.add(binding);
            }
        }
        return List.copyOf(matches);
    }

    private static List<UvPoint> orientUvSamples(OrientedEdge orientedEdge, List<UvPoint> samples, ParametricMapper mapper) {
        if (samples.isEmpty()) {
            return List.of();
        }
        List<UvPoint> oriented = new ArrayList<>(samples);
        UvPoint expectedStart = mapper.project(orientedEdge.startVertex().point(), null);
        UvPoint expectedEnd = expectedStart == null
                ? mapper.project(orientedEdge.endVertex().point(), null)
                : mapper.project(orientedEdge.endVertex().point(), expectedStart);
        if (expectedStart != null) {
            oriented.set(0, alignToReference(expectedStart, oriented.get(0), mapper));
        }
        if (expectedEnd != null) {
            oriented.set(oriented.size() - 1, alignToReference(expectedEnd, oriented.get(oriented.size() - 1), mapper));
        }
        double forward = uvDistance(oriented.get(0), expectedStart) + uvDistance(oriented.get(oriented.size() - 1), expectedEnd);
        double backward = uvDistance(oriented.get(0), expectedEnd) + uvDistance(oriented.get(oriented.size() - 1), expectedStart);
        if (backward < forward) {
            Collections.reverse(oriented);
        }
        if (expectedStart != null) {
            oriented.set(0, alignToReference(expectedStart, oriented.get(0), mapper));
        }
        if (expectedEnd != null) {
            oriented.set(oriented.size() - 1, alignToReference(expectedEnd, oriented.get(oriented.size() - 1), mapper));
        }
        return List.copyOf(oriented);
    }

    private static UvPoint alignToReference(UvPoint point, UvPoint reference, ParametricMapper mapper) {
        if (point == null || reference == null) {
            return point;
        }
        double u = point.u();
        double v = point.v();
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        if (uPeriod != null) {
            while (u - reference.u() > uPeriod * 0.5) {
                u -= uPeriod;
            }
            while (u - reference.u() < -uPeriod * 0.5) {
                u += uPeriod;
            }
        }
        if (vPeriod != null) {
            while (v - reference.v() > vPeriod * 0.5) {
                v -= vPeriod;
            }
            while (v - reference.v() < -vPeriod * 0.5) {
                v += vPeriod;
            }
        }
        return new UvPoint(u, v);
    }

    // --- Curve2 sampling ---
    
    private static List<UvPoint> sampleCurve2(Curve2 curve, UvPoint start, UvPoint end) {
        if (curve instanceof Line2) {
            Line2 line = (Line2) curve;
            return sampleLinePcurve(line, start, end);
        }
        if (curve instanceof Circle2) {
            Circle2 circle = (Circle2) curve;
            return sampleCirclePcurve(circle, start, end);
        }
        if (curve instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) curve;
            return sampleEllipsePcurve(ellipse, start, end);
        }
        if (curve instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) curve;
            return sampleSplinePcurve(spline, start, end);
        }
        if (curve instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmed = (TrimmedCurve2) curve;
            return sampleTrimmedPcurve(trimmed, start, end);
        }
        return List.of();
    }

    private static List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startParameter = line.parameterOf(startPoint);
        double endParameter = line.parameterOf(endPoint);
        int segments = Math.max(12, (int) Math.ceil(Math.abs(endParameter - startParameter) * 6.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = startParameter + (endParameter - startParameter) * index / segments;
            Point2 point = line.pointAt(parameter);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint start, UvPoint end) {
        List<Point2> sampled = spline.sample(48);
        if (sampled.size() < 2) {
            return List.of();
        }
        int startIndex = closestPointIndex(sampled, start);
        int endIndex = closestPointIndex(sampled, end);
        if (startIndex == endIndex) {
            return List.of(start, end);
        }
        List<UvPoint> points = new ArrayList<>();
        int step = startIndex <= endIndex ? 1 : -1;
        for (int index = startIndex; index != endIndex + step; index += step) {
            Point2 point = sampled.get(index);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static int closestPointIndex(List<Point2> points, UvPoint target) {
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            Point2 point = points.get(index);
            double du = point.x() - target.u();
            double dv = point.y() - target.v();
            double distance = du * du + dv * dv;
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    private static List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startAngle = circle.angleOf(startPoint);
        double endAngle = circle.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            Point2 point = circle.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startAngle = ellipse.angleOf(startPoint);
        double endAngle = ellipse.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            Point2 point = ellipse.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint start, UvPoint end) {
        UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
        UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
        List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
        List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
        if (forward.isEmpty() && reverse.isEmpty()) {
            return List.of();
        }
        List<UvPoint> preferred;
        if (!trimmed.senseAgreement()) {
            preferred = reverse.isEmpty() ? forward : reverse;
        } else {
            preferred = score(start, end, forward) <= score(start, end, reverse) ? forward : reverse;
        }
        return alignTrimmedSamples(preferred, start, end);
    }

    private static double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
        if (samples.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return uvDistance(start, samples.get(0)) + uvDistance(end, samples.get(samples.size() - 1));
    }

    private static List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint start, UvPoint end) {
        if (samples.isEmpty()) {
            return samples;
        }
        List<UvPoint> aligned = new ArrayList<>(samples);
        double forwardScore = uvDistance(start, aligned.get(0)) + uvDistance(end, aligned.get(aligned.size() - 1));
        double reverseScore = uvDistance(start, aligned.get(aligned.size() - 1)) + uvDistance(end, aligned.get(0));
        if (reverseScore < forwardScore) {
            Collections.reverse(aligned);
        }
        aligned.set(0, start);
        aligned.set(aligned.size() - 1, end);
        return List.copyOf(aligned);
    }

    // --- UV utilities ---
    
    private static double uvDistance(UvPoint a, UvPoint b) {
        if (a == null || b == null) {
            return Double.POSITIVE_INFINITY;
        }
        double du = a.u() - b.u();
        double dv = a.v() - b.v();
        return du * du + dv * dv;
    }

    private static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricMapper mapper) {
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
                    while (u - previous.u() > uPeriod * 0.5) {
                        u -= uPeriod;
                    }
                    while (u - previous.u() < -uPeriod * 0.5) {
                        u += uPeriod;
                    }
                }
                if (vPeriod != null) {
                    while (v - previous.v() > vPeriod * 0.5) {
                        v -= vPeriod;
                    }
                    while (v - previous.v() < -vPeriod * 0.5) {
                        v += vPeriod;
                    }
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        return normalized;
    }

    static List<ParametricLoop> normalizeLoopRoles(List<ParametricLoop> loops) {
        if (loops.stream().anyMatch(ParametricLoop::outer)) {
            return loops;
        }
        int outerIndex = -1;
        double outerArea = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < loops.size(); i++) {
            double area = Math.abs(signedAreaUv(loops.get(i).points()));
            if (area > outerArea + PLANAR_EPS) {
                outerArea = area;
                outerIndex = i;
            }
        }
        if (outerIndex < 0) {
            return loops;
        }
        List<ParametricLoop> normalized = new ArrayList<>(loops.size());
        for (int i = 0; i < loops.size(); i++) {
            normalized.add(new ParametricLoop(i == outerIndex, loops.get(i).points()));
        }
        return List.copyOf(normalized);
    }

    static List<ParametricLoop> normalizeLoopPeriods(List<ParametricLoop> loops, ParametricMapper mapper) {
        if (loops.isEmpty()) {
            return loops;
        }
        ParametricLoop outer = loops.stream().filter(ParametricLoop::outer).findFirst().orElse(null);
        if (outer == null) {
            return loops;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        if (uPeriod == null && vPeriod == null) {
            return loops;
        }
        UvPoint outerCenter = centroidUv(outer.points());
        List<ParametricLoop> normalized = new ArrayList<>(loops.size());
        for (ParametricLoop loop : loops) {
            if (loop.outer()) {
                normalized.add(loop);
                continue;
            }
            UvPoint center = centroidUv(loop.points());
            int bestUShift = 0;
            int bestVShift = 0;
            double bestDistance = uvDistance(center, outerCenter);
            for (int uShift = -1; uShift <= 1; uShift++) {
                if (uPeriod == null && uShift != 0) {
                    continue;
                }
                for (int vShift = -1; vShift <= 1; vShift++) {
                    if (vPeriod == null && vShift != 0) {
                        continue;
                    }
                    UvPoint shiftedCenter = new UvPoint(
                            center.u() + (uPeriod == null ? 0.0 : uPeriod * uShift),
                            center.v() + (vPeriod == null ? 0.0 : vPeriod * vShift));
                    double distance = uvDistance(shiftedCenter, outerCenter);
                    if (distance + PLANAR_EPS < bestDistance) {
                        bestDistance = distance;
                        bestUShift = uShift;
                        bestVShift = vShift;
                    }
                }
            }
            double du = uPeriod == null ? 0.0 : uPeriod * bestUShift;
            double dv = vPeriod == null ? 0.0 : vPeriod * bestVShift;
            List<UvPoint> shifted = loop.points().stream()
                    .map(point -> new UvPoint(point.u() + du, point.v() + dv))
                    .collect(Collectors.toList());
            normalized.add(new ParametricLoop(false, shifted));
        }
        return List.copyOf(normalized);
    }

    private static UvPoint centroidUv(List<UvPoint> points) {
        if (points.isEmpty()) {
            return new UvPoint(0.0, 0.0);
        }
        int count = points.size();
        if (count > 1 && sameUv(points.get(0), points.get(points.size() - 1))) {
            count--;
        }
        if (count <= 0) {
            return points.get(0);
        }
        double sumU = 0.0;
        double sumV = 0.0;
        for (int i = 0; i < count; i++) {
            sumU += points.get(i).u();
            sumV += points.get(i).v();
        }
        return new UvPoint(sumU / count, sumV / count);
    }

    static UvBounds boundsOf(List<ParametricLoop> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        boolean found = false;
        for (ParametricLoop loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                maxU = Math.max(maxU, point.u());
                minV = Math.min(minV, point.v());
                maxV = Math.max(maxV, point.v());
                found = true;
            }
        }
        return found ? new UvBounds(minU, maxU, minV, maxV) : null;
    }

    static boolean containsParametricLoops(List<ParametricLoop> loops, UvPoint point) {
        ParametricLoop outer = loops.stream().filter(ParametricLoop::outer).findFirst().orElse(null);
        if (outer == null) {
            return false;
        }
        UvBounds outerBox = loopBoundingBox(outer);
        if (point.u() < outerBox.minU() || point.u() > outerBox.maxU()
                || point.v() < outerBox.minV() || point.v() > outerBox.maxV()) {
            return false;
        }
        if (!containsUvPolygon(outer.points(), point)) {
            return false;
        }
        for (ParametricLoop hole : loops) {
            if (!hole.outer()) {
                UvBounds holeBox = loopBoundingBox(hole);
                if (point.u() < holeBox.minU() || point.u() > holeBox.maxU()
                        || point.v() < holeBox.minV() || point.v() > holeBox.maxV()) {
                    continue;
                }
                if (containsUvPolygon(hole.points(), point)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static UvBounds loopBoundingBox(ParametricLoop loop) {
        double minU = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (UvPoint p : loop.points()) {
            double pu = p.u();
            double pv = p.v();
            if (pu < minU) minU = pu;
            if (pu > maxU) maxU = pu;
            if (pv < minV) minV = pv;
            if (pv > maxV) maxV = pv;
        }
        return new UvBounds(minU, maxU, minV, maxV);
    }

    private static boolean containsUvPolygon(List<UvPoint> polygon, UvPoint point) {
        if (polygon.size() < 3) {
            return false;
        }
        if (isOnPolygonBoundary(polygon, point)) {
            return true;
        }
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            UvPoint a = polygon.get(i);
            UvPoint b = polygon.get(j);
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u();
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
        for (int i = 0; i + 1 < polygon.size(); i++) {
            if (isOnSegment(polygon.get(i), polygon.get(i + 1), point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
        double abU = b.u() - a.u();
        double abV = b.v() - a.v();
        double lengthSquared = abU * abU + abV * abV;
        if (lengthSquared <= 1.0e-18) {
            return distanceSquared(a, point) <= 1.0e-18;
        }
        double apU = point.u() - a.u();
        double apV = point.v() - a.v();
        double cross = abU * apV - abV * apU;
        if (Math.abs(cross) > 1.0e-9) {
            return false;
        }
        double dot = apU * abU + apV * abV;
        if (dot < -1.0e-9) {
            return false;
        }
        return dot <= lengthSquared + 1.0e-9;
    }

    private static boolean sameUv(UvPoint left, UvPoint right) {
        return distanceSquared(left, right) <= 1.0e-12;
    }

    private static double distanceSquared(UvPoint left, UvPoint right) {
        double du = left.u() - right.u();
        double dv = left.v() - right.v();
        return du * du + dv * dv;
    }

    private static double signedAreaUv(List<UvPoint> points) {
        if (points.size() < 3) {
            return 0.0;
        }
        double area = 0.0;
        for (int i = 0; i + 1 < points.size(); i++) {
            UvPoint current = points.get(i);
            UvPoint next = points.get(i + 1);
            area += current.u() * next.v() - next.u() * current.v();
        }
        return area * 0.5;
    }

    private static final double MIN_TRIANGLE_AREA = 1e-12;

    private static void appendOrientedTriangle(
            CartesianPoint p0,
            CartesianPoint p1,
            CartesianPoint p2,
            Vector3 normal,
            boolean flipped,
            BiFunction<CartesianPoint, Vector3, Integer> addVertex,
            Consumer<int[]> addTriangle
    ) {
        if (triangleArea(p0, p1, p2) <= MIN_TRIANGLE_AREA) {
            return;
        }
        int v0 = addVertex.apply(p0, normal);
        int v1 = addVertex.apply(p1, normal);
        int v2 = addVertex.apply(p2, normal);
        if (flipped) {
            addTriangle.accept(new int[]{v0, v2, v1});
        } else {
            addTriangle.accept(new int[]{v0, v1, v2});
        }
    }

    private static double triangleArea(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
        return b.subtract(a).cross(c.subtract(a)).norm() * 0.5;
    }
}