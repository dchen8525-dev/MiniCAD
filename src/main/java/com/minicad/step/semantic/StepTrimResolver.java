package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.syntax.StepValue;

import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Resolves trim values and parameters for TRIMMED_CURVE entities.
 * Handles entity-reference and numeric-parameter trims for both 3D and 2D curves.
 */
final class StepTrimResolver {

    private final Map<Integer, StepEntity> entitiesById;
    private final IntFunction<CartesianPoint> buildPoint;
    private final IntFunction<Point2> buildPoint2;

    StepTrimResolver(Map<Integer, StepEntity> entitiesById,
                     IntFunction<CartesianPoint> buildPoint,
                     IntFunction<Point2> buildPoint2) {
        this.entitiesById = entitiesById;
        this.buildPoint = buildPoint;
        this.buildPoint2 = buildPoint2;
    }

    /**
     * Validates a trim value which can be an entity reference or a parameter value.
     */
    void validateTrimValue(StepValue trim, Curve3 basis, String slot) {
        StepValue unwrapped = unwrapTyped(trim);
        if (unwrapped instanceof StepValue.ListValue innerList) {
            validateTrimValue(innerList.elements().getFirst(), basis, slot);
            return;
        }
        if (unwrapped instanceof StepValue.ReferenceValue ref) {
            if (ref.id() == 0) {
                throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " reference is invalid");
            }
        } else if (unwrapped instanceof StepValue.NumberValue) {
            // Parameter-value trim — accepted
        } else {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports entity reference or numeric parameter trims, got " + unwrapped.getClass().getSimpleName());
        }
    }

    /**
     * Resolves the first trim value to a parameter on the basis curve.
     */
    double resolveTrimParameter(List<StepValue> trims, Curve3 basis, String slot) {
        if (trims.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " must have at least one trim value");
        }
        StepValue trim = unwrapTyped(trims.getFirst());
        if (trim instanceof StepValue.ListValue innerList) {
            return resolveTrimParameter(innerList.elements(), basis, slot);
        }
        if (trim instanceof StepValue.NumberValue num) {
            return num.value();
        }
        if (trim instanceof StepValue.ReferenceValue ref) {
            StepEntity entity = entitiesById.get(ref.id());
            if (entity instanceof StepCartesianPoint point) {
                if (point.coordinates().size() < 2) {
                    throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must have at least 2 coordinates");
                }
                CartesianPoint cp = buildPoint.apply(point.id());
                return basis.parameterAt(cp);
            }
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " reference #" + ref.id() + " is not a CARTESIAN_POINT");
        }
        throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports entity reference or numeric parameter trims");
    }

    /**
     * Resolves the first trim value to a CartesianPoint on the basis curve.
     */
    CartesianPoint resolveTrimPoint3(List<StepValue> trims, Curve3 basis, String slot) {
        if (trims.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " must have at least one trim value");
        }
        StepValue trim = unwrapTyped(trims.getFirst());
        if (trim instanceof StepValue.ListValue innerList) {
            return resolveTrimPoint3(innerList.elements(), basis, slot);
        }
        if (trim instanceof StepValue.ReferenceValue ref) {
            StepEntity entity = entitiesById.get(ref.id());
            if (entity instanceof StepCartesianPoint point) {
                if (point.coordinates().size() < 2) {
                    throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must have at least 2 coordinates");
                }
                return buildPoint.apply(point.id());
            }
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " reference #" + ref.id() + " is not a CARTESIAN_POINT");
        }
        if (trim instanceof StepValue.NumberValue num) {
            return basis.pointAt(num.value());
        }
        throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports entity reference or numeric parameter trims");
    }

    Point2 requireTrimPoint2(List<StepEntity> trims, String slot) {
        if (trims.isEmpty() || !(trims.getFirst() instanceof StepCartesianPoint point)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports CARTESIAN_POINT trims");
        }
        if (point.coordinates().size() != 2) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must be 2D for PCURVE");
        }
        return buildPoint2.apply(point.id());
    }

    /**
     * Resolves a 2D trim value to a Point2.
     */
    Point2 resolveTrimPoint2(List<StepValue> trims, Curve2 basisCurve, String slot) {
        if (trims.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " must have at least one trim value");
        }
        StepValue trim = unwrapTyped(trims.getFirst());
        if (trim instanceof StepValue.ListValue innerList) {
            return resolveTrimPoint2(innerList.elements(), basisCurve, slot);
        }
        if (trim instanceof StepValue.ReferenceValue ref) {
            StepEntity entity = entitiesById.get(ref.id());
            if (entity instanceof StepCartesianPoint point) {
                if (point.coordinates().size() < 2) {
                    throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must have at least 2 coordinates");
                }
                Point2 result = buildPoint2.apply(point.id());
                return snapTrimPoint2(result, basisCurve);
            }
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " reference #" + ref.id() + " is not a CARTESIAN_POINT");
        }
        if (trim instanceof StepValue.NumberValue num) {
            return evaluateCurve2AtParameter(basisCurve, num.value());
        }
        throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports entity reference or numeric parameter trims");
    }

    /**
     * Resolves a 2D trim value to a double parameter on the basis curve.
     */
    double resolveTrimParam2(List<StepValue> trims, Curve2 basisCurve, String slot) {
        if (trims.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " must have at least one trim value");
        }
        StepValue trim = unwrapTyped(trims.getFirst());
        if (trim instanceof StepValue.ListValue innerList) {
            return resolveTrimParam2(innerList.elements(), basisCurve, slot);
        }
        if (trim instanceof StepValue.ReferenceValue ref) {
            StepEntity entity = entitiesById.get(ref.id());
            if (entity instanceof StepCartesianPoint point) {
                if (point.coordinates().size() < 2) {
                    throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must have at least 2 coordinates");
                }
                Point2 result = buildPoint2.apply(point.id());
                return parameterOnCurve2(basisCurve, result);
            }
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " reference #" + ref.id() + " is not a CARTESIAN_POINT");
        }
        if (trim instanceof StepValue.NumberValue num) {
            return num.value();
        }
        throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports entity reference or numeric parameter trims");
    }

    double parameterOnCurve2(Curve2 curve, Point2 point) {
        return switch (curve) {
            case Line2 line -> line.parameterOf(point);
            case Circle2 circle -> {
                Vector2 offset = point.subtract(circle.center());
                Vector2 x = circle.xDirection().asVector();
                Vector2 y = new Vector2(-x.y(), x.x());
                double angle = Math.atan2(offset.dot(y) / circle.radius(), offset.dot(x) / circle.radius());
                yield angle >= 0.0 ? angle : angle + Math.PI * 2.0;
            }
            case Ellipse2 ellipse -> {
                Vector2 offset = point.subtract(ellipse.center());
                Vector2 x = ellipse.xDirection().asVector();
                Vector2 y = new Vector2(-x.y(), x.x());
                double angle = Math.atan2(offset.dot(y) / ellipse.semiAxis2(), offset.dot(x) / ellipse.semiAxis1());
                yield angle >= 0.0 ? angle : angle + Math.PI * 2.0;
            }
            case BSplineCurve2 bspline -> parameterOnBSpline2(bspline, point);
            case RationalBSplineCurve2 rational -> parameterOnRationalBSpline2(rational, point);
            case Polyline2 polyline -> parameterOnPolyline2(polyline, point);
            case TrimmedCurve2 trimmed -> parameterOnCurve2(trimmed.basisCurve(), point);
            default -> 0.0;
        };
    }

    Point2 evaluateCurve2AtParameter(Curve2 curve, double param) {
        return switch (curve) {
            case Line2 line -> line.pointAt(param);
            case Circle2 circle -> circle.pointAt(param);
            case Ellipse2 ellipse -> ellipse.pointAt(param);
            case Polyline2 polyline -> evaluatePolyline2AtParameter(polyline, param);
            case TrimmedCurve2 trimmed -> evaluateCurve2AtParameter(trimmed.basisCurve(), param);
            case BSplineCurve2 bspline -> bspline.pointAt(param);
            case RationalBSplineCurve2 rational -> rational.pointAt(param);
            case CompositeCurve2 composite -> evaluateComposite2AtParameter(composite, param);
            default -> throw new UnsupportedGeometryException("TRIMMED_CURVE parameter trim not supported for 2D curve type " + curve.getClass().getSimpleName());
        };
    }

    Point2 snapTrimPoint2(Point2 point, Curve2 basisCurve) {
        if (basisCurve.contains(point)) {
            return point;
        }
        if (basisCurve instanceof Line2 line) {
            return line.closestPoint(point);
        }
        if (basisCurve instanceof Circle2 circle) {
            com.minicad.geometry2d.Vector2 offset = point.subtract(circle.center());
            double norm = offset.norm();
            if (norm <= Epsilon.EPS) {
                return circle.pointAt(0.0);
            }
            return circle.center().add(offset.scale(circle.radius() / norm));
        }
        if (basisCurve instanceof Ellipse2 ellipse) {
            com.minicad.geometry2d.Vector2 offset = point.subtract(ellipse.center());
            if (offset.norm() <= Epsilon.EPS) {
                return ellipse.pointAt(0.0);
            }
            com.minicad.geometry2d.Vector2 x = ellipse.xDirection().asVector();
            com.minicad.geometry2d.Vector2 y = new com.minicad.geometry2d.Vector2(-x.y(), x.x());
            double nx = offset.dot(x) / ellipse.semiAxis1();
            double ny = offset.dot(y) / ellipse.semiAxis2();
            double norm = Math.hypot(nx, ny);
            if (norm <= Epsilon.EPS) {
                return ellipse.pointAt(0.0);
            }
            double angle = Math.atan2(ny / norm, nx / norm);
            return ellipse.pointAt(angle);
        }
        if (basisCurve instanceof BSplineCurve2 spline) {
            Point2 best = null;
            double bestDistance = Double.POSITIVE_INFINITY;
            for (Point2 sample : spline.sample(192)) {
                double distance = sample.subtract(point).norm();
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = sample;
                }
            }
            if (best != null) {
                return best;
            }
        }
        if (basisCurve instanceof TrimmedCurve2 trimmed) {
            return snapTrimPoint2(point, trimmed.basisCurve());
        }
        return point;
    }

    private static StepValue unwrapTyped(StepValue value) {
        return value instanceof StepValue.TypedValue typed ? typed.value() : value;
    }

    private double parameterOnBSpline2(BSplineCurve2 bspline, Point2 point) {
        double bestT = bspline.startParameter();
        double minDist = Double.POSITIVE_INFINITY;
        int n = 64;
        double range = bspline.endParameter() - bspline.startParameter();
        for (int i = 0; i <= n; i++) {
            double t = bspline.startParameter() + range * i / n;
            double d = point.distanceTo(bspline.pointAt(t));
            if (d < minDist) {
                minDist = d;
                bestT = t;
            }
        }
        return bestT;
    }

    private double parameterOnRationalBSpline2(RationalBSplineCurve2 rational, Point2 point) {
        double bestT = rational.startParameter();
        double minDist = Double.POSITIVE_INFINITY;
        int n = 64;
        double range = rational.endParameter() - rational.startParameter();
        for (int i = 0; i <= n; i++) {
            double t = rational.startParameter() + range * i / n;
            double d = point.distanceTo(rational.pointAt(t));
            if (d < minDist) {
                minDist = d;
                bestT = t;
            }
        }
        return bestT;
    }

    private double parameterOnPolyline2(Polyline2 polyline, Point2 point) {
        List<Point2> points = polyline.points();
        if (points.size() < 2) return 0.0;
        double bestT = 0.0;
        double minDist = Double.POSITIVE_INFINITY;
        int n = points.size() - 1;
        for (int i = 0; i < n; i++) {
            double t = (double) i / n;
            double d = point.distanceTo(points.get(i));
            if (d < minDist) {
                minDist = d;
                bestT = t;
            }
        }
        return bestT;
    }

    private Point2 evaluatePolyline2AtParameter(Polyline2 polyline, double param) {
        List<Point2> points = polyline.points();
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE parameter trim on empty polyline");
        }
        int segments = points.size() - 1;
        double t = param * segments;
        int index = (int) Math.max(0, Math.min(Math.floor(t), segments - 1));
        double localT = t - index;
        Point2 p1 = points.get(index);
        Point2 p2 = points.get(index + 1);
        return new Point2(p1.x() + (p2.x() - p1.x()) * localT, p1.y() + (p2.y() - p1.y()) * localT);
    }

    private Point2 evaluateComposite2AtParameter(CompositeCurve2 composite, double param) {
        List<Curve2> segments = composite.segments();
        if (segments.isEmpty()) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE parameter trim on empty composite curve");
        }
        int segIndex = Math.min((int) param, segments.size() - 1);
        segIndex = Math.max(0, segIndex);
        double localT = param - segIndex;
        return evaluateCurve2AtParameter(segments.get(segIndex), localT);
    }
}
