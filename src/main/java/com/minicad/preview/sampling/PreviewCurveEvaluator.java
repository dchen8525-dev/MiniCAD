package com.minicad.preview.sampling;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeWire;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityNamingUtils;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.PointPayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.minicad.export.json.StepTypeNameResolver;

/** Curve evaluation, sampling, and preview payload construction.
 *  Extracted from StepPreviewJsonExporter to isolate curve logic. */
public final class PreviewCurveEvaluator {

    private PreviewCurveEvaluator() {}

    // ─── CurveEvaluator interface ────────────────────────────────────────

    public interface CurveEvaluator {
        double start();

        double end();

        CartesianPoint pointAt(double parameter);

        default Vector3 tangentAt(double parameter) {
            double span = Math.max(end() - start(), 1.0);
            double step = Math.max(span * 1.0e-4, 1.0e-5);
            double t0 = Math.max(start(), parameter - step);
            double t1 = Math.min(end(), parameter + step);
            if (t1 - t0 <= Epsilon.EPS) {
                t0 = Math.max(start(), parameter - step * 2.0);
                t1 = Math.min(end(), parameter + step * 2.0);
            }
            return pointAt(t1).subtract(pointAt(t0));
        }

        default List<CartesianPoint> sample(int segments) {
            List<CartesianPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                double parameter = start() + (end() - start()) * index / (double) segments;
                points.add(pointAt(parameter));
            }
            return List.copyOf(points);
        }
    }

    // ─── CurveEvaluator factory ──────────────────────────────────────────

    @FunctionalInterface
    private interface CurveEvaluatorFactory {
        CurveEvaluator evaluate(StepEntity curve, StepCadBuilder builder);
    }

    private record EvaluatorRule(Class<?> type, CurveEvaluatorFactory factory) {
        boolean matches(StepEntity curve) {
            return type.isInstance(curve);
        }
    }

    private static EvaluatorRule rule(Class<?> type, CurveEvaluatorFactory factory) {
        return new EvaluatorRule(type, factory);
    }

    /** Wrapper curves whose evaluator is the evaluator of a referenced curve. */
    private static EvaluatorRule wrapperRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return rule(type, (curve, builder) -> curveEvaluator(next.apply(curve), builder));
    }

    /** Curve families materialized through builder.buildCurveReference3. */
    private static EvaluatorRule curveReferenceRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildCurveReference3(curve.id())));
    }

    /** 2D curve families lifted to 3D through builder.buildCurve3From2D. */
    private static EvaluatorRule curve2DRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildCurve3From2D(curve.id())));
    }

    /** Path-family curves materialized through builder.buildPath. */
    private static EvaluatorRule pathRule(Class<?> type) {
        return rule(type, (curve, builder) -> sampledCurveEvaluator(builder.buildPath(curve.id())));
    }

    /**
     * Curve-evaluator factories keyed by concrete type, replacing the former
     * 59-branch if/else-if chain. Order mirrors the original chain (first
     * match wins) so subtype overlaps keep their old resolution.
     */
    private static final List<EvaluatorRule> CURVE_EVALUATOR_RULES = List.of(
            rule(StepLine.class, (curve, builder) -> {
                StepLine line = (StepLine) curve;
                Line3 geometry = builder.buildLine(line.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return -1.0; }
                    @Override
                    public double end() { return 1.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepCircle.class, (curve, builder) -> {
                StepCircle circle = (StepCircle) curve;
                Circle geometry = builder.buildCircle(circle.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepEllipse.class, (curve, builder) -> {
                StepEllipse ellipse = (StepEllipse) curve;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            rule(StepBSplineCurveWithKnots.class, (curve, builder) -> {
                StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) curve;
                BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
                return new CurveEvaluator() {
                    @Override
                    public double start() { return geometry.startParameter(); }
                    @Override
                    public double end() { return geometry.endParameter(); }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }),
            wrapperRule(StepTrimmedCurve.class, curve -> ((StepTrimmedCurve) curve).basisCurve()),
            wrapperRule(StepSurfaceCurve.class, curve -> ((StepSurfaceCurve) curve).curve3d()),
            rule(StepRationalBSplineCurve.class, (curve, builder) -> {
                StepRationalBSplineCurve spline = (StepRationalBSplineCurve) curve;
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
                return new CurveEvaluator() {
                    @Override public double start() { return geometry.startParameter(); }
                    @Override public double end() { return geometry.endParameter(); }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }),
            rule(StepPolyline.class, (curve, builder) -> {
                StepPolyline polyline = (StepPolyline) curve;
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return new CurveEvaluator() {
                    @Override public double start() { return 0.0; }
                    @Override public double end() { return 1.0; }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }),
            rule(StepCompositeCurve.class, (curve, builder) -> {
                StepCompositeCurve compositeCurve = (StepCompositeCurve) curve;
                CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
                return sampledCurveEvaluator(geometry);
            }),
            curveReferenceRule(StepBezierCurve.class),
            curveReferenceRule(StepUniformCurve.class),
            curveReferenceRule(StepQuasiUniformCurve.class),
            curveReferenceRule(StepPiecewiseBezierCurve.class),
            rule(StepOffsetCurve3D.class, (curve, builder) -> {
                StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) curve;
                return sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
            }),
            rule(StepConicCurve.class, (curve, builder) -> {
                StepConicCurve conic = (StepConicCurve) curve;
                List<CartesianPoint> points = sampleConicCurvePoints(conic, builder);
                if (points == null || points.size() < 2) return null;
                return sampledCurveEvaluator(new Polyline3(points));
            }),
            wrapperRule(StepOrientedCurve.class, curve -> ((StepOrientedCurve) curve).curveElement()),
            wrapperRule(StepGeometricReplica.class, curve -> ((StepGeometricReplica) curve).parent()),
            curveReferenceRule(StepBSplineCurve.class),
            rule(StepSeamCurve.class, (curve, builder) -> {
                StepSeamCurve seamCurve = (StepSeamCurve) curve;
                return sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
            }),
            curve2DRule(StepCircle2D.class),
            curve2DRule(StepEllipse2D.class),
            curve2DRule(StepPolyline2D.class),
            curve2DRule(StepTrimmedCurve2D.class),
            curve2DRule(StepCompositeCurve2D.class),
            curve2DRule(StepBezierCurve2D.class),
            curve2DRule(StepQuasiUniformCurve2D.class),
            curve2DRule(StepUniformCurve2D.class),
            curve2DRule(StepPiecewiseBezierCurve2D.class),
            curve2DRule(StepIndexedPolyCurve2D.class),
            curve2DRule(StepDegenerateCurve2D.class),
            curve2DRule(StepBSplineCurve2D.class),
            curve2DRule(StepRationalBSplineCurve2D.class),
            curve2DRule(StepLine2D.class),
            curve2DRule(StepCurve2D.class),
            curve2DRule(StepHyperbola2D.class),
            curve2DRule(StepParabola2D.class),
            curve2DRule(StepOffsetCurve2D.class),
            curveReferenceRule(StepClothoid.class),
            curveReferenceRule(StepIndexedPolyCurve.class),
            curveReferenceRule(StepDegenerateCurve.class),
            rule(StepBSplineCurveWithKnotsAndBreakpoints.class, (curve, builder) -> {
                StepBSplineCurveWithKnotsAndBreakpoints splineBreak = (StepBSplineCurveWithKnotsAndBreakpoints) curve;
                return sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
            }),
            curveReferenceRule(StepCompositeCurveOnSurface.class),
            curveReferenceRule(StepCompositeCurveOnSurface3D.class),
            rule(StepLineSegment.class, (curve, builder) -> {
                StepLineSegment lineSeg = (StepLineSegment) curve;
                List<CartesianPoint> pts = List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                );
                return sampledCurveEvaluator(new Polyline3(pts));
            }),
            pathRule(StepPath.class),
            pathRule(StepOpenPath.class),
            pathRule(StepSubpath.class),
            pathRule(StepOrientedPath.class),
            curveReferenceRule(StepEdgeCurve.class),
            curveReferenceRule(StepSurfacedEdgeCurve.class),
            wrapperRule(StepAnnotationCurveOccurrence.class, curve -> ((StepAnnotationCurveOccurrence) curve).item()),
            wrapperRule(StepDimensionCurve.class, curve -> ((StepDimensionCurve) curve).item()),
            wrapperRule(StepLeaderCurve.class, curve -> ((StepLeaderCurve) curve).item()),
            wrapperRule(StepProjectionCurve.class, curve -> ((StepProjectionCurve) curve).item()),
            wrapperRule(StepDraughtingAnnotationOccurrence.class, curve -> ((StepDraughtingAnnotationOccurrence) curve).item()),
            wrapperRule(StepTerminatorSymbol.class, curve -> ((StepTerminatorSymbol) curve).annotatedCurve()),
            curveReferenceRule(StepCurve.class),
            curveReferenceRule(StepBoundedCurve.class),
            wrapperRule(StepMappedItem.class, curve -> ((StepMappedItem) curve).mappingTarget())
    );

    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        for (EvaluatorRule candidate : CURVE_EVALUATOR_RULES) {
            if (candidate.matches(curve)) {
                return candidate.factory().evaluate(curve, builder);
            }
        }
        return null;
    }

    // ─── Sampled curve evaluator ─────────────────────────────────────────

    public static CurveEvaluator sampledCurveEvaluator(Curve3 curve) {
        List<CartesianPoint> points = curve.sample(128);
        if (points.size() < 2) return null;
        return new CurveEvaluator() {
            @Override public double start() { return 0.0; }
            @Override public double end() { return 1.0; }
            @Override
            public CartesianPoint pointAt(double parameter) {
                double t = Math.max(0, Math.min(1, parameter));
                double idx = t * (points.size() - 1);
                int i0 = (int) idx;
                int i1 = Math.min(i0 + 1, points.size() - 1);
                double f = idx - i0;
                CartesianPoint p0 = points.get(i0);
                CartesianPoint p1 = points.get(i1);
                return new CartesianPoint(
                        p0.x() + (p1.x() - p0.x()) * f,
                        p0.y() + (p1.y() - p0.y()) * f,
                        p0.z() + (p1.z() - p0.z()) * f
                );
            }
        };
    }

    // ─── Closest parameter ───────────────────────────────────────────────

    public static double closestParameter(CurveEvaluator curve, CartesianPoint point, Double preferred) {
        int coarseSegments = 160;
        double start = curve.start();
        double end = curve.end();
        double bestParameter = start;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index <= coarseSegments; index++) {
            double parameter = start + (end - start) * index / coarseSegments;
            double distance = curve.pointAt(parameter).distanceTo(point);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        if (preferred != null && preferred >= start && preferred <= end) {
            double preferredDistance = curve.pointAt(preferred).distanceTo(point);
            if (preferredDistance <= bestDistance * 1.25) {
                bestDistance = preferredDistance;
                bestParameter = preferred;
            }
        }
        double window = Math.max((end - start) / coarseSegments, 1.0e-6);
        for (int refinement = 0; refinement < 5; refinement++) {
            double min = Math.max(start, bestParameter - window);
            double max = Math.min(end, bestParameter + window);
            for (int index = 0; index <= 12; index++) {
                double parameter = min + (max - min) * index / 12.0;
                double distance = curve.pointAt(parameter).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestParameter = parameter;
                }
            }
            window *= 0.35;
        }
        return bestParameter;
    }

    // ─── Revolution helpers ──────────────────────────────────────────────

    public static Vector3 radialComponent(CartesianPoint point, CartesianPoint axisOrigin, Direction3 axisDirection) {
        Vector3 offset = point.subtract(axisOrigin);
        return offset.subtract(axisDirection.asVector().scale(offset.dot(axisDirection.asVector())));
    }

    public static Vector3 fallbackNormal(Vector3 preferredAxis) {
        Vector3 seed = Math.abs(preferredAxis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 1.0, 0.0);
        Vector3 normal = preferredAxis.cross(seed);
        if (normal.norm() <= Epsilon.EPS) {
            normal = preferredAxis.cross(new Vector3(0.0, 0.0, 1.0));
        }
        return normal.norm() <= Epsilon.EPS ? new Vector3(0.0, 0.0, 1.0) : normal;
    }

    public static double unwrapPeriodic(double value, Double previous, double period) {
        if (previous == null) {
            return value;
        }
        while (value - previous > period * 0.5) {
            value -= period;
        }
        while (value - previous < -period * 0.5) {
            value += period;
        }
        return value;
    }

    // ─── Loose curve sampling (3D) ───────────────────────────────────────

    public static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        return Curve3SamplingHelper.sampleLooseCurve(curve);
    }

    // ─── Loose curve sampling (2D) ───────────────────────────────────────

    public static Curve3 liftCurve2(Curve2 curve) {
        List<Point2> points2 = sampleLooseCurve2(curve);
        List<CartesianPoint> points3 = new ArrayList<>(points2.size());
        for (Point2 point : points2) {
            points3.add(new CartesianPoint(point.x(), point.y(), 0.0));
        }
        return new Polyline3(List.copyOf(points3));
    }

    public static List<Point2> sampleLooseCurve2(Curve2 curve) {
        return Curve2SamplingHelper.sampleLooseCurve2(curve);
    }

    public static List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleLooseCurve2(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).subtract(sampled.get(sampled.size() - 1)).norm() <= 1.0e-9;
        List<Point2> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimEnd());

        List<Point2> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint2(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    // ─── 2D conic helpers ────────────────────────────────────────────────

    public static List<Point2> sampleCircle2Points(Circle2 circle, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    public static List<Point2> sampleEllipse2Points(Ellipse2 ellipse, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    // ─── Curve type names ────────────────────────────────────────────────

    public static String curveTypeName(Curve3 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    public static String curveTypeName(Curve2 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    // ─── Conic curve sampling ────────────────────────────────────────────

    public static List<CartesianPoint> sampleConicCurvePoints(StepConicCurve curve, StepCadBuilder builder) {
        double[] matrix = MatrixTransformHelper.matrixForPlacementEntity(curve.position(), builder);
        if (matrix == null) {
            return null;
        }
        String entityName = curve.entityName();
        if (entityName.equals("CIRCLE")) {
            return sampleConicCirclePoints(curve, matrix);
        } else if (entityName.equals("ELLIPSE")) {
            return sampleConicEllipsePoints(curve, matrix);
        } else if (entityName.equals("PARABOLA")) {
            return sampleParabolaPoints(curve, matrix);
        } else if (entityName.equals("HYPERBOLA")) {
            return sampleHyperbolaPoints(curve, matrix);
        } else if (entityName.equals("DEGENERATE_CONIC")) {
            CartesianPoint point = MatrixTransformHelper.transformCartesian(new CartesianPoint(0.0, 0.0, 0.0), matrix);
            return List.of(point, point);
        } else {
            return null;
        }
    }

    private static List<CartesianPoint> sampleConicCirclePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) return null;
        double radius = curve.parameters().get(0);
        if (!Double.isFinite(radius) || radius <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, radius, radius, 72);
    }

    private static List<CartesianPoint> sampleConicEllipsePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) return null;
        double semiMajor = curve.parameters().get(0);
        double semiMinor = curve.parameters().get(1);
        if (!Double.isFinite(semiMajor) || !Double.isFinite(semiMinor)) return null;
        if (semiMajor <= Epsilon.EPS || semiMinor <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, semiMajor, semiMinor, 72);
    }

    private static List<CartesianPoint> sampleConicPointsInMatrix(double[] matrix, double rx, double ry, int segments) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            CartesianPoint local = new CartesianPoint(rx * Math.cos(angle), ry * Math.sin(angle), 0.0);
            points.add(MatrixTransformHelper.transformCartesian(local, matrix));
        }
        return List.copyOf(points);
    }

    private static List<CartesianPoint> sampleParabolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) return null;
        double focalDistance = curve.parameters().get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) return null;
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -yExtent + (2.0 * yExtent * index) / segments;
            double x = (t * t) / (4.0 * focalDistance);
            points.add(MatrixTransformHelper.transformCartesian(new CartesianPoint(x, t, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    private static List<CartesianPoint> sampleHyperbolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) return null;
        double semiAxis = curve.parameters().get(0);
        double semiImaginaryAxis = curve.parameters().get(1);
        if (!Double.isFinite(semiAxis) || !Double.isFinite(semiImaginaryAxis)
                || semiAxis <= Epsilon.EPS || semiImaginaryAxis <= Epsilon.EPS) return null;
        double extent = 1.75;
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(MatrixTransformHelper.transformCartesian(new CartesianPoint(x, y, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    // ─── Edge sampling ───────────────────────────────────────────────────

    public static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(sampleTrimmedCurve3(trimmedCurve, 72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 splineCurve = (BSplineCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 splineCurve = (RationalBSplineCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            return sampleCircleArc(circle, start, end, naturalForward);
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            return sampleEllipseArc(ellipse, start, end, naturalForward);
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>();
            boolean firstSegment = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleEdge(start, end, segment, naturalForward);
                int startIndex = firstSegment ? 0 : 1;
                for (int i = startIndex; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                firstSegment = false;
            }
            if (!points.isEmpty()) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
            List<CartesianPoint> points = new ArrayList<>(parabola.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            List<CartesianPoint> points = new ArrayList<>(hyperbola.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
            List<CartesianPoint> points = new ArrayList<>(clothoid.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            return List.of(start, end);
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, PARABOLA, HYPERBOLA, CLOTHOID, POLYLINE, COMPOSITE_CURVE, B_SPLINE, RATIONAL_B_SPLINE_CURVE, OFFSET_CURVE_2D/3D, SURFACE_CURVE, SEAM_CURVE, DEGENERATE_CURVE or TRIMMED_CURVE topology");
    }

    public static List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleLooseCurve(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) <= 1.0e-9;
        List<CartesianPoint> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex(basisPoints, trimmedCurve.trimEnd());

        List<CartesianPoint> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    // ─── Point utilities (3D) ────────────────────────────────────────────

    public static int nearestPointIndex(List<CartesianPoint> points, CartesianPoint target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).distanceTo(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    public static void appendClosedTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    public static void appendOpenTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    public static void addDistinctPoint(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    // ─── Arc sampling ────────────────────────────────────────────────────

    public static List<CartesianPoint> sampleCircleArc(Circle circle, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        CartesianPoint projectedStart = circle.contains(start) ? start : circle.closestPointTo(start);
        CartesianPoint projectedEnd = circle.contains(end) ? end : circle.closestPointTo(end);
        double startAngle = circle.angleOf(projectedStart);
        double endAngle = circle.angleOf(projectedEnd);
        double delta = endAngle - startAngle;
        if (projectedStart.distanceTo(projectedEnd) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) delta += Math.PI * 2.0;
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(64, (int) Math.ceil(Math.abs(delta) / (Math.PI / 72.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(circle.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    public static List<CartesianPoint> sampleEllipseArc(Ellipse3 ellipse, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        double startAngle = ellipse.angleOf(start);
        double endAngle = ellipse.angleOf(end);
        double delta = endAngle - startAngle;
        if (start.distanceTo(end) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) delta += Math.PI * 2.0;
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(72, (int) Math.ceil(Math.abs(delta) / (Math.PI / 96.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(ellipse.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    // ─── Point utilities (2D) ────────────────────────────────────────────

    public static int nearestPointIndex2(List<Point2> points, Point2 target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).subtract(target).norm();
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    public static void appendClosedTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    public static void appendOpenTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint2(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    public static void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    // ─── Arc sweep ───────────────────────────────────────────────────────

    public static double arcSweep(double startAngle, double endAngle, boolean closed, boolean naturalForward) {
        double delta = endAngle - startAngle;
        if (closed) {
            return naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        }
        if (naturalForward) {
            return delta < 0.0 ? delta + Math.PI * 2.0 : delta;
        }
        return delta > 0.0 ? delta - Math.PI * 2.0 : delta;
    }

    // ─── Edge curve payload helpers ──────────────────────────────────────

    public static EdgeCurvePayload edgeCurvePayload(
            StepEntity edgeGeometry,
            CartesianPoint start,
            CartesianPoint end,
            boolean naturalForward,
            StepCadBuilder builder
    ) {
        try {
            if (edgeGeometry instanceof StepLine) {
                return sampledCurvePayload(edgeGeometry, builder);
            }
            if (edgeGeometry instanceof StepCircle) {
            StepCircle circle = (StepCircle) edgeGeometry;
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(), "circle_arc", null, null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(), null, null, null, null, null, null, null, null, null, null, null, null, null,
                        startAngle, arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward));
            }
            if (edgeGeometry instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) edgeGeometry;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(), "ellipse_arc", null, null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null, geometry.semiAxis1(), geometry.semiAxis2(), null, null, null, null, null, null, null, null, null, null, null,
                        startAngle, arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward));
            }
            if (edgeGeometry instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots bspline = (StepBSplineCurveWithKnots) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepBSplineCurve) {
            StepBSplineCurve bspline = (StepBSplineCurve) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve rational = (StepRationalBSplineCurve) edgeGeometry;
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve3(rational.id());
                return newRationalBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) edgeGeometry;
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return newPolylineCurvePayload(edgeGeometry.id(), geometry, start, end);
            }
            if (edgeGeometry instanceof StepLine) {
            StepLine line = (StepLine) edgeGeometry;
                return newLineCurvePayload(edgeGeometry.id(), builder, line, start, end);
            }
            EdgeCurvePayload generic = sampledCurvePayload(edgeGeometry, builder);
            if (generic != null) return generic;
        } catch (com.minicad.common.GeometryException | com.minicad.common.TopologyException ex) {
            // logged in caller
        }
        return null;
    }

    public static EdgeCurvePayload newBSplineCurvePayload(int stepId, BSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    public static EdgeCurvePayload newRationalBSplineCurvePayload(int stepId, RationalBSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "rational_bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    public static EdgeCurvePayload newPolylineCurvePayload(int stepId, Polyline3 geometry, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "polyline", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    public static EdgeCurvePayload newLineCurvePayload(int stepId, StepCadBuilder builder, StepLine line, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "line", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    // ─── Sampled curve payload ───────────────────────────────────────────

    public static EdgePayload sampledCurveEdgePayload(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> points = sampleLooseEdgePoints(item, builder);
        if (points == null || points.size() < 2) {
            return null;
        }
        return new EdgePayload(item.id(), toPointPayloads(points), sampledCurvePayload(item, builder), null);
    }

    public static EdgeCurvePayload sampledCurvePayload(StepEntity item, StepCadBuilder builder) {
        String type = previewCurveTypeName(item);
        if (type == null) return null;
        String basisType = previewCurveBasisTypeName(item);
        Integer basisStepId = previewCurveBasisStepId(item);
        Boolean orientation = previewCurveOrientation(item);
        Boolean senseAgreement = previewCurveSenseAgreement(item);
        Double offsetDistance = previewCurveOffsetDistance(item);
        Boolean selfIntersect = previewCurveSelfIntersect(item);
        List<Double> refDirection = previewCurveRefDirection(item);
        Double transformScale = previewCurveTransformScale(item);
        String masterRepresentation = previewCurveMasterRepresentation(item);
        List<String> associatedSurfaceTypes = previewCurveAssociatedSurfaceTypes(item);
        List<Integer> associatedSurfaceStepIds = previewCurveAssociatedSurfaceStepIds(item);
        try {
            if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(), "CIRCLE", basisType, basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(), null, null, orientation, senseAgreement, offsetDistance, selfIntersect,
                        refDirection, transformScale, masterRepresentation, associatedSurfaceTypes,
                        associatedSurfaceStepIds, null, null, 0.0, Math.PI * 2.0);
            }
            if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(), "ELLIPSE", basisType, basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null, geometry.semiAxis1(), geometry.semiAxis2(), orientation, senseAgreement, offsetDistance,
                        selfIntersect, refDirection, transformScale, masterRepresentation, associatedSurfaceTypes,
                        associatedSurfaceStepIds, null, null, 0.0, Math.PI * 2.0);
            }
        } catch (com.minicad.common.GeometryException | com.minicad.common.StepResolutionException ex) {
            // logged in caller
        }
        return new EdgeCurvePayload(
                item.id(), type, basisType, basisStepId, null, null, null, null, null, null,
                orientation, senseAgreement, offsetDistance, selfIntersect, refDirection, transformScale,
                masterRepresentation, associatedSurfaceTypes, associatedSurfaceStepIds, null, null, 0.0, 0.0);
    }

    // ─── Preview curve type/name helpers ─────────────────────────────────

    private record TypeNameRule(
            Class<?> type,
            Predicate<StepEntity> guard,
            Function<StepEntity, String> name
    ) {
        boolean matches(StepEntity item) {
            return type.isInstance(item) && (guard == null || guard.test(item));
        }
    }

    private static TypeNameRule typeNameRule(Class<?> type, String name) {
        return new TypeNameRule(type, null, item -> name);
    }

    /**
     * Preview curve names keyed by concrete type, replacing the former
     * 62-branch if/else-if chain. Two dead duplicate branches
     * (RATIONAL_B_SPLINE_CURVE, COMPOSITE_CURVE_ON_SURFACE) that could never
     * be reached behind identical earlier rules are dropped.
     */
    private static final List<TypeNameRule> PREVIEW_CURVE_TYPE_NAME_RULES = List.of(
            typeNameRule(StepLine.class, "LINE"),
            typeNameRule(StepCircle.class, "CIRCLE"),
            typeNameRule(StepEllipse.class, "ELLIPSE"),
            new TypeNameRule(StepConicCurve.class, null, item -> ((StepConicCurve) item).entityName()),
            typeNameRule(StepBezierCurve.class, "BEZIER_CURVE"),
            typeNameRule(StepUniformCurve.class, "UNIFORM_CURVE"),
            typeNameRule(StepQuasiUniformCurve.class, "QUASI_UNIFORM_CURVE"),
            typeNameRule(StepPiecewiseBezierCurve.class, "PIECEWISE_BEZIER_CURVE"),
            typeNameRule(StepBSplineCurveWithKnots.class, "B_SPLINE_CURVE_WITH_KNOTS"),
            typeNameRule(StepRationalBSplineCurve.class, "RATIONAL_B_SPLINE_CURVE"),
            new TypeNameRule(StepSurfaceCurve.class, null, item -> ((StepSurfaceCurve) item).entityName()),
            typeNameRule(StepSeamCurve.class, "SEAM_CURVE"),
            typeNameRule(StepTrimmedCurve.class, "TRIMMED_CURVE"),
            typeNameRule(StepPolyline.class, "POLYLINE"),
            typeNameRule(StepCompositeCurve.class, "COMPOSITE_CURVE"),
            typeNameRule(StepCompositeCurveOnSurface.class, "COMPOSITE_CURVE_ON_SURFACE"),
            typeNameRule(StepOffsetCurve2D.class, "OFFSET_CURVE_2D"),
            typeNameRule(StepOffsetCurve3D.class, "OFFSET_CURVE_3D"),
            typeNameRule(StepPcurve.class, "PCURVE"),
            typeNameRule(StepDegeneratePcurve.class, "DEGENERATE_PCURVE"),
            typeNameRule(StepOrientedCurve.class, "ORIENTED_CURVE"),
            typeNameRule(StepAnnotationCurveOccurrence.class, "ANNOTATION_CURVE_OCCURRENCE"),
            typeNameRule(StepDimensionCurve.class, "DIMENSION_CURVE"),
            typeNameRule(StepLeaderCurve.class, "LEADER_CURVE"),
            typeNameRule(StepProjectionCurve.class, "PROJECTION_CURVE"),
            typeNameRule(StepDraughtingAnnotationOccurrence.class, "DRAUGHTING_ANNOTATION_OCCURRENCE"),
            typeNameRule(StepTerminatorSymbol.class, "TERMINATOR_SYMBOL"),
            new TypeNameRule(StepGeometricReplica.class,
                    item -> "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()),
                    item -> "CURVE_REPLICA"),
            typeNameRule(StepBSplineCurve.class, "B_SPLINE_CURVE"),
            typeNameRule(StepCompositeCurveOnSurface3D.class, "COMPOSITE_CURVE_ON_SURFACE_3D"),
            typeNameRule(StepClothoid.class, "CLOTHOID"),
            typeNameRule(StepIndexedPolyCurve.class, "INDEXED_POLY_CURVE"),
            typeNameRule(StepDegenerateCurve.class, "DEGENERATE_CURVE"),
            typeNameRule(StepBSplineCurveWithKnotsAndBreakpoints.class, "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS"),
            typeNameRule(StepLineSegment.class, "LINE_SEGMENT"),
            typeNameRule(StepEdgeCurve.class, "EDGE_CURVE"),
            typeNameRule(StepSurfacedEdgeCurve.class, "SURFACED_EDGE_CURVE"),
            typeNameRule(StepPath.class, "PATH"),
            typeNameRule(StepOpenPath.class, "OPEN_PATH"),
            typeNameRule(StepSubpath.class, "SUBPATH"),
            typeNameRule(StepOrientedPath.class, "ORIENTED_PATH"),
            typeNameRule(StepCurve.class, "CURVE"),
            typeNameRule(StepBoundedCurve.class, "BOUNDED_CURVE"),
            typeNameRule(StepCircle2D.class, "CIRCLE_2D"),
            typeNameRule(StepEllipse2D.class, "ELLIPSE_2D"),
            typeNameRule(StepPolyline2D.class, "POLYLINE_2D"),
            typeNameRule(StepTrimmedCurve2D.class, "TRIMMED_CURVE_2D"),
            typeNameRule(StepCompositeCurve2D.class, "COMPOSITE_CURVE_2D"),
            typeNameRule(StepBezierCurve2D.class, "BEZIER_CURVE_2D"),
            typeNameRule(StepQuasiUniformCurve2D.class, "QUASI_UNIFORM_CURVE_2D"),
            typeNameRule(StepUniformCurve2D.class, "UNIFORM_CURVE_2D"),
            typeNameRule(StepPiecewiseBezierCurve2D.class, "PIECEWISE_BEZIER_CURVE_2D"),
            typeNameRule(StepIndexedPolyCurve2D.class, "INDEXED_POLY_CURVE_2D"),
            typeNameRule(StepDegenerateCurve2D.class, "DEGENERATE_CURVE_2D"),
            typeNameRule(StepBSplineCurve2D.class, "B_SPLINE_CURVE_2D"),
            typeNameRule(StepRationalBSplineCurve2D.class, "RATIONAL_B_SPLINE_CURVE_2D"),
            typeNameRule(StepLine2D.class, "LINE_2D"),
            typeNameRule(StepCurve2D.class, "CURVE_2D"),
            typeNameRule(StepHyperbola2D.class, "HYPERBOLA_2D"),
            typeNameRule(StepParabola2D.class, "PARABOLA_2D")
    );

    public static String previewCurveTypeName(StepEntity item) {
        for (TypeNameRule rule : PREVIEW_CURVE_TYPE_NAME_RULES) {
            if (rule.matches(item)) {
                return rule.name().apply(item);
            }
        }
        return null;
    }

    public static String previewCurveBasisTypeName(StepEntity item) {
        if (item instanceof StepSurfaceCurve) return previewCurveTypeName(((StepSurfaceCurve) item).curve3d());
        if (item instanceof StepSeamCurve) return previewCurveTypeName(((StepSeamCurve) item).curve3d());
        if (item instanceof StepTrimmedCurve) return previewCurveTypeName(((StepTrimmedCurve) item).basisCurve());
        if (item instanceof StepOffsetCurve2D) return previewCurveTypeName(((StepOffsetCurve2D) item).basisCurve());
        if (item instanceof StepOffsetCurve3D) return previewCurveTypeName(((StepOffsetCurve3D) item).basisCurve());
        if (item instanceof StepOrientedCurve) return previewCurveTypeName(((StepOrientedCurve) item).curveElement());
        if (item instanceof StepAnnotationCurveOccurrence) return previewCurveTypeName(((StepAnnotationCurveOccurrence) item).item());
        if (item instanceof StepDimensionCurve) return previewCurveTypeName(((StepDimensionCurve) item).item());
        if (item instanceof StepLeaderCurve) return previewCurveTypeName(((StepLeaderCurve) item).item());
        if (item instanceof StepProjectionCurve) return previewCurveTypeName(((StepProjectionCurve) item).item());
        if (item instanceof StepDraughtingAnnotationOccurrence) return previewCurveTypeName(((StepDraughtingAnnotationOccurrence) item).item());
        if (item instanceof StepTerminatorSymbol) return previewCurveTypeName(((StepTerminatorSymbol) item).annotatedCurve());
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return previewCurveTypeName(replica.parent());
        }
        if (item instanceof StepTrimmedCurve2D) return previewCurveTypeName(((StepTrimmedCurve2D) item).basisCurve());
        return null;
    }

    public static Integer previewCurveBasisStepId(StepEntity item) {
        if (item instanceof StepSurfaceCurve) return ((StepSurfaceCurve) item).curve3d().id();
        if (item instanceof StepSeamCurve) return ((StepSeamCurve) item).curve3d().id();
        if (item instanceof StepTrimmedCurve) return ((StepTrimmedCurve) item).basisCurve().id();
        if (item instanceof StepOffsetCurve2D) return ((StepOffsetCurve2D) item).basisCurve().id();
        if (item instanceof StepOffsetCurve3D) return ((StepOffsetCurve3D) item).basisCurve().id();
        if (item instanceof StepOrientedCurve) return ((StepOrientedCurve) item).curveElement().id();
        if (item instanceof StepAnnotationCurveOccurrence) return ((StepAnnotationCurveOccurrence) item).item().id();
        if (item instanceof StepDimensionCurve) return ((StepDimensionCurve) item).item().id();
        if (item instanceof StepLeaderCurve) return ((StepLeaderCurve) item).item().id();
        if (item instanceof StepProjectionCurve) return ((StepProjectionCurve) item).item().id();
        if (item instanceof StepDraughtingAnnotationOccurrence) return ((StepDraughtingAnnotationOccurrence) item).item().id();
        if (item instanceof StepTerminatorSymbol) return ((StepTerminatorSymbol) item).annotatedCurve().id();
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.parent().id();
        }
        if (item instanceof StepTrimmedCurve2D) return ((StepTrimmedCurve2D) item).basisCurve().id();
        return null;
    }

    public static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve) return ((StepOrientedCurve) item).orientation();
        return null;
    }

    public static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve) return ((StepTrimmedCurve) item).senseAgreement();
        if (item instanceof StepTrimmedCurve2D) return ((StepTrimmedCurve2D) item).senseAgreement();
        return null;
    }

    public static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) return ((StepOffsetCurve2D) item).distance();
        if (item instanceof StepOffsetCurve3D) return ((StepOffsetCurve3D) item).distance();
        return null;
    }

    public static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) return ((StepOffsetCurve2D) item).selfIntersect();
        if (item instanceof StepOffsetCurve3D) return ((StepOffsetCurve3D) item).selfIntersect();
        if (item instanceof StepCompositeCurveOnSurface) return ((StepCompositeCurveOnSurface) item).selfIntersect();
        return null;
    }

    public static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D) return List.copyOf(((StepOffsetCurve3D) item).refDirection().directionRatios());
        return null;
    }

    public static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            return ((StepGeometricReplica) item).transformation().scale();
        }
        return null;
    }

    public static String previewCurveMasterRepresentation(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) return ((StepSurfaceCurve) semanticCurve).masterRepresentation();
        if (semanticCurve instanceof StepSeamCurve) return ((StepSeamCurve) semanticCurve).masterRepresentation();
        return null;
    }

    public static List<String> previewCurveAssociatedSurfaceTypes(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) return null;
        List<String> surfaceTypes = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceTypes.add(StepTypeNameResolver.surfaceTypeName(pcurve.basisSurface()));
            }
        }
        return surfaceTypes.isEmpty() ? null : List.copyOf(surfaceTypes);
    }

    public static List<Integer> previewCurveAssociatedSurfaceStepIds(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) return null;
        List<Integer> surfaceIds = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }

    private static List<StepEntity> previewCurveAssociatedGeometry(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) return ((StepSurfaceCurve) semanticCurve).associatedGeometry();
        if (semanticCurve instanceof StepSeamCurve) return ((StepSeamCurve) semanticCurve).associatedGeometry();
        return null;
    }

    public static StepEntity previewCurveSemanticItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepOrientedCurve) {
                current = ((StepOrientedCurve) current).curveElement(); continue; }
            if (current instanceof StepAnnotationCurveOccurrence) {
                current = ((StepAnnotationCurveOccurrence) current).item(); continue; }
            if (current instanceof StepDimensionCurve) {
                current = ((StepDimensionCurve) current).item(); continue; }
            if (current instanceof StepLeaderCurve) {
                current = ((StepLeaderCurve) current).item(); continue; }
            if (current instanceof StepProjectionCurve) {
                current = ((StepProjectionCurve) current).item(); continue; }
            if (current instanceof StepDraughtingAnnotationOccurrence) {
                current = ((StepDraughtingAnnotationOccurrence) current).item(); continue; }
            if (current instanceof StepTerminatorSymbol) {
                current = ((StepTerminatorSymbol) current).annotatedCurve();
                continue;
            }
            if (current instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                current = ((StepGeometricReplica) current).parent();
                continue;
            }
            return current;
        }
    }

    // ─── sampleLooseEdgePoints (copied from StepPreviewJsonExporter) ─────

    private static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        // This is a large method copied verbatim from StepPreviewJsonExporter.
        // Since it's private in the exporter and we cannot modify that file,
        // we include the full implementation here.
        if (item instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(), builder);
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(), builder);
        }
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(), builder);
        }
        if (item instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            if ("CURVE_REPLICA".equals(replica.entityName())) {
                List<CartesianPoint> parentPoints = sampleLooseEdgePoints(replica.parent(), builder);
                if (parentPoints == null) return null;
                List<CartesianPoint> transformed = new ArrayList<>(parentPoints.size());
                for (CartesianPoint point : parentPoints) {
                    transformed.add(transformPoint(point, replica.transformation(), builder));
                }
                return List.copyOf(transformed);
            }
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            List<CartesianPoint> points = sampleLooseEdgePoints(orientedCurve.curveElement(), builder);
            if (points == null) return null;
            if (orientedCurve.orientation()) return points;
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            return List.copyOf(reversed);
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) item;
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire) {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            return sampleGeometricCollectionPoints(edgeWire.edges(), builder);
        }
        Curve3 curve = curveForLooseEdge(item, builder);
        if (curve == null) return null;
        return sampleLooseCurve(curve);
    }

    @FunctionalInterface
    private interface LooseEdgeCurveFactory {
        Curve3 build(StepEntity item, StepCadBuilder builder);
    }

    private record LooseEdgeRule(Class<?> type, Predicate<StepEntity> guard, LooseEdgeCurveFactory factory) {
        boolean matches(StepEntity item) {
            return type.isInstance(item) && (guard == null || guard.test(item));
        }
    }

    private static LooseEdgeRule looseRule(Class<?> type, LooseEdgeCurveFactory factory) {
        return new LooseEdgeRule(type, null, factory);
    }

    /** Types materialized through builder.buildCurveReference3. */
    private static LooseEdgeRule looseRefRule(Class<?> type) {
        return looseRule(type, (item, builder) -> builder.buildCurveReference3(item.id()));
    }

    /** Wrapper curves resolved through the curve they reference. */
    private static LooseEdgeRule looseRecurseRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return looseRule(type, (item, builder) -> curveForLooseEdge(next.apply(item), builder));
    }

    /** Pcurve families whose builder result is a 2D curve lifted to 3D. */
    private static LooseEdgeRule loosePcurveRule(Class<?> type) {
        return looseRule(type, (item, builder) -> {
            Object built = builder.buildPcurve2(item.id());
            return built instanceof Curve2 ? liftCurve2((Curve2) built) : null;
        });
    }

    /** 2D curve families lifted to 3D through builder.buildCurve3From2D. */
    private static final List<Class<?>> LOOSE_CURVE_FROM_2D_TYPES = List.of(
            StepCompositeCurve2D.class,
            StepTrimmedCurve2D.class,
            StepBezierCurve2D.class,
            StepQuasiUniformCurve2D.class,
            StepUniformCurve2D.class,
            StepPiecewiseBezierCurve2D.class,
            StepIndexedPolyCurve2D.class,
            StepDegenerateCurve2D.class,
            StepBSplineCurve2D.class,
            StepRationalBSplineCurve2D.class,
            StepLine2D.class,
            StepCurve2D.class,
            StepHyperbola2D.class,
            StepParabola2D.class
    );

    /**
     * Loose-edge curve factories keyed by concrete type, replacing the former
     * 53-branch if/else-if chain. Order mirrors the original chain (first
     * match wins). The dead cast to StepBoundedCurve in the wireframe-model
     * branch (which always threw ClassCastException for entities that reach
     * it, since StepEdgeBasedWireframeModel is not a StepBoundedCurve) is
     * dropped; the branch keeps its intended sampling behavior.
     */
    private static final List<LooseEdgeRule> LOOSE_EDGE_RULES = List.of(
            looseRule(StepLine.class, (item, builder) -> builder.buildLine(item.id())),
            looseRule(StepCircle.class, (item, builder) -> builder.buildCircle(item.id())),
            looseRule(StepEllipse.class, (item, builder) -> builder.buildEllipse(item.id())),
            looseRule(StepConicCurve.class, (item, builder) -> {
                List<CartesianPoint> points = sampleConicCurvePoints((StepConicCurve) item, builder);
                return points == null ? null : new Polyline3(points);
            }),
            looseRefRule(StepBezierCurve.class),
            looseRefRule(StepUniformCurve.class),
            looseRefRule(StepQuasiUniformCurve.class),
            looseRefRule(StepPiecewiseBezierCurve.class),
            looseRule(StepBSplineCurveWithKnots.class, (item, builder) -> builder.buildBSplineCurve(item.id())),
            looseRule(StepSurfaceCurve.class, (item, builder) -> builder.buildSurfaceCurve(item.id())),
            looseRule(StepSeamCurve.class, (item, builder) -> builder.buildSeamCurve(item.id())),
            looseRule(StepTrimmedCurve.class, (item, builder) -> builder.buildTrimmedCurve(item.id())),
            looseRule(StepPolyline.class, (item, builder) -> builder.buildPolyline(item.id())),
            looseRule(StepCompositeCurve.class, (item, builder) -> builder.buildCompositeCurve(item.id())),
            looseRule(StepCompositeCurveOnSurface.class, (item, builder) -> builder.buildCompositeCurve(item.id())),
            looseRule(StepRationalBSplineCurve.class, (item, builder) -> builder.buildRationalBSplineCurve(item.id())),
            looseRule(StepOffsetCurve2D.class, (item, builder) -> liftCurve2(builder.buildOffsetCurve2(item.id()))),
            looseRule(StepOffsetCurve3D.class, (item, builder) -> builder.buildOffsetCurve3(item.id())),
            loosePcurveRule(StepPcurve.class),
            loosePcurveRule(StepDegeneratePcurve.class),
            looseRecurseRule(StepOrientedCurve.class, item -> ((StepOrientedCurve) item).curveElement()),
            looseRecurseRule(StepAnnotationCurveOccurrence.class, item -> ((StepAnnotationCurveOccurrence) item).item()),
            looseRecurseRule(StepDimensionCurve.class, item -> ((StepDimensionCurve) item).item()),
            looseRecurseRule(StepLeaderCurve.class, item -> ((StepLeaderCurve) item).item()),
            looseRecurseRule(StepProjectionCurve.class, item -> ((StepProjectionCurve) item).item()),
            looseRecurseRule(StepDraughtingAnnotationOccurrence.class, item -> ((StepDraughtingAnnotationOccurrence) item).item()),
            looseRecurseRule(StepTerminatorSymbol.class, item -> ((StepTerminatorSymbol) item).annotatedCurve()),
            new LooseEdgeRule(StepGeometricReplica.class,
                    item -> "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()),
                    (item, builder) -> curveForLooseEdge(((StepGeometricReplica) item).parent(), builder)),
            looseRule(StepPath.class, (item, builder) -> builder.buildPath(item.id())),
            looseRule(StepOpenPath.class, (item, builder) -> builder.buildPath(item.id())),
            looseRule(StepSubpath.class, (item, builder) -> builder.buildPath(item.id())),
            looseRule(StepOrientedPath.class, (item, builder) -> builder.buildPath(item.id())),
            new LooseEdgeRule(StepEntity.class,
                    item -> LOOSE_CURVE_FROM_2D_TYPES.stream().anyMatch(type -> type.isInstance(item)),
                    (item, builder) -> builder.buildCurve3From2D(item.id())),
            looseRefRule(StepEdgeCurve.class),
            looseRefRule(StepSurfacedEdgeCurve.class),
            looseRefRule(StepCurve.class),
            looseRefRule(StepBoundedCurve.class),
            looseRule(StepEdgeBasedWireframeModel.class, (item, builder) -> {
                List<CartesianPoint> sampled = sampleWireframeBoundaryPoints(((StepEdgeBasedWireframeModel) item).boundaries(), builder);
                return sampled == null || sampled.size() < 2 ? null : new Polyline3(sampled);
            })
    );

    private static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            for (LooseEdgeRule rule : LOOSE_EDGE_RULES) {
                if (rule.matches(item)) {
                    return rule.factory().build(item, builder);
                }
            }
        } catch (com.minicad.common.GeometryException | com.minicad.common.TopologyException | com.minicad.common.StepResolutionException ignored) {
            // fallthrough
        }
        return null;
    }

    private static List<CartesianPoint> sampleAnnotationFillAreaPoints(StepAnnotationFillArea fillArea, StepCadBuilder builder) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : fillArea.boundaries()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) continue;
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) points.add(sampled.get(i));
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleGeometricCollectionPoints(List<StepEntity> elements, StepCadBuilder builder) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity element : elements) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(element, builder);
            if (sampled != null && !sampled.isEmpty()) points.addAll(sampled);
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireShellPoints(StepWireShell wireShell, StepCadBuilder builder) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepLoop loop : wireShell.loops()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(loop, builder);
            if (sampled != null && !sampled.isEmpty()) points.addAll(sampled);
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireframeBoundaryPoints(List<? extends StepEntity> boundaries, StepCadBuilder builder) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : boundaries) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) continue;
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) points.add(sampled.get(i));
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleMappedAnnotationPoints(
            StepRepresentation representation, StepEntity mappedOrigin, StepEntity mappingTarget, StepCadBuilder builder) {
        double[] matrix = MatrixTransformHelper.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) return null;
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity content : representation.items()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
            if (sampled == null) continue;
            for (CartesianPoint point : sampled) points.add(MatrixTransformHelper.transformCartesian(point, matrix));
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static double[] matrixForMappedPlacement(StepEntity mappedOrigin, StepEntity mappingTarget, StepCadBuilder builder) {
        StepEntity originPlacement = null;
        StepEntity targetPlacement = null;
        if (mappedOrigin instanceof StepAxis2Placement3D || mappedOrigin instanceof StepAxis2Placement2D) {
            originPlacement = mappedOrigin;
        }
        if (mappingTarget instanceof StepAxis2Placement3D || mappingTarget instanceof StepAxis2Placement2D) {
            targetPlacement = mappingTarget;
        }
        if (mappedOrigin instanceof StepRepresentation) {
            StepRepresentation mappedRep = (StepRepresentation) mappedOrigin;
            // Check for placement-like items in the representation
            for (StepEntity repItem : mappedRep.items()) {
                if (repItem instanceof StepAxis2Placement3D) {
                    originPlacement = repItem;
                    break;
                }
            }
        }
        double[] originMatrix = originPlacement == null ? null : MatrixTransformHelper.matrixForPlacementEntity(originPlacement, builder);
        double[] targetMatrix = targetPlacement == null ? null : MatrixTransformHelper.matrixForPlacementEntity(targetPlacement, builder);
        if (originMatrix == null || targetMatrix == null) return null;
        return composeMatrices(invertMatrix(targetMatrix), originMatrix);
    }

    private static double[] invertMatrix(double[] m) {
        double[] inv = new double[16];
        inv[0] = m[0]; inv[1] = m[4]; inv[2] = m[8];
        inv[4] = m[1]; inv[5] = m[5]; inv[6] = m[9];
        inv[8] = m[2]; inv[9] = m[6]; inv[10] = m[10];
        Vector3 t = new Vector3(m[3], m[7], m[11]);
        Vector3 col0 = new Vector3(m[0], m[4], m[8]);
        Vector3 col1 = new Vector3(m[1], m[5], m[9]);
        Vector3 col2 = new Vector3(m[2], m[6], m[10]);
        double tx = -t.dot(col0); double ty = -t.dot(col1); double tz = -t.dot(col2);
        inv[3] = tx; inv[7] = ty; inv[11] = tz;
        inv[12] = 0.0; inv[13] = 0.0; inv[14] = 0.0; inv[15] = 1.0;
        return inv;
    }

    private static double[] composeMatrices(double[] a, double[] b) {
        double[] c = new double[16];
        c[0] = a[0]*b[0] + a[1]*b[4] + a[2]*b[8];
        c[1] = a[0]*b[1] + a[1]*b[5] + a[2]*b[9];
        c[2] = a[0]*b[2] + a[1]*b[6] + a[2]*b[10];
        c[3] = a[0]*b[3] + a[1]*b[7] + a[2]*b[11] + a[3];
        c[4] = a[4]*b[0] + a[5]*b[4] + a[6]*b[8];
        c[5] = a[4]*b[1] + a[5]*b[5] + a[6]*b[9];
        c[6] = a[4]*b[2] + a[5]*b[6] + a[6]*b[10];
        c[7] = a[4]*b[3] + a[5]*b[7] + a[6]*b[11] + a[7];
        c[8] = a[8]*b[0] + a[9]*b[4] + a[10]*b[8];
        c[9] = a[8]*b[1] + a[9]*b[5] + a[10]*b[9];
        c[10] = a[8]*b[2] + a[9]*b[6] + a[10]*b[10];
        c[11] = a[8]*b[3] + a[9]*b[7] + a[10]*b[11] + a[11];
        c[12] = 0.0; c[13] = 0.0; c[14] = 0.0; c[15] = 1.0;
        return c;
    }

    private static CartesianPoint transformPoint(
            CartesianPoint point,
            StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() == null) {
            Vector3 fallback = PreviewCurveEvaluator.fallbackNormal(axis1);
            axis2 = Direction3.from(fallback).asVector();
        } else {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        }
        Vector3 axis3 = axis1.cross(axis2).normalize().asVector();
        axis2 = axis3.cross(axis1).normalize().asVector();
        CartesianPoint localOrigin = transformation.localOrigin() == null
                ? new CartesianPoint(0.0, 0.0, 0.0)
                : builder.buildPoint(transformation.localOrigin().id());
        double scale = transformation.scale() <= 0.0 ? 1.0 : transformation.scale();
        Vector3 offset = point.subtract(new CartesianPoint(0.0, 0.0, 0.0));
        double x = offset.x() * axis1.x() + offset.y() * axis2.x() + offset.z() * axis3.x();
        double y = offset.x() * axis1.y() + offset.y() * axis2.y() + offset.z() * axis3.y();
        double z = offset.x() * axis1.z() + offset.y() * axis2.z() + offset.z() * axis3.z();
        return new CartesianPoint(
                localOrigin.x() + x * scale,
                localOrigin.y() + y * scale,
                localOrigin.z() + z * scale
        );
    }

    // ─── Payload conversion ──────────────────────────────────────────────

    public static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PreviewCurveEvaluator::toPointPayload).collect(Collectors.toList());
    }
}
