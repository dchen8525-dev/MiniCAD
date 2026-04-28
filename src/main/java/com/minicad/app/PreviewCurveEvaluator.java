package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.annotation.StepAnnotationFillArea;
import com.minicad.step.model.annotation.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSymbol;
import com.minicad.step.model.annotation.StepAnnotationText;
import com.minicad.step.model.annotation.StepAnnotationTextCharacter;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.product.*;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.topology.StepConnectedEdgeSet;
import com.minicad.step.model.topology.StepEdgeCurve;
import com.minicad.step.model.topology.StepEdgeWire;
import com.minicad.step.model.topology.StepLoop;
import com.minicad.step.model.topology.StepWireShell;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        return switch (curve) {
            case StepLine line -> {
                Line3 geometry = builder.buildLine(line.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() { return -1.0; }
                    @Override
                    public double end() { return 1.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepCircle circle -> {
                Circle geometry = builder.buildCircle(circle.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepEllipse ellipse -> {
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() { return 0.0; }
                    @Override
                    public double end() { return Math.PI * 2.0; }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepBSplineCurveWithKnots spline -> {
                BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() { return geometry.startParameter(); }
                    @Override
                    public double end() { return geometry.endParameter(); }
                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepTrimmedCurve trimmedCurve -> curveEvaluator(trimmedCurve.basisCurve(), builder);
            case StepSurfaceCurve surfaceCurve -> curveEvaluator(surfaceCurve.curve3d(), builder);
            case StepRationalBSplineCurve spline -> {
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
                yield new CurveEvaluator() {
                    @Override public double start() { return geometry.startParameter(); }
                    @Override public double end() { return geometry.endParameter(); }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }
            case StepPolyline polyline -> {
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                yield new CurveEvaluator() {
                    @Override public double start() { return 0.0; }
                    @Override public double end() { return 1.0; }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }
            case StepCompositeCurve compositeCurve -> {
                CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
                yield sampledCurveEvaluator(geometry);
            }
            case StepBezierCurve bezier -> sampledCurveEvaluator(builder.buildCurveReference3(bezier.id()));
            case StepUniformCurve uniform -> sampledCurveEvaluator(builder.buildCurveReference3(uniform.id()));
            case StepQuasiUniformCurve quasiUniform -> sampledCurveEvaluator(builder.buildCurveReference3(quasiUniform.id()));
            case StepPiecewiseBezierCurve piecewiseBezier -> sampledCurveEvaluator(builder.buildCurveReference3(piecewiseBezier.id()));
            case StepOffsetCurve3D offsetCurve3D -> sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
            case StepConicCurve conic -> {
                List<CartesianPoint> points = sampleConicCurvePoints(conic, builder);
                if (points == null || points.size() < 2) yield null;
                yield sampledCurveEvaluator(new Polyline3(points));
            }
            case StepOrientedCurve orientedCurve -> curveEvaluator(orientedCurve.curveElement(), builder);
            case StepGeometricReplica replica -> curveEvaluator(replica.parent(), builder);
            case StepBSplineCurve bspline -> sampledCurveEvaluator(builder.buildCurveReference3(bspline.id()));
            case StepSeamCurve seamCurve -> sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
            case StepCircle2D circle2D -> sampledCurveEvaluator(builder.buildCurve3From2D(circle2D.id()));
            case StepEllipse2D ellipse2D -> sampledCurveEvaluator(builder.buildCurve3From2D(ellipse2D.id()));
            case StepPolyline2D polyline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(polyline2D.id()));
            case StepTrimmedCurve2D trimmedCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(trimmedCurve2D.id()));
            case StepCompositeCurve2D compositeCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(compositeCurve2D.id()));
            case StepBezierCurve2D bezier2D -> sampledCurveEvaluator(builder.buildCurve3From2D(bezier2D.id()));
            case StepQuasiUniformCurve2D quasiUniform2D -> sampledCurveEvaluator(builder.buildCurve3From2D(quasiUniform2D.id()));
            case StepUniformCurve2D uniform2D -> sampledCurveEvaluator(builder.buildCurve3From2D(uniform2D.id()));
            case StepPiecewiseBezierCurve2D piecewiseBezier2D -> sampledCurveEvaluator(builder.buildCurve3From2D(piecewiseBezier2D.id()));
            case StepIndexedPolyCurve2D polyCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(polyCurve2D.id()));
            case StepDegenerateCurve2D degenerateCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(degenerateCurve2D.id()));
            case StepBSplineCurve2D bspline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(bspline2D.id()));
            case StepRationalBSplineCurve2D rationalBspline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(rationalBspline2D.id()));
            case StepLine2D line2D -> sampledCurveEvaluator(builder.buildCurve3From2D(line2D.id()));
            case StepCurve2D curve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(curve2D.id()));
            case StepHyperbola2D hyperbola2D -> sampledCurveEvaluator(builder.buildCurve3From2D(hyperbola2D.id()));
            case StepParabola2D parabola2D -> sampledCurveEvaluator(builder.buildCurve3From2D(parabola2D.id()));
            case StepOffsetCurve2D offsetCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(offsetCurve2D.id()));
            case StepClothoid clothoid -> sampledCurveEvaluator(builder.buildCurveReference3(clothoid.id()));
            case StepIndexedPolyCurve polyCurve -> sampledCurveEvaluator(builder.buildCurveReference3(polyCurve.id()));
            case StepDegenerateCurve degenerate -> sampledCurveEvaluator(builder.buildCurveReference3(degenerate.id()));
            case StepBSplineCurveWithKnotsAndBreakpoints splineBreak -> sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
            case StepCompositeCurveOnSurface compositeOnSurface -> sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface.id()));
            case StepCompositeCurveOnSurface3D compositeOnSurface3D -> sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface3D.id()));
            case StepLineSegment lineSeg -> {
                List<CartesianPoint> pts = List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                );
                yield sampledCurveEvaluator(new Polyline3(pts));
            }
            case StepPath path -> sampledCurveEvaluator(builder.buildPath(path.id()));
            case StepOpenPath openPath -> sampledCurveEvaluator(builder.buildPath(openPath.id()));
            case StepSubpath subpath -> sampledCurveEvaluator(builder.buildPath(subpath.id()));
            case StepOrientedPath orientedPath -> sampledCurveEvaluator(builder.buildPath(orientedPath.id()));
            case StepEdgeCurve edgeCurve -> sampledCurveEvaluator(builder.buildCurveReference3(edgeCurve.id()));
            case StepSurfacedEdgeCurve surfacedEdge -> sampledCurveEvaluator(builder.buildCurveReference3(surfacedEdge.id()));
            case StepAnnotationCurveOccurrence occurrence -> curveEvaluator(occurrence.item(), builder);
            case StepDimensionCurve dimensionCurve -> curveEvaluator(dimensionCurve.item(), builder);
            case StepLeaderCurve leaderCurve -> curveEvaluator(leaderCurve.item(), builder);
            case StepProjectionCurve projectionCurve -> curveEvaluator(projectionCurve.item(), builder);
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> curveEvaluator(annotationOccurrence.item(), builder);
            case StepTerminatorSymbol terminatorSymbol -> curveEvaluator(terminatorSymbol.annotatedCurve(), builder);
            case StepCurve abstractCurve -> sampledCurveEvaluator(builder.buildCurveReference3(abstractCurve.id()));
            case StepBoundedCurve boundedCurve -> sampledCurveEvaluator(builder.buildCurveReference3(boundedCurve.id()));
            case StepMappedItem mappedItem -> curveEvaluator(mappedItem.mappingTarget(), builder);
            default -> null;
        };
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
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleTrimmedCurve3(trimmedCurve, 72);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleLooseCurve(surfaceCurve.curve3d());
        }
        if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> points = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleLooseCurve(segment);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        List<CartesianPoint> points = curve.sample(72);
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("curve sampling for " + curve.getClass().getSimpleName() + " is unsupported");
        }
        return points;
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
        if (curve instanceof Line2 line) {
            return List.of(line.pointAt(0.0), line.pointAt(1.0));
        }
        if (curve instanceof Circle2 circle) {
            return sampleCircle2Points(circle, 72);
        }
        if (curve instanceof Ellipse2 ellipse) {
            return sampleEllipse2Points(ellipse, 72);
        }
        if (curve instanceof Parabola2 parabola) {
            return parabola.sample(72);
        }
        if (curve instanceof Hyperbola2 hyperbola) {
            return hyperbola.sample(72);
        }
        if (curve instanceof DegenerateCurve2 degenerate) {
            return List.of(degenerate.point());
        }
        if (curve instanceof BSplineCurve2 spline) {
            return spline.sample(72);
        }
        if (curve instanceof RationalBSplineCurve2 spline) {
            return spline.sample(72);
        }
        if (curve instanceof TrimmedCurve2 trimmedCurve) {
            return sampleTrimmedCurve2(trimmedCurve, 72);
        }
        if (curve instanceof Polyline2 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve2 compositeCurve) {
            List<Point2> points = new ArrayList<>();
            boolean first = true;
            for (Curve2 segment : compositeCurve.segments()) {
                List<Point2> segmentPoints = sampleLooseCurve2(segment);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        throw new UnsupportedGeometryException("2D curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    public static List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleLooseCurve2(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().subtract(sampled.getLast()).norm() <= 1.0e-9;
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
        if (curve instanceof Line3) return "LINE";
        if (curve instanceof Circle) return "CIRCLE";
        if (curve instanceof Ellipse3) return "ELLIPSE";
        if (curve instanceof Parabola3) return "PARABOLA";
        if (curve instanceof Hyperbola3) return "HYPERBOLA";
        if (curve instanceof Clothoid3) return "CLOTHOID";
        if (curve instanceof DegenerateCurve3) return "DEGENERATE_CURVE";
        if (curve instanceof BSplineCurve3) return "B_SPLINE_CURVE";
        if (curve instanceof RationalBSplineCurve3) return "RATIONAL_B_SPLINE_CURVE";
        if (curve instanceof TrimmedCurve3) return "TRIMMED_CURVE";
        if (curve instanceof SurfaceCurve3) return "SURFACE_CURVE";
        if (curve instanceof Polyline3) return "POLYLINE";
        if (curve instanceof CompositeCurve3) return "COMPOSITE_CURVE";
        return curve.getClass().getSimpleName();
    }

    public static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) return "LINE";
        if (curve instanceof Circle2) return "CIRCLE";
        if (curve instanceof Ellipse2) return "ELLIPSE";
        if (curve instanceof Parabola2) return "PARABOLA";
        if (curve instanceof Hyperbola2) return "HYPERBOLA";
        if (curve instanceof DegenerateCurve2) return "DEGENERATE_CURVE";
        if (curve instanceof BSplineCurve2) return "B_SPLINE_CURVE";
        if (curve instanceof RationalBSplineCurve2) return "RATIONAL_B_SPLINE_CURVE";
        if (curve instanceof TrimmedCurve2) return "TRIMMED_CURVE";
        if (curve instanceof Polyline2) return "POLYLINE";
        if (curve instanceof CompositeCurve2) return "COMPOSITE_CURVE";
        return curve.getClass().getSimpleName();
    }

    // ─── Conic curve sampling ────────────────────────────────────────────

    public static List<CartesianPoint> sampleConicCurvePoints(StepConicCurve curve, StepCadBuilder builder) {
        double[] matrix = matrixForPlacementEntity(curve.position(), builder);
        if (matrix == null) {
            return null;
        }
        return switch (curve.entityName()) {
            case "CIRCLE" -> sampleConicCirclePoints(curve, matrix);
            case "ELLIPSE" -> sampleConicEllipsePoints(curve, matrix);
            case "PARABOLA" -> sampleParabolaPoints(curve, matrix);
            case "HYPERBOLA" -> sampleHyperbolaPoints(curve, matrix);
            case "DEGENERATE_CONIC" -> {
                CartesianPoint point = transformCartesian(new CartesianPoint(0.0, 0.0, 0.0), matrix);
                yield List.of(point, point);
            }
            default -> null;
        };
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
            points.add(transformCartesian(local, matrix));
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
            points.add(transformCartesian(new CartesianPoint(x, t, 0.0), matrix));
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
            points.add(transformCartesian(new CartesianPoint(x, y, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    // ─── Conic sampling helpers (copied from StepPreviewJsonExporter) ─────

    private static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    private static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D placement3D) {
            return com.minicad.app.StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D placement2D) {
            CartesianPoint origin = pointFromPlacement(placement2D);
            if (origin == null) return null;
            Vector3 x;
            if (placement2D.refDirection() == null) {
                x = new Vector3(1.0, 0.0, 0.0);
            } else {
                List<Double> ratios = placement2D.refDirection().directionRatios();
                x = new Vector3(ratios.get(0), ratios.get(1), 0.0).normalize().asVector();
            }
            Vector3 z = new Vector3(0.0, 0.0, 1.0);
            Vector3 y = z.cross(x).normalize().asVector();
            return new double[]{
                    x.x(), y.x(), z.x(), origin.x(),
                    x.y(), y.y(), z.y(), origin.y(),
                    x.z(), y.z(), z.z(), origin.z(),
                    0.0, 0.0, 0.0, 1.0
            };
        }
        return null;
    }

    private static CartesianPoint pointFromPlacement(StepAxis2Placement2D placement2D) {
        StepCartesianPoint point = placement2D.location();
        return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
    }

    // ─── Edge sampling ───────────────────────────────────────────────────

    public static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            List<CartesianPoint> points = new ArrayList<>(sampleTrimmedCurve3(trimmedCurve, 72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
        }
        if (curve instanceof BSplineCurve3 splineCurve) {
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve3 splineCurve) {
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
        if (curve instanceof Circle circle) {
            return sampleCircleArc(circle, start, end, naturalForward);
        }
        if (curve instanceof Ellipse3 ellipse) {
            return sampleEllipseArc(ellipse, start, end, naturalForward);
        }
        if (curve instanceof Polyline3 polyline) {
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
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
        if (curve instanceof Parabola3 parabola) {
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
        if (curve instanceof Hyperbola3 hyperbola) {
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
        if (curve instanceof Clothoid3 clothoid) {
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
        if (curve instanceof DegenerateCurve3 degenerate) {
            return List.of(start, end);
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, PARABOLA, HYPERBOLA, CLOTHOID, POLYLINE, COMPOSITE_CURVE, B_SPLINE, RATIONAL_B_SPLINE_CURVE, OFFSET_CURVE_2D/3D, SURFACE_CURVE, SEAM_CURVE, DEGENERATE_CURVE or TRIMMED_CURVE topology");
    }

    public static List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleLooseCurve(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().distanceTo(sampled.getLast()) <= 1.0e-9;
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
        if (points.isEmpty() || points.getLast().distanceTo(candidate) > 1.0e-9) {
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
        if (points.isEmpty() || points.getLast().subtract(candidate).norm() > 1.0e-9) {
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
            if (edgeGeometry instanceof StepCircle circle) {
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(), "circle_arc", null, null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(), null, null,
                        startAngle, arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward));
            }
            if (edgeGeometry instanceof StepEllipse ellipse) {
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(), "ellipse_arc", null, null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null, geometry.semiAxis1(), geometry.semiAxis2(),
                        startAngle, arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward));
            }
            if (edgeGeometry instanceof StepBSplineCurveWithKnots bspline) {
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepBSplineCurve bspline) {
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepRationalBSplineCurve rational) {
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve3(rational.id());
                return newRationalBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepPolyline polyline) {
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return newPolylineCurvePayload(edgeGeometry.id(), geometry, start, end);
            }
            if (edgeGeometry instanceof StepLine line) {
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
        return new EdgePayload(item.id(), toPointPayloads(points), sampledCurvePayload(item, builder));
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
            if (item instanceof StepCircle circle) {
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
            if (item instanceof StepEllipse ellipse) {
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

    public static String previewCurveTypeName(StepEntity item) {
        if (item instanceof StepLine) return "LINE";
        if (item instanceof StepCircle) return "CIRCLE";
        if (item instanceof StepEllipse) return "ELLIPSE";
        if (item instanceof StepConicCurve conic) return conic.entityName();
        if (item instanceof StepBezierCurve) return "BEZIER_CURVE";
        if (item instanceof StepUniformCurve) return "UNIFORM_CURVE";
        if (item instanceof StepQuasiUniformCurve) return "QUASI_UNIFORM_CURVE";
        if (item instanceof StepPiecewiseBezierCurve) return "PIECEWISE_BEZIER_CURVE";
        if (item instanceof StepBSplineCurveWithKnots) return "B_SPLINE_CURVE_WITH_KNOTS";
        if (item instanceof StepRationalBSplineCurve) return "RATIONAL_B_SPLINE_CURVE";
        if (item instanceof StepSurfaceCurve surfaceCurve) return surfaceCurve.entityName();
        if (item instanceof StepSeamCurve) return "SEAM_CURVE";
        if (item instanceof StepTrimmedCurve) return "TRIMMED_CURVE";
        if (item instanceof StepPolyline) return "POLYLINE";
        if (item instanceof StepCompositeCurve) return "COMPOSITE_CURVE";
        if (item instanceof StepCompositeCurveOnSurface) return "COMPOSITE_CURVE_ON_SURFACE";
        if (item instanceof StepOffsetCurve2D) return "OFFSET_CURVE_2D";
        if (item instanceof StepOffsetCurve3D) return "OFFSET_CURVE_3D";
        if (item instanceof StepPcurve) return "PCURVE";
        if (item instanceof StepDegeneratePcurve) return "DEGENERATE_PCURVE";
        if (item instanceof StepOrientedCurve) return "ORIENTED_CURVE";
        if (item instanceof StepAnnotationCurveOccurrence) return "ANNOTATION_CURVE_OCCURRENCE";
        if (item instanceof StepDimensionCurve) return "DIMENSION_CURVE";
        if (item instanceof StepLeaderCurve) return "LEADER_CURVE";
        if (item instanceof StepProjectionCurve) return "PROJECTION_CURVE";
        if (item instanceof StepDraughtingAnnotationOccurrence) return "DRAUGHTING_ANNOTATION_OCCURRENCE";
        if (item instanceof StepTerminatorSymbol) return "TERMINATOR_SYMBOL";
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) return "CURVE_REPLICA";
        if (item instanceof StepBSplineCurve) return "B_SPLINE_CURVE";
        if (item instanceof StepRationalBSplineCurve) return "RATIONAL_B_SPLINE_CURVE";
        if (item instanceof StepCompositeCurveOnSurface3D) return "COMPOSITE_CURVE_ON_SURFACE_3D";
        if (item instanceof StepClothoid) return "CLOTHOID";
        if (item instanceof StepIndexedPolyCurve) return "INDEXED_POLY_CURVE";
        if (item instanceof StepDegenerateCurve) return "DEGENERATE_CURVE";
        if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints) return "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS";
        if (item instanceof StepLineSegment) return "LINE_SEGMENT";
        if (item instanceof StepEdgeCurve) return "EDGE_CURVE";
        if (item instanceof StepSurfacedEdgeCurve) return "SURFACED_EDGE_CURVE";
        if (item instanceof StepCompositeCurveOnSurface) return "COMPOSITE_CURVE_ON_SURFACE";
        if (item instanceof StepPath) return "PATH";
        if (item instanceof StepOpenPath) return "OPEN_PATH";
        if (item instanceof StepSubpath) return "SUBPATH";
        if (item instanceof StepOrientedPath orientedPath) return "ORIENTED_PATH";
        if (item instanceof StepCurve) return "CURVE";
        if (item instanceof StepBoundedCurve) return "BOUNDED_CURVE";
        if (item instanceof StepCircle2D) return "CIRCLE_2D";
        if (item instanceof StepEllipse2D) return "ELLIPSE_2D";
        if (item instanceof StepPolyline2D) return "POLYLINE_2D";
        if (item instanceof StepTrimmedCurve2D) return "TRIMMED_CURVE_2D";
        if (item instanceof StepCompositeCurve2D) return "COMPOSITE_CURVE_2D";
        if (item instanceof StepBezierCurve2D) return "BEZIER_CURVE_2D";
        if (item instanceof StepQuasiUniformCurve2D) return "QUASI_UNIFORM_CURVE_2D";
        if (item instanceof StepUniformCurve2D) return "UNIFORM_CURVE_2D";
        if (item instanceof StepPiecewiseBezierCurve2D) return "PIECEWISE_BEZIER_CURVE_2D";
        if (item instanceof StepIndexedPolyCurve2D) return "INDEXED_POLY_CURVE_2D";
        if (item instanceof StepDegenerateCurve2D) return "DEGENERATE_CURVE_2D";
        if (item instanceof StepBSplineCurve2D) return "B_SPLINE_CURVE_2D";
        if (item instanceof StepRationalBSplineCurve2D) return "RATIONAL_B_SPLINE_CURVE_2D";
        if (item instanceof StepLine2D) return "LINE_2D";
        if (item instanceof StepCurve2D) return "CURVE_2D";
        if (item instanceof StepHyperbola2D) return "HYPERBOLA_2D";
        if (item instanceof StepParabola2D) return "PARABOLA_2D";
        return null;
    }

    public static String previewCurveBasisTypeName(StepEntity item) {
        if (item instanceof StepSurfaceCurve surfaceCurve) return previewCurveTypeName(surfaceCurve.curve3d());
        if (item instanceof StepSeamCurve seamCurve) return previewCurveTypeName(seamCurve.curve3d());
        if (item instanceof StepTrimmedCurve trimmedCurve) return previewCurveTypeName(trimmedCurve.basisCurve());
        if (item instanceof StepOffsetCurve2D offsetCurve2D) return previewCurveTypeName(offsetCurve2D.basisCurve());
        if (item instanceof StepOffsetCurve3D offsetCurve3D) return previewCurveTypeName(offsetCurve3D.basisCurve());
        if (item instanceof StepOrientedCurve orientedCurve) return previewCurveTypeName(orientedCurve.curveElement());
        if (item instanceof StepAnnotationCurveOccurrence occurrence) return previewCurveTypeName(occurrence.item());
        if (item instanceof StepDimensionCurve dimensionCurve) return previewCurveTypeName(dimensionCurve.item());
        if (item instanceof StepLeaderCurve leaderCurve) return previewCurveTypeName(leaderCurve.item());
        if (item instanceof StepProjectionCurve projectionCurve) return previewCurveTypeName(projectionCurve.item());
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) return previewCurveTypeName(annotationOccurrence.item());
        if (item instanceof StepTerminatorSymbol terminatorSymbol) return previewCurveTypeName(terminatorSymbol.annotatedCurve());
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) return previewCurveTypeName(replica.parent());
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) return previewCurveTypeName(trimmedCurve2D.basisCurve());
        return null;
    }

    public static Integer previewCurveBasisStepId(StepEntity item) {
        if (item instanceof StepSurfaceCurve surfaceCurve) return surfaceCurve.curve3d().id();
        if (item instanceof StepSeamCurve seamCurve) return seamCurve.curve3d().id();
        if (item instanceof StepTrimmedCurve trimmedCurve) return trimmedCurve.basisCurve().id();
        if (item instanceof StepOffsetCurve2D offsetCurve2D) return offsetCurve2D.basisCurve().id();
        if (item instanceof StepOffsetCurve3D offsetCurve3D) return offsetCurve3D.basisCurve().id();
        if (item instanceof StepOrientedCurve orientedCurve) return orientedCurve.curveElement().id();
        if (item instanceof StepAnnotationCurveOccurrence occurrence) return occurrence.item().id();
        if (item instanceof StepDimensionCurve dimensionCurve) return dimensionCurve.item().id();
        if (item instanceof StepLeaderCurve leaderCurve) return leaderCurve.item().id();
        if (item instanceof StepProjectionCurve projectionCurve) return projectionCurve.item().id();
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) return annotationOccurrence.item().id();
        if (item instanceof StepTerminatorSymbol terminatorSymbol) return terminatorSymbol.annotatedCurve().id();
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) return replica.parent().id();
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) return trimmedCurve2D.basisCurve().id();
        return null;
    }

    public static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve orientedCurve) return orientedCurve.orientation();
        return null;
    }

    public static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve trimmedCurve) return trimmedCurve.senseAgreement();
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) return trimmedCurve2D.senseAgreement();
        return null;
    }

    public static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D offsetCurve2D) return offsetCurve2D.distance();
        if (item instanceof StepOffsetCurve3D offsetCurve3D) return offsetCurve3D.distance();
        return null;
    }

    public static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D offsetCurve2D) return offsetCurve2D.selfIntersect();
        if (item instanceof StepOffsetCurve3D offsetCurve3D) return offsetCurve3D.selfIntersect();
        if (item instanceof StepCompositeCurveOnSurface compositeOnSurface) return compositeOnSurface.selfIntersect();
        return null;
    }

    public static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D offsetCurve3D) return List.copyOf(offsetCurve3D.refDirection().directionRatios());
        return null;
    }

    public static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) return replica.transformation().scale();
        return null;
    }

    public static String previewCurveMasterRepresentation(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve surfaceCurve) return surfaceCurve.masterRepresentation();
        if (semanticCurve instanceof StepSeamCurve seamCurve) return seamCurve.masterRepresentation();
        return null;
    }

    public static List<String> previewCurveAssociatedSurfaceTypes(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) return null;
        List<String> surfaceTypes = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve) {
                surfaceTypes.add(StepPreviewJsonExporter.surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve pcurve) {
                surfaceTypes.add(StepPreviewJsonExporter.surfaceTypeName(pcurve.basisSurface()));
            }
        }
        return surfaceTypes.isEmpty() ? null : List.copyOf(surfaceTypes);
    }

    public static List<Integer> previewCurveAssociatedSurfaceStepIds(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) return null;
        List<Integer> surfaceIds = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve) {
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve pcurve) {
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }

    private static List<StepEntity> previewCurveAssociatedGeometry(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve surfaceCurve) return surfaceCurve.associatedGeometry();
        if (semanticCurve instanceof StepSeamCurve seamCurve) return seamCurve.associatedGeometry();
        return null;
    }

    public static StepEntity previewCurveSemanticItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepOrientedCurve orientedCurve) { current = orientedCurve.curveElement(); continue; }
            if (current instanceof StepAnnotationCurveOccurrence occurrence) { current = occurrence.item(); continue; }
            if (current instanceof StepDimensionCurve dimensionCurve) { current = dimensionCurve.item(); continue; }
            if (current instanceof StepLeaderCurve leaderCurve) { current = leaderCurve.item(); continue; }
            if (current instanceof StepProjectionCurve projectionCurve) { current = projectionCurve.item(); continue; }
            if (current instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) { current = annotationOccurrence.item(); continue; }
            if (current instanceof StepTerminatorSymbol terminatorSymbol) { current = terminatorSymbol.annotatedCurve(); continue; }
            if (current instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) { current = replica.parent(); continue; }
            return current;
        }
    }

    // ─── sampleLooseEdgePoints (copied from StepPreviewJsonExporter) ─────

    private static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        // This is a large method copied verbatim from StepPreviewJsonExporter.
        // Since it's private in the exporter and we cannot modify that file,
        // we include the full implementation here.
        if (item instanceof StepAnnotationFillArea fillArea) {
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(), builder);
        }
        if (item instanceof StepAnnotationText annotationText) {
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(), builder);
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(), builder);
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            List<CartesianPoint> parentPoints = sampleLooseEdgePoints(replica.parent(), builder);
            if (parentPoints == null) return null;
            List<CartesianPoint> transformed = new ArrayList<>(parentPoints.size());
            for (CartesianPoint point : parentPoints) {
                transformed.add(transformPoint(point, replica.transformation(), builder));
            }
            return List.copyOf(transformed);
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
            List<CartesianPoint> points = sampleLooseEdgePoints(orientedCurve.curveElement(), builder);
            if (points == null) return null;
            if (orientedCurve.orientation()) return points;
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            return List.copyOf(reversed);
        }
        if (item instanceof StepGeometricSet geometricSet) {
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet connectedEdgeSet) {
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell wireShell) {
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire edgeWire) {
            return sampleGeometricCollectionPoints(edgeWire.edges(), builder);
        }
        Curve3 curve = curveForLooseEdge(item, builder);
        if (curve == null) return null;
        return sampleLooseCurve(curve);
    }

    private static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            if (item instanceof StepLine line) return builder.buildLine(line.id());
            if (item instanceof StepCircle circle) return builder.buildCircle(circle.id());
            if (item instanceof StepEllipse ellipse) return builder.buildEllipse(ellipse.id());
            if (item instanceof StepConicCurve conic) {
                List<CartesianPoint> points = sampleConicCurvePoints(conic, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepBezierCurve curve) return builder.buildCurveReference3(curve.id());
            if (item instanceof StepUniformCurve curve) return builder.buildCurveReference3(curve.id());
            if (item instanceof StepQuasiUniformCurve curve) return builder.buildCurveReference3(curve.id());
            if (item instanceof StepPiecewiseBezierCurve curve) return builder.buildCurveReference3(curve.id());
            if (item instanceof StepBSplineCurveWithKnots spline) return builder.buildBSplineCurve(spline.id());
            if (item instanceof StepSurfaceCurve surfaceCurve) return builder.buildSurfaceCurve(surfaceCurve.id());
            if (item instanceof StepSeamCurve seamCurve) return builder.buildSeamCurve(seamCurve.id());
            if (item instanceof StepTrimmedCurve trimmedCurve) return builder.buildTrimmedCurve(trimmedCurve.id());
            if (item instanceof StepPolyline polyline) return builder.buildPolyline(polyline.id());
            if (item instanceof StepCompositeCurve compositeCurve) return builder.buildCompositeCurve(compositeCurve.id());
            if (item instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) return builder.buildCompositeCurve(compositeCurveOnSurface.id());
            if (item instanceof StepRationalBSplineCurve spline) return builder.buildRationalBSplineCurve(spline.id());
            if (item instanceof StepOffsetCurve2D offsetCurve2D) return liftCurve2(builder.buildOffsetCurve2(offsetCurve2D.id()));
            if (item instanceof StepOffsetCurve3D offsetCurve3D) return builder.buildOffsetCurve3(offsetCurve3D.id());
            if (item instanceof StepPcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? liftCurve2(curve2) : null;
            }
            if (item instanceof StepDegeneratePcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? liftCurve2(curve2) : null;
            }
            if (item instanceof StepOrientedCurve orientedCurve) return curveForLooseEdge(orientedCurve.curveElement(), builder);
            if (item instanceof StepAnnotationCurveOccurrence occurrence) return curveForLooseEdge(occurrence.item(), builder);
            if (item instanceof StepDimensionCurve dimensionCurve) return curveForLooseEdge(dimensionCurve.item(), builder);
            if (item instanceof StepLeaderCurve leaderCurve) return curveForLooseEdge(leaderCurve.item(), builder);
            if (item instanceof StepProjectionCurve projectionCurve) return curveForLooseEdge(projectionCurve.item(), builder);
            if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) return curveForLooseEdge(annotationOccurrence.item(), builder);
            if (item instanceof StepTerminatorSymbol terminatorSymbol) return curveForLooseEdge(terminatorSymbol.annotatedCurve(), builder);
            if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                return curveForLooseEdge(replica.parent(), builder);
            }
            if (item instanceof StepPath path) return builder.buildPath(path.id());
            if (item instanceof StepOpenPath openPath) return builder.buildPath(openPath.id());
            if (item instanceof StepSubpath subpath) return builder.buildPath(subpath.id());
            if (item instanceof StepOrientedPath orientedPath) return builder.buildPath(orientedPath.id());
            if (item instanceof StepCompositeCurve2D compositeCurve2D) return builder.buildCurve3From2D(compositeCurve2D.id());
            if (item instanceof StepTrimmedCurve2D trimmedCurve2D) return builder.buildCurve3From2D(trimmedCurve2D.id());
            if (item instanceof StepBezierCurve2D bezier2D) return builder.buildCurve3From2D(bezier2D.id());
            if (item instanceof StepQuasiUniformCurve2D quasiUniform2D) return builder.buildCurve3From2D(quasiUniform2D.id());
            if (item instanceof StepUniformCurve2D uniform2D) return builder.buildCurve3From2D(uniform2D.id());
            if (item instanceof StepPiecewiseBezierCurve2D piecewiseBezier2D) return builder.buildCurve3From2D(piecewiseBezier2D.id());
            if (item instanceof StepIndexedPolyCurve2D polyCurve2D) return builder.buildCurve3From2D(polyCurve2D.id());
            if (item instanceof StepDegenerateCurve2D degenerateCurve2D) return builder.buildCurve3From2D(degenerateCurve2D.id());
            if (item instanceof StepBSplineCurve2D bspline2D) return builder.buildCurve3From2D(bspline2D.id());
            if (item instanceof StepRationalBSplineCurve2D rationalBspline2D) return builder.buildCurve3From2D(rationalBspline2D.id());
            if (item instanceof StepLine2D line2D) return builder.buildCurve3From2D(line2D.id());
            if (item instanceof StepCurve2D curve2D) return builder.buildCurve3From2D(curve2D.id());
            if (item instanceof StepHyperbola2D hyperbola2D) return builder.buildCurve3From2D(hyperbola2D.id());
            if (item instanceof StepParabola2D parabola2D) return builder.buildCurve3From2D(parabola2D.id());
            if (item instanceof StepEdgeCurve edgeCurve) return builder.buildCurveReference3(edgeCurve.id());
            if (item instanceof StepSurfacedEdgeCurve surfacedEdge) return builder.buildCurveReference3(surfacedEdge.id());
            if (item instanceof StepCurve abstractCurve) return builder.buildCurveReference3(abstractCurve.id());
            if (item instanceof StepBoundedCurve boundedCurve) return builder.buildCurveReference3(boundedCurve.id());
            if (item instanceof StepEdgeBasedWireframeModel edgeBasedWireframe) {
                List<CartesianPoint> sampled = sampleWireframeBoundaryPoints(edgeBasedWireframe.boundaries(), builder);
                return sampled == null || sampled.size() < 2 ? null : new Polyline3(sampled);
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
        double[] matrix = matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) return null;
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity content : representation.items()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
            if (sampled == null) continue;
            for (CartesianPoint point : sampled) points.add(transformCartesian(point, matrix));
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
        if (mappedOrigin instanceof StepRepresentation mappedRep) {
            // Check for placement-like items in the representation
            for (StepEntity repItem : mappedRep.items()) {
                if (repItem instanceof StepAxis2Placement3D) {
                    originPlacement = repItem;
                    break;
                }
            }
        }
        double[] originMatrix = originPlacement == null ? null : matrixForPlacementEntity(originPlacement, builder);
        double[] targetMatrix = targetPlacement == null ? null : matrixForPlacementEntity(targetPlacement, builder);
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

    static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(PreviewCurveEvaluator::toPointPayload).toList();
    }
}
