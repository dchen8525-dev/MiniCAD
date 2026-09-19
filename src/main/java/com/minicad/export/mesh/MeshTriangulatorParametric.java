package com.minicad.export.mesh;

import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.geometry.*;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.sampling.ParametricWindowWalk;
import com.minicad.preview.sampling.PcurveSamplingHelper;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

    @FunctionalInterface
    interface SurfacePointSampler {
        CartesianPoint pointAt(double u, double v);
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
        ParametricSurfaceMapper mapper = mapperFor(surface);
        if (mapper == null) {
            return false;
        }
        List<ParametricLoopPayload> loops = buildParametricLoops(face, mapper, surface);
        if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoopPayload::outer)) {
            return false;
        }
        List<ParametricLoopPayload> normalizedLoops =
                normalizeLoopPeriods(PreviewMeshExporter.normalizeLoopRoles(loops), mapper);
        UvBounds bounds = PreviewMeshExporter.boundsOf(normalizedLoops);
        if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
            return false;
        }
        int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
        int vSegments = Math.max(12, Math.min(48, sampleCount * 2));
        return triangulateGrid(mapper, normalizedLoops, bounds, uSegments, vSegments, flipped, triangleCountSupplier, addVertex, addTriangle);
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
        ParametricSurfaceMapper mapper = mapperFor(surface);
        if (mapper == null) {
            return false;
        }
        List<ParametricLoopPayload> loops = buildSemanticParametricLoops(stepFace, faceGeometry, mapper, builder);
        if (loops.isEmpty() || loops.stream().noneMatch(ParametricLoopPayload::outer)) {
            return false;
        }
        List<ParametricLoopPayload> normalizedLoops =
                normalizeLoopPeriods(PreviewMeshExporter.normalizeLoopRoles(loops), mapper);
        UvBounds bounds = PreviewMeshExporter.boundsOf(normalizedLoops);
        if (bounds == null || bounds.uSpan() <= PLANAR_EPS || bounds.vSpan() <= PLANAR_EPS) {
            return false;
        }
        int sampleCount = normalizedLoops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        int uSegments = Math.max(16, Math.min(64, sampleCount * 2));
        int vSegments = Math.max(12, Math.min(48, sampleCount * 2));
        return triangulateGrid(mapper, normalizedLoops, bounds, uSegments, vSegments, flipped, triangleCountSupplier, addVertex, addTriangle);
    }

    // --- ParametricSurfaceMapper factory ---

    /**
     * Triangulates the parametric bounds with a shared corner grid: every
     * (u,v) lattice point is evaluated exactly once instead of once per
     * adjacent cell (up to 4x), and loop bounds are precomputed instead of
     * being rebuilt for every cell's containment test.
     *
     * <p>The cell walk itself is not this file's: it is
     * {@link ParametricWindowWalk}, which the json/glb triangulator runs too.
     * This method keeps the corner grid, which is the one part the other consumer
     * does not share, and the vertex/triangle emission, which it cannot.</p>
     */
    private static boolean triangulateGrid(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> normalizedLoops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean flipped,
            Supplier<Integer> triangleCountSupplier,
            BiFunction<CartesianPoint, Vector3, Integer> addVertex,
            Consumer<int[]> addTriangle
    ) {
        ParametricWindowWalk.Region region = ParametricWindowWalk.Region.of(normalizedLoops);
        if (region == null) {
            return false;
        }
        CartesianPoint[][] grid = new CartesianPoint[uSegments + 1][vSegments + 1];
        for (int ui = 0; ui <= uSegments; ui++) {
            double u = bounds.minU() + bounds.uSpan() * ui / uSegments;
            for (int vi = 0; vi <= vSegments; vi++) {
                double v = bounds.minV() + bounds.vSpan() * vi / vSegments;
                grid[ui][vi] = mapper.pointAt(u, v);
            }
        }
        int trianglesBefore = triangleCountSupplier.get();
        ParametricWindowWalk.walk(bounds, uSegments, vSegments, region, cell -> {
            CartesianPoint p00 = grid[cell.uIndex()][cell.vIndex()];
            CartesianPoint p10 = grid[cell.uIndex() + 1][cell.vIndex()];
            CartesianPoint p01 = grid[cell.uIndex()][cell.vIndex() + 1];
            CartesianPoint p11 = grid[cell.uIndex() + 1][cell.vIndex() + 1];
            Vector3 normal = mapper.normalAt(cell.center().u(), cell.center().v());
            if (flipped) {
                normal = normal.negate();
            }
            appendOrientedTriangle(p00, p10, p11, normal, flipped, addVertex, addTriangle);
            appendOrientedTriangle(p00, p11, p01, normal, flipped, addVertex, addTriangle);
        });
        return triangleCountSupplier.get() > trianglesBefore;
    }

    /**
     * Builds the {@link ParametricSurfaceMapper} for one concrete surface type. The
     * handler receives the already type-tested surface and owns the cast, so
     * each rule body is the original branch body verbatim.
     */
    @FunctionalInterface
    private interface ParametricMapperHandler {
        ParametricSurfaceMapper build(SurfaceGeometry surface);
    }

    private record ParametricMapperRule(
            Class<? extends SurfaceGeometry> type, ParametricMapperHandler handler) {
        boolean matches(SurfaceGeometry surface) {
            return type.isInstance(surface);
        }
    }

    private static ParametricMapperRule parametricMapperRule(
            Class<? extends SurfaceGeometry> type, ParametricMapperHandler handler) {
        return new ParametricMapperRule(type, handler);
    }

    /**
     * Parametric-mapper rules keyed by concrete surface type, replacing the
     * former 6-branch if/else-if chain. Order mirrors the original chain (first
     * match wins); a surface matching no rule yields null, as the old trailing
     * return did, which the callers read as "not parametrically triangulable".
     */
    private static final List<ParametricMapperRule> MAPPER_RULES = List.of(
            parametricMapperRule(CylindricalSurface.class, (surface) -> {
                CylindricalSurface cylinder = (CylindricalSurface) surface;
                return new ParametricSurfaceMapper() {
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
            }),
            parametricMapperRule(ConicalSurface.class, (surface) -> {
                ConicalSurface cone = (ConicalSurface) surface;
                return new ParametricSurfaceMapper() {
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
            }),
            parametricMapperRule(ToroidalSurface.class, (surface) -> {
                ToroidalSurface torus = (ToroidalSurface) surface;
                return new ParametricSurfaceMapper() {
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
            }),
            parametricMapperRule(SphericalSurface.class, (surface) -> {
                SphericalSurface sphere = (SphericalSurface) surface;
                return new ParametricSurfaceMapper() {
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
            }),
            parametricMapperRule(SurfaceOfRevolution3.class, (surface) -> {
                SurfaceOfRevolution3 revolution = (SurfaceOfRevolution3) surface;
                return new ParametricSurfaceMapper() {
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
                        return projectRevolutionUv(revolution, point, previous);
                    }
                    @Override
                    public Double uPeriod() {
                        return Math.PI * 2.0;
                    }
                };
            }),
            parametricMapperRule(SurfaceOfLinearExtrusion3.class, (surface) -> {
                SurfaceOfLinearExtrusion3 extrusion = (SurfaceOfLinearExtrusion3) surface;
                return new ParametricSurfaceMapper() {
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
                        return projectExtrusionUv(extrusion, point, previous);
                    }
                };
            })
    );

    static ParametricSurfaceMapper mapperFor(SurfaceGeometry surface) {
        for (ParametricMapperRule rule : MAPPER_RULES) {
            if (rule.matches(surface)) {
                return rule.handler().build(surface);
            }
        }
        return null;
    }

    // --- UV approximation ---

    /** Generatrix-parameter window scanned when a grid fallback is needed. */
    private static final double GENERATRIX_MIN = -4.0;
    private static final double GENERATRIX_MAX = 4.0;
    private static final int GENERATRIX_STEPS = 96;
    private static final double TWO_PI = Math.PI * 2.0;

    /**
     * Projects a point onto a surface of revolution without a 2D grid scan.
     *
     * <p>For a generatrix point C(s), the revolving circle is centered on the
     * axis at height z(s) with radius r(s), so the squared distance from the
     * target to that circle is (r(s) - r)^2 + (z(s) - z)^2 — a 1D minimization
     * over s. The angle is then an atan2 in the generatrix's radial frame,
     * matching the moving reference frame used by
     * {@link SurfaceOfRevolution3#pointAt(double, double)}. Falls back to the
     * grid search when the closed-form result fails to reproduce on the surface.
     */
    private static UvPoint projectRevolutionUv(
            SurfaceOfRevolution3 revolution, CartesianPoint point, UvPoint previous) {
        Vector3 axis = revolution.axisDirection().asVector();
        double axisNorm = axis.norm();
        if (axisNorm < 1e-12) {
            return approximateUv(point, previous, 96, 64,
                    0.0, TWO_PI, GENERATRIX_MIN, GENERATRIX_MAX, true,
                    (u, v) -> revolution.pointAt(v, u));
        }
        axis = axis.scale(1.0 / axisNorm);
        CartesianPoint origin = revolution.axisOrigin();
        Vector3 offset = point.subtract(origin);
        double axialP = offset.dot(axis);
        Vector3 radialP = offset.subtract(axis.scale(axialP));
        double radiusP = radialP.norm();

        Curve3 generatrix = revolution.sweptCurve();
        double bestS = GENERATRIX_MIN;
        double bestDistanceSq = Double.POSITIVE_INFINITY;
        for (int i = 0; i <= GENERATRIX_STEPS; i++) {
            double s = GENERATRIX_MIN + (GENERATRIX_MAX - GENERATRIX_MIN) * i / GENERATRIX_STEPS;
            double distanceSq = revolutionCircleDistanceSq(generatrix, origin, axis, s, axialP, radiusP);
            if (distanceSq < bestDistanceSq) {
                bestDistanceSq = distanceSq;
                bestS = s;
            }
        }
        double radius = (GENERATRIX_MAX - GENERATRIX_MIN) / GENERATRIX_STEPS;
        for (int pass = 0; pass < 3; pass++) {
            for (int k = -4; k <= 4; k++) {
                double s = clamp(bestS + radius * k / 4.0, GENERATRIX_MIN, GENERATRIX_MAX);
                double distanceSq = revolutionCircleDistanceSq(generatrix, origin, axis, s, axialP, radiusP);
                if (distanceSq < bestDistanceSq) {
                    bestDistanceSq = distanceSq;
                    bestS = s;
                }
            }
            radius /= 8.0;
        }

        double angle = revolutionAngleAt(generatrix, origin, axis, bestS, radialP, radiusP, previous);
        double u = wrapPeriodic(angle, TWO_PI);
        if (previous != null) {
            u = unwrapPeriodic(u, previous.u(), TWO_PI);
        }
        UvPoint candidate = new UvPoint(u, bestS);
        double actual = revolution.pointAt(bestS, u).distanceTo(point);
        if (actual > Math.sqrt(bestDistanceSq) + 1e-6 * (1.0 + Math.sqrt(bestDistanceSq))) {
            return approximateUv(point, previous, 96, 64,
                    0.0, TWO_PI, GENERATRIX_MIN, GENERATRIX_MAX, true,
                    (a, b) -> revolution.pointAt(b, a));
        }
        return candidate;
    }

    /** Squared distance from the target (axialP, radiusP) to the revolving circle at generatrix parameter s. */
    private static double revolutionCircleDistanceSq(
            Curve3 generatrix, CartesianPoint origin, Vector3 axis, double s, double axialP, double radiusP) {
        Vector3 offset = generatrix.pointAt(s).subtract(origin);
        double axial = offset.dot(axis);
        double radial = offset.subtract(axis.scale(axial)).norm();
        double dAxial = axial - axialP;
        double dRadial = radial - radiusP;
        return dAxial * dAxial + dRadial * dRadial;
    }

    /**
     * Revolution angle of the target around the axis, measured in the same
     * generatrix-relative frame {@link SurfaceOfRevolution3#pointAt(double, double)} uses.
     */
    private static double revolutionAngleAt(
            Curve3 generatrix, CartesianPoint origin, Vector3 axis, double s,
            Vector3 radialP, double radiusP, UvPoint previous) {
        if (radiusP < 1e-12) {
            return previous != null ? previous.u() : 0.0;
        }
        Vector3 offset = generatrix.pointAt(s).subtract(origin);
        Vector3 axial = axis.scale(offset.dot(axis));
        Vector3 radialC = offset.subtract(axial);
        if (radialC.norm() < 1e-12) {
            return previous != null ? previous.u() : 0.0;
        }
        Vector3 perp1 = radialC.normalize();
        Vector3 perp2 = axis.cross(perp1).normalize();
        Vector3 targetDir = radialP.scale(1.0 / radiusP);
        return Math.atan2(targetDir.dot(perp2), targetDir.dot(perp1));
    }

    /**
     * Projects a point onto a surface of linear extrusion without a 2D grid
     * scan: for a fixed generatrix parameter s the extrusion segment is the
     * straight line C(s) + t·direction (t in [0,1]), whose closest point to the
     * target is closed-form, leaving a 1D minimization over s. Falls back to
     * the grid search when the closed-form result fails to reproduce.
     */
    private static UvPoint projectExtrusionUv(
            SurfaceOfLinearExtrusion3 extrusion, CartesianPoint point, UvPoint previous) {
        Vector3 direction = extrusion.extrusionVector();
        double directionSq = direction.dot(direction);
        if (directionSq < 1e-24) {
            return approximateUv(point, previous, 64, 48,
                    GENERATRIX_MIN, GENERATRIX_MAX, 0.0, 1.0, false, extrusion::pointAt);
        }
        Curve3 generatrix = extrusion.sweptCurve();
        double bestS = GENERATRIX_MIN;
        double bestT = 0.0;
        double bestDistanceSq = Double.POSITIVE_INFINITY;
        for (int i = 0; i <= GENERATRIX_STEPS; i++) {
            double s = GENERATRIX_MIN + (GENERATRIX_MAX - GENERATRIX_MIN) * i / GENERATRIX_STEPS;
            double[] closest = extrusionSegmentClosest(generatrix, direction, directionSq, s, point);
            if (closest[1] < bestDistanceSq) {
                bestDistanceSq = closest[1];
                bestS = s;
                bestT = closest[0];
            }
        }
        double radius = (GENERATRIX_MAX - GENERATRIX_MIN) / GENERATRIX_STEPS;
        for (int pass = 0; pass < 3; pass++) {
            for (int k = -4; k <= 4; k++) {
                double s = clamp(bestS + radius * k / 4.0, GENERATRIX_MIN, GENERATRIX_MAX);
                double[] closest = extrusionSegmentClosest(generatrix, direction, directionSq, s, point);
                if (closest[1] < bestDistanceSq) {
                    bestDistanceSq = closest[1];
                    bestS = s;
                    bestT = closest[0];
                }
            }
            radius /= 8.0;
        }
        UvPoint candidate = new UvPoint(bestS, bestT);
        double actual = extrusion.pointAt(bestS, bestT).distanceTo(point);
        if (actual > Math.sqrt(bestDistanceSq) + 1e-6 * (1.0 + Math.sqrt(bestDistanceSq))) {
            return approximateUv(point, previous, 64, 48,
                    GENERATRIX_MIN, GENERATRIX_MAX, 0.0, 1.0, false, extrusion::pointAt);
        }
        return candidate;
    }

    /** Returns {t, distanceSquared} for the closest point on the segment C(s) + t·direction, t clamped to [0,1]. */
    private static double[] extrusionSegmentClosest(
            Curve3 generatrix, Vector3 direction, double directionSq, double s, CartesianPoint point) {
        Vector3 offset = point.subtract(generatrix.pointAt(s));
        double t = clamp(offset.dot(direction) / directionSq, 0.0, 1.0);
        Vector3 delta = offset.subtract(direction.scale(t));
        return new double[] {t, delta.normSquared()};
    }

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

    static List<ParametricLoopPayload> buildParametricLoops(Face face, ParametricSurfaceMapper mapper, SurfaceGeometry surface) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            List<UvPoint> uvPoints = extractLoopUvPoints(bound.loop(), mapper, surface);
            if (!bound.orientation()) {
                uvPoints = reverseLoop(uvPoints);
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            if (uvPoints.size() < 3) {
                continue;
            }
            if (!PcurveSamplingHelper.sameUv(uvPoints.get(0), uvPoints.get(uvPoints.size() - 1))) {
                uvPoints.add(uvPoints.get(0));
            } else {
                uvPoints.set(uvPoints.size() - 1, uvPoints.get(0));
            }
            loops.add(new ParametricLoopPayload(bound.outer(), uvPoints));
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

    static List<ParametricLoopPayload> buildSemanticParametricLoops(
            StepFaceEntity stepFace,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer);
        for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop)) {
                return List.of();
            }
            com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
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
            if (!PcurveSamplingHelper.sameUv(loopPoints.get(0), loopPoints.get(loopPoints.size() - 1))) {
                loopPoints.add(loopPoints.get(0));
            }
            loops.add(new ParametricLoopPayload(bound.outer() || promoteSingleOuter, loopPoints));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> extractLoopUvPoints(Loop loop, ParametricSurfaceMapper mapper, SurfaceGeometry surface) {
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
            if (uvPoints.size() > 1 && PcurveSamplingHelper.sameUv(uvPoints.get(0), uvPoints.get(uvPoints.size() - 1))) {
                uvPoints.remove(uvPoints.size() - 1);
            }
            return uvPoints;
        }
        return List.of();
    }

    private static List<UvPoint> sampleSemanticOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
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

    /**
     * One step of the surface-descent chain, shared by
     * {@link #mapPointIntoFaceGeometry} and
     * {@link #acceptablePcurveBasisSurfaceIds}. Each rule matches a wrapper
     * surface type and returns the surface it wraps; a SURFACE_REPLICA only
     * descends when its entityName is exactly "SURFACE_REPLICA". An entity no
     * rule matches (or a non-matching replica) returns null, which the callers
     * read as "stop descending". Order mirrors the original if/else-if chains
     * (first match wins); all 5 types are final direct StepEntity
     * implementations, so the order is behaviour neutral today but frozen by
     * surface-unwrap-dispatch-order.txt.
     */
    @FunctionalInterface
    private interface SurfaceUnwrapHandler {
        StepEntity next(StepEntity surface);
    }

    private record SurfaceUnwrapRule(
            Class<? extends StepEntity> type,
            Predicate<StepEntity> guard,
            SurfaceUnwrapHandler handler) {
        boolean matches(StepEntity surface) {
            return type.isInstance(surface) && (guard == null || guard.test(surface));
        }
    }

    private static SurfaceUnwrapRule surfaceUnwrapRule(
            Class<? extends StepEntity> type, SurfaceUnwrapHandler handler) {
        return new SurfaceUnwrapRule(type, null, handler);
    }

    private static final List<SurfaceUnwrapRule> SURFACE_UNWRAP_RULES = List.of(
            surfaceUnwrapRule(com.minicad.step.model.StepRectangularTrimmedSurface.class,
                    (surface) -> ((com.minicad.step.model.StepRectangularTrimmedSurface) surface).basisSurface()),
            surfaceUnwrapRule(com.minicad.step.model.StepCurveBoundedSurface.class,
                    (surface) -> ((com.minicad.step.model.StepCurveBoundedSurface) surface).basisSurface()),
            surfaceUnwrapRule(com.minicad.step.model.StepOrientedSurface.class,
                    (surface) -> ((com.minicad.step.model.StepOrientedSurface) surface).surfaceElement()),
            surfaceUnwrapRule(com.minicad.step.model.StepOffsetSurface.class,
                    (surface) -> ((com.minicad.step.model.StepOffsetSurface) surface).basisSurface()),
            new SurfaceUnwrapRule(com.minicad.step.model.StepGeometricReplica.class,
                    MeshTriangulatorParametric::isSurfaceReplica,
                    (surface) -> ((com.minicad.step.model.StepGeometricReplica) surface).parent())
    );

    private static boolean isSurfaceReplica(StepEntity surface) {
        return surface instanceof com.minicad.step.model.StepGeometricReplica replica
                && "SURFACE_REPLICA".equals(replica.entityName());
    }

    private static StepEntity unwrapSurfaceOnce(StepEntity surface) {
        for (SurfaceUnwrapRule rule : SURFACE_UNWRAP_RULES) {
            if (rule.matches(surface)) {
                return rule.handler().next(surface);
            }
        }
        return null;
    }

    private static CartesianPoint mapPointIntoFaceGeometry(
            CartesianPoint point,
            StepEntity faceGeometry,
            StepCadBuilder builder
    ) {
        StepEntity current = faceGeometry;
        CartesianPoint mapped = point;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            StepEntity next = unwrapSurfaceOnce(current);
            if (isSurfaceReplica(current)) {
                com.minicad.step.model.StepGeometricReplica replica =
                        (com.minicad.step.model.StepGeometricReplica) current;
                mapped = StepMeshExporter.transformPoint3(mapped, replica.transformation(), builder);
            }
            if (next == null) {
                break;
            }
            current = next;
        }
        return mapped;
    }

    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        StepEntity current = edgeGeometry;
        for (int depth = 0; depth < 16; depth++) {
            if (current instanceof com.minicad.step.model.StepOrientedCurve) {
                com.minicad.step.model.StepOrientedCurve orientedCurve = (com.minicad.step.model.StepOrientedCurve) current;
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof com.minicad.step.model.StepGeometricReplica) {
                com.minicad.step.model.StepGeometricReplica replica = (com.minicad.step.model.StepGeometricReplica) current;
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
        if (edgeGeometry instanceof com.minicad.step.model.StepSurfaceCurve) {
            com.minicad.step.model.StepSurfaceCurve surfaceCurve = (com.minicad.step.model.StepSurfaceCurve) edgeGeometry;
            return surfaceCurve.associatedGeometry();
        }
        if (edgeGeometry instanceof com.minicad.step.model.StepSeamCurve) {
            com.minicad.step.model.StepSeamCurve seamCurve = (com.minicad.step.model.StepSeamCurve) edgeGeometry;
            return seamCurve.associatedGeometry();
        }
        return List.of();
    }

    private static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof com.minicad.step.model.StepPcurve) {
                com.minicad.step.model.StepPcurve pcurve = (com.minicad.step.model.StepPcurve) associated;
                if (acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                    matches.add(pcurve);
                }
            } else if (associated instanceof com.minicad.step.model.StepDegeneratePcurve) {
                com.minicad.step.model.StepDegeneratePcurve pcurve = (com.minicad.step.model.StepDegeneratePcurve) associated;
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
            StepEntity next = unwrapSurfaceOnce(current);
            if (next == null) {
                break;
            }
            current = next;
        }
        return Set.copyOf(ids);
    }

    private static CartesianPoint pointFromStep(com.minicad.step.model.StepCartesianPoint point) {
        return new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                point.coordinates().get(2)
        );
    }

    private static List<UvPoint> extractEdgeUvPoints(OrientedEdge orientedEdge, ParametricSurfaceMapper mapper, SurfaceGeometry surface) {
        List<UvPoint> pcurvePoints = extractSurfaceCurveUvPoints(orientedEdge, mapper, surface);
        if (!pcurvePoints.isEmpty()) {
            return pcurvePoints;
        }
        List<CartesianPoint> points3d = MeshSampleOrientationHelper.orientSamples(
                orientedEdge, orientedEdge.edge().curve().sample(DEFAULT_CURVE_SEGMENTS));
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

    private static List<UvPoint> extractSurfaceCurveUvPoints(OrientedEdge orientedEdge, ParametricSurfaceMapper mapper, SurfaceGeometry surface) {
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

    private static List<UvPoint> orientUvSamples(OrientedEdge orientedEdge, List<UvPoint> samples, ParametricSurfaceMapper mapper) {
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

    private static UvPoint alignToReference(UvPoint point, UvPoint reference, ParametricSurfaceMapper mapper) {
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

    /**
     * Curve2 sampling rules keyed by concrete type, replacing the former
     * 5-branch if/else-if chain (first match wins, mirrors the original
     * sequential ifs). Every branch casts the curve to its concrete type and
     * delegates to the matching sampler, so a type wired to the wrong handler
     * throws ClassCastException rather than compiling silently. All 5 types are
     * final direct Curve2 implementations (no subtype relation today), so the
     * order is behaviour neutral but frozen by
     * mesh-sample-curve2-dispatch-order.txt. A curve matching no rule returns
     * an empty list, as the old trailing statement did. This is a separate
     * table from StepCadGeometryOps.sampleCurve2 on purpose -- different
     * component, different sampler set.
     */
    @FunctionalInterface
    private interface MeshCurve2SampleHandler {
        List<UvPoint> sample(Curve2 curve, UvPoint start, UvPoint end);
    }

    private record MeshCurve2SampleRule(
            Class<? extends Curve2> type, MeshCurve2SampleHandler handler) {
        boolean matches(Curve2 curve) {
            return type.isInstance(curve);
        }
    }

    private static MeshCurve2SampleRule meshCurve2SampleRule(
            Class<? extends Curve2> type, MeshCurve2SampleHandler handler) {
        return new MeshCurve2SampleRule(type, handler);
    }

    private static final List<MeshCurve2SampleRule> MESH_SAMPLE_CURVE2_RULES = List.of(
            meshCurve2SampleRule(Line2.class, (curve, start, end) ->
                    PcurveSamplingHelper.sampleLinePcurve((Line2) curve, start, end)),
            meshCurve2SampleRule(Circle2.class, (curve, start, end) ->
                    PcurveSamplingHelper.sampleCirclePcurve((Circle2) curve, start, end)),
            meshCurve2SampleRule(Ellipse2.class, (curve, start, end) ->
                    PcurveSamplingHelper.sampleEllipsePcurve((Ellipse2) curve, start, end)),
            meshCurve2SampleRule(BSplineCurve2.class, (curve, start, end) ->
                    PcurveSamplingHelper.sampleSplinePcurve((BSplineCurve2) curve, start, end)),
            meshCurve2SampleRule(TrimmedCurve2.class, (curve, start, end) ->
                    sampleTrimmedPcurve((TrimmedCurve2) curve, start, end))
    );

    private static List<UvPoint> sampleCurve2(Curve2 curve, UvPoint start, UvPoint end) {
        for (MeshCurve2SampleRule rule : MESH_SAMPLE_CURVE2_RULES) {
            if (rule.matches(curve)) {
                return rule.handler().sample(curve, start, end);
            }
        }
        return List.of();
    }

    // The line / circle / ellipse / spline samplers used to be private copies of
    // PcurveSamplingHelper's; they were identical line for line, so the table now
    // delegates to that helper (see the rules above) and the copies are gone.
    //
    // sampleTrimmedPcurve, score, alignTrimmedSamples and uvDistance deliberately
    // stay local: this file's UV distance is null guarded (a null endpoint scores
    // as +inf) while PcurveSamplingHelper.distanceSquared is not, so routing the
    // trimmed path through the helper would drop that guard. Converging them means
    // either weakening the guard or tightening the shared helper -- both are
    // behaviour changes, so the pair stays split on purpose.

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

    private static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
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

    static List<ParametricLoopPayload> normalizeLoopPeriods(List<ParametricLoopPayload> loops, ParametricSurfaceMapper mapper) {
        if (loops.isEmpty()) {
            return loops;
        }
        ParametricLoopPayload outer = loops.stream().filter(ParametricLoopPayload::outer).findFirst().orElse(null);
        if (outer == null) {
            return loops;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        if (uPeriod == null && vPeriod == null) {
            return loops;
        }
        UvPoint outerCenter = centroidUv(outer.points());
        List<ParametricLoopPayload> normalized = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
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
            normalized.add(new ParametricLoopPayload(false, shifted));
        }
        return List.copyOf(normalized);
    }

    private static UvPoint centroidUv(List<UvPoint> points) {
        if (points.isEmpty()) {
            return new UvPoint(0.0, 0.0);
        }
        int count = points.size();
        if (count > 1 && PcurveSamplingHelper.sameUv(points.get(0), points.get(points.size() - 1))) {
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
