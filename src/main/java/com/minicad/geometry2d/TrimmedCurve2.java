package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal 2D trimmed curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve2(
        Curve2 basisCurve,
        double trimParamStart,
        double trimParamEnd,
        boolean senseAgreement
) implements Curve2 {

    public TrimmedCurve2 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireFinite(trimParamStart, "trimParamStart");
        Preconditions.requireFinite(trimParamEnd, "trimParamEnd");
    }

    /**
     * Returns the geometric trim start point.
     */
    public Point2 trimStart() {
        if (basisCurve instanceof BSplineCurve2 bs) {
            return bs.pointAt(trimParamStart);
        } else if (basisCurve instanceof RationalBSplineCurve2 rb) {
            return rb.pointAt(trimParamStart);
        } else if (basisCurve instanceof Circle2 circle) {
            return circle.pointAt(trimParamStart);
        } else if (basisCurve instanceof Ellipse2 ellipse) {
            return ellipse.pointAt(trimParamStart);
        } else if (basisCurve instanceof Line2 line) {
            return line.pointAt(trimParamStart);
        } else if (basisCurve instanceof Polyline2 polyline) {
            return polyline.pointAt(trimParamStart);
        } else if (basisCurve instanceof Hyperbola2 hyperbola) {
            return hyperbola.pointAt(trimParamStart);
        } else if (basisCurve instanceof Parabola2 parabola) {
            return parabola.pointAt(trimParamStart);
        } else if (basisCurve instanceof CompositeCurve2 composite) {
            return composite.pointAt(trimParamStart);
        }
        // Fallback: linear interpolation
        return basisCurve.closestPointTo(new Point2(trimParamStart, 0));
    }

    /**
     * Returns the geometric trim end point.
     */
    public Point2 trimEnd() {
        if (basisCurve instanceof BSplineCurve2 bs) {
            return bs.pointAt(trimParamEnd);
        } else if (basisCurve instanceof RationalBSplineCurve2 rb) {
            return rb.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Circle2 circle) {
            return circle.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Ellipse2 ellipse) {
            return ellipse.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Line2 line) {
            return line.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Polyline2 polyline) {
            return polyline.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Hyperbola2 hyperbola) {
            return hyperbola.pointAt(trimParamEnd);
        } else if (basisCurve instanceof Parabola2 parabola) {
            return parabola.pointAt(trimParamEnd);
        } else if (basisCurve instanceof CompositeCurve2 composite) {
            return composite.pointAt(trimParamEnd);
        }
        // Fallback: linear interpolation
        return basisCurve.closestPointTo(new Point2(trimParamEnd, 0));
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Find closest point on the trimmed segment
        Point2 closest = closestPointTo(point);
        if (point.distanceTo(closest) > Epsilon.EPS * 10) {
            return false;
        }
        // Find the parameter of the closest point on the basis curve
        double param = parameterOnBasisCurve(closest);
        double minP = Math.min(trimParamStart, trimParamEnd);
        double maxP = Math.max(trimParamStart, trimParamEnd);
        return param >= minP - Epsilon.EPS && param <= maxP + Epsilon.EPS;
    }

    /**
     * Evaluates a point on the trimmed curve at the given parameter.
     * The parameter is linearly mapped from [0,1] to [trimParamStart, trimParamEnd].
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the basis curve at the mapped parameter
     */
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double mapped = senseAgreement
                ? trimParamStart + (trimParamEnd - trimParamStart) * parameter
                : trimParamEnd + (trimParamStart - trimParamEnd) * parameter;
        return pointOnBasisCurve(mapped);
    }

    private Point2 pointOnBasisCurve(double parameter) {
        if (basisCurve instanceof BSplineCurve2 bs) {
            return bs.pointAt(parameter);
        } else if (basisCurve instanceof RationalBSplineCurve2 rb) {
            return rb.pointAt(parameter);
        } else if (basisCurve instanceof Circle2 circle) {
            return circle.pointAt(parameter);
        } else if (basisCurve instanceof Ellipse2 ellipse) {
            return ellipse.pointAt(parameter);
        } else if (basisCurve instanceof Line2 line) {
            return line.pointAt(parameter);
        } else if (basisCurve instanceof Polyline2 polyline) {
            return polyline.pointAt(parameter);
        } else if (basisCurve instanceof Hyperbola2 hyperbola) {
            return hyperbola.pointAt(parameter);
        } else if (basisCurve instanceof Parabola2 parabola) {
            return parabola.pointAt(parameter);
        } else if (basisCurve instanceof CompositeCurve2 composite) {
            return composite.pointAt(parameter);
        } else if (basisCurve instanceof TrimmedCurve2 trimmed) {
            return trimmed.pointAt(parameter);
        }
        // Fallback: linear interpolation between trim points
        return interpolatePoint(trimStart(), trimEnd(), parameter);
    }

    private double parameterOnBasisCurve(Point2 point) {
        if (basisCurve instanceof BSplineCurve2 bs) {
            Point2 closest = bs.closestPointTo(point);
            double bestT = bs.startParameter();
            double minDist = Double.POSITIVE_INFINITY;
            int n = 64;
            double range = bs.endParameter() - bs.startParameter();
            for (int i = 0; i <= n; i++) {
                double t = bs.startParameter() + range * i / n;
                double d = point.distanceTo(bs.pointAt(t));
                if (d < minDist) {
                    minDist = d;
                    bestT = t;
                }
            }
            return bestT;
        } else if (basisCurve instanceof RationalBSplineCurve2 rb) {
            Point2 closest = rb.closestPointTo(point);
            double bestT = rb.startParameter();
            double minDist = Double.POSITIVE_INFINITY;
            int n = 64;
            double range = rb.endParameter() - rb.startParameter();
            for (int i = 0; i <= n; i++) {
                double t = rb.startParameter() + range * i / n;
                double d = point.distanceTo(rb.pointAt(t));
                if (d < minDist) {
                    minDist = d;
                    bestT = t;
                }
            }
            return bestT;
        } else if (basisCurve instanceof Circle2 circle) {
            return circle.angleOf(point);
        } else if (basisCurve instanceof Ellipse2 ellipse) {
            return ellipse.angleOf(point);
        } else if (basisCurve instanceof Line2 line) {
            return line.parameterOf(point);
        } else if (basisCurve instanceof Polyline2 polyline) {
            return polyline.parameterOf(point);
        } else if (basisCurve instanceof Parabola2 parabola) {
            return parabola.parameterOf(point);
        } else if (basisCurve instanceof Hyperbola2 hyperbola) {
            return hyperbola.parameterOf(point);
        } else if (basisCurve instanceof CompositeCurve2 composite) {
            return composite.parameterOf(point);
        } else if (basisCurve instanceof DegenerateCurve2) {
            return 0.0;
        } else if (basisCurve instanceof TrimmedCurve2 trimmed) {
            return trimmed.parameterOnTrimmedCurve(point);
        }
        // Fallback: sampling-based
        double bestT = 0.0;
        double minDist = Double.POSITIVE_INFINITY;
        java.util.List<Point2> samples = sample(64);
        for (int i = 0; i < samples.size(); i++) {
            double d = point.distanceTo(samples.get(i));
            if (d < minDist) {
                minDist = d;
                bestT = trimParamStart + (trimParamEnd - trimParamStart) * (double) i / (samples.size() - 1);
            }
        }
        return bestT;
    }

    double parameterOnTrimmedCurve(Point2 point) {
        double basisParam = parameterOnBasisCurve(point);
        double minP = Math.min(trimParamStart, trimParamEnd);
        double maxP = Math.max(trimParamStart, trimParamEnd);
        double range = maxP - minP;
        if (range <= Epsilon.EPS) {
            return 0.0;
        }
        double normalized = Math.max(0.0, Math.min(1.0, (basisParam - minP) / range));
        return senseAgreement ? normalized : 1.0 - normalized;
    }

    double parameterOnUnderlyingCurve(Point2 point) {
        return basisCurve instanceof TrimmedCurve2 trimmed
                ? trimmed.parameterOnUnderlyingCurve(point)
                : parameterOnBasisCurve(point);
    }

    private static Point2 interpolatePoint(Point2 start, Point2 end, double t) {
        return new Point2(
            start.x() + (end.x() - start.x()) * t,
            start.y() + (end.y() - start.y()) * t
        );
    }

    /**
     * Samples the trimmed curve by sampling the basis curve between trim parameters.
     *
     * @param segments number of segments to sample
     * @return list of sampled points
     */
    public java.util.List<Point2> sample(int segments) {
        java.util.List<Point2> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            double mapped = senseAgreement
                    ? trimParamStart + (trimParamEnd - trimParamStart) * t
                    : trimParamEnd + (trimParamStart - trimParamEnd) * t;
            points.add(pointOnBasisCurve(mapped));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Returns the bounding box enclosing the trimmed curve by sampling.
     *
     * @return bounding box enclosing the trimmed segment
     */
    public BoundingBox2 boundingBox() {
        BoundingBox2 box = BoundingBox2.empty();
        java.util.List<Point2> samples = sample(64);
        for (Point2 point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the length of the trimmed curve segment.
     * Delegates to the basis curve's analytical length when available.
     *
     * @return arc length over the trimmed parameter range
     */
    public double length() {
        double tMin = Math.min(trimParamStart, trimParamEnd);
        double tMax = Math.max(trimParamStart, trimParamEnd);
        return switch (basisCurve) {
            case Circle2 c -> c.circumference() * (tMax - tMin) / (2.0 * Math.PI);
            case Ellipse2 e -> e.perimeter() * (tMax - tMin) / (2.0 * Math.PI);
            case Line2 line2 -> trimEnd().distanceTo(trimStart());
            case BSplineCurve2 bs -> bs.length(tMin, tMax);
            case RationalBSplineCurve2 rb -> rb.length(tMin, tMax);
            case Parabola2 p -> p.length(tMin, tMax);
            case Hyperbola2 h -> h.length(tMin, tMax, true);
            case TrimmedCurve2 tc -> {
                // Recursively compute length of nested trimmed curve, then take the appropriate fraction
                double innerMin = Math.min(tc.trimParamStart, tc.trimParamEnd);
                double innerMax = Math.max(tc.trimParamStart, tc.trimParamEnd);
                double innerLength = tc.length();
                double innerRange = innerMax - innerMin;
                if (Math.abs(innerRange) < Epsilon.EPS) yield 0.0;
                double outerMin = innerMin + (innerMax - innerMin) * tMin;
                double outerMax = innerMin + (innerMax - innerMin) * tMax;
                yield innerLength * (outerMax - outerMin) / innerRange;
            }
            case DegenerateCurve2 deg2 -> 0.0;
            case Polyline2 pl -> {
                int n = pl.points().size();
                if (n < 2) yield 0.0;
                double total = 0.0;
                for (int i = 0; i < n - 1; i++) {
                    double segStart = (double) i / (n - 1);
                    double segEnd = (double) (i + 1) / (n - 1);
                    if (segEnd < tMin - Epsilon.EPS || segStart > tMax + Epsilon.EPS) continue;
                    double overlapStart = Math.max(segStart, tMin);
                    double overlapEnd = Math.min(segEnd, tMax);
                    if (overlapEnd > overlapStart) {
                        double segLen = pl.points().get(i).distanceTo(pl.points().get(i + 1));
                        total += segLen * (overlapEnd - overlapStart) / (segEnd - segStart);
                    }
                }
                yield total;
            }
            default -> {
                java.util.List<Point2> samples = sample(256);
                double total = 0.0;
                for (int i = 0; i < samples.size() - 1; i++) {
                    total += samples.get(i).distanceTo(samples.get(i + 1));
                }
                yield total;
            }
        };
    }

    /**
     * Returns the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double mapped = senseAgreement
                ? trimParamStart + (trimParamEnd - trimParamStart) * parameter
                : trimParamEnd + (trimParamStart - trimParamEnd) * parameter;
        Vector2 tangent = tangentOnBasisCurve(mapped);
        if (!senseAgreement) {
            tangent = tangent.negate();
        }
        return tangent;
    }

    private Vector2 tangentOnBasisCurve(double parameter) {
        if (basisCurve instanceof BSplineCurve2 bs) {
            return bs.tangentAt(parameter);
        } else if (basisCurve instanceof RationalBSplineCurve2 rb) {
            return rb.tangentAt(parameter);
        } else if (basisCurve instanceof Circle2 circle) {
            return circle.tangentAt(parameter);
        } else if (basisCurve instanceof Ellipse2 ellipse) {
            return ellipse.tangentAt(parameter);
        } else if (basisCurve instanceof Line2 line) {
            return line.tangentAt(parameter);
        } else if (basisCurve instanceof Polyline2 polyline) {
            return polyline.tangentAt(parameter);
        } else if (basisCurve instanceof Hyperbola2 hyperbola) {
            return hyperbola.tangentAt(parameter);
        } else if (basisCurve instanceof Parabola2 parabola) {
            return parabola.tangentAt(parameter);
        } else if (basisCurve instanceof CompositeCurve2 composite) {
            return composite.tangentAt(parameter);
        } else if (basisCurve instanceof TrimmedCurve2 trimmed) {
            return trimmed.tangentAt(parameter);
        }
        // Fallback: numerical differentiation using the given parameter
        double eps = 1e-6;
        Point2 p1 = pointOnBasisCurve(parameter - eps);
        Point2 p2 = pointOnBasisCurve(parameter + eps);
        Vector2 tangent = p2.subtract(p1);
        if (tangent.normSquared() <= Epsilon.EPS) {
            return new Vector2(1, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the closest point on the trimmed curve to a given point.
     * Uses sampling for initial guess then Newton-Raphson refinement when available.
     *
     * @param point target point
     * @return closest point on the trimmed curve
     */
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        double tMin = Math.min(trimParamStart, trimParamEnd);
        double tMax = Math.max(trimParamStart, trimParamEnd);

        // Sample the trimmed curve at increasing resolution to find initial guess
        Point2 closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        double bestT = tMin;
        for (int res : new int[]{16, 32, 64}) {
            java.util.List<Point2> samples = sample(res);
            for (int i = 0; i < samples.size(); i++) {
                double d = point.distanceTo(samples.get(i));
                if (d < minDist) {
                    minDist = d;
                    closest = samples.get(i);
                    double localT = (double) i / (samples.size() - 1);
                    bestT = senseAgreement
                            ? trimParamStart + (trimParamEnd - trimParamStart) * localT
                            : trimParamEnd + (trimParamStart - trimParamEnd) * localT;
                }
            }
        }

        // Newton-Raphson refinement on basis curve: minimize ||C(t) - P||^2
        double t = bestT;
        for (int iter = 0; iter < 20; iter++) {
            Point2 cp = pointOnBasisCurve(t);
            Vector2 residual = cp.subtract(point);
            Vector2 deriv = tangentOnBasisCurve(t);
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t += dt;
            t = Math.max(tMin, Math.min(tMax, t));
            if (Math.abs(dt) < 1e-12) break;
        }
        return pointOnBasisCurve(t);
    }

    /**
     * Returns the distance from a point to the trimmed curve.
     *
     * @param point target point
     * @return minimum distance to the trimmed curve
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the trimmed curve.
     *
     * @return midpoint
     */
    public Point2 midpoint() {
        return pointAt(0.5);
    }

    /**
     * Returns the underlying curve.
     *
     * @return basis curve
     */
    public Curve2 underlyingCurve() {
        return basisCurve;
    }
}
