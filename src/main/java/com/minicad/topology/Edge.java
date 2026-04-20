package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal topological edge backed by a supported 3D curve.
 *
 * @param start start vertex
 * @param end end vertex
 * @param curve underlying curve geometry
 * @param sameSense whether the topological direction matches the curve direction
 */
public record Edge(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {

    private static final double IMPORT_CURVE_TOLERANCE = 1.0e-2;
    private static final int IMPORT_CURVE_SAMPLES = 512;

    /**
     * Creates an edge and validates its invariants.
     */
    public Edge {
        if (start == null) {
            throw new TopologyException("start must not be null");
        }
        if (end == null) {
            throw new TopologyException("end must not be null");
        }
        if (curve == null) {
            throw new TopologyException("curve must not be null");
        }
        boolean coincidentVertices = start.point().distanceTo(end.point()) <= Epsilon.EPS;
        if (coincidentVertices && !isClosedCurve(curve)) {
            throw new TopologyException("edge must have distinct vertices");
        }
        if (!liesOnCurve(start.point(), curve)) {
            throw new TopologyException("start vertex must lie on edge curve");
        }
        if (!liesOnCurve(end.point(), curve)) {
            throw new TopologyException("end vertex must lie on edge curve");
        }
    }

    /**
     * Returns the bounding box of the edge.
     *
     * @return bounding box enclosing the edge vertices
     */
    public BoundingBox3 boundingBox() {
        return BoundingBox3.of(start.point(), end.point());
    }

    /**
     * Returns the approximate length of the edge.
     *
     * @return approximate length
     */
    public double length() {
        if (start.point().distanceTo(end.point()) <= Epsilon.EPS) {
            // Closed curve - return full curve length
            if (curve instanceof Circle circle) {
                return circle.circumference();
            } else if (curve instanceof Ellipse3 ellipse) {
                return ellipse.perimeter();
            }
        }
        // For non-closed curves, use the distance between start and end
        // This is the most reliable for Line edges
        if (curve instanceof Line3) {
            return start.point().distanceTo(end.point());
        }
        // For other curves, approximate using samples
        List<CartesianPoint> samples = sampleCurve(curve);
        double totalLength = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            totalLength += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Gets bounding box from curve.
     */
    private static BoundingBox3 getCurveBoundingBox(Curve3 curve) {
        if (curve instanceof Circle circle) {
            return circle.boundingBox();
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.boundingBox();
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.boundingBox();
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.boundingBox();
        } else if (curve instanceof BSplineCurve3 bspline) {
            return bspline.boundingBox();
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.boundingBox();
        } else if (curve instanceof CompositeCurve3 composite) {
            return composite.boundingBox();
        } else if (curve instanceof Line3 line) {
            return BoundingBox3.of(line.origin(), line.pointAt(1.0));
        }
        // For other curves, sample and compute
        BoundingBox3 box = BoundingBox3.empty();
        List<CartesianPoint> samples = sampleCurve(curve);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    private static boolean liesOnCurve(CartesianPoint point, Curve3 curve) {
        // For B-spline and rational B-spline curves, use the curve's own
        // closestPointTo (Newton-Raphson) which is much more accurate than
        // sample-based linear interpolation.
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.distanceTo(point) <= IMPORT_CURVE_TOLERANCE;
        }
        if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.distanceTo(point) <= IMPORT_CURVE_TOLERANCE;
        }
        // For wrapped curves, delegate to the underlying curve
        if (curve instanceof TrimmedCurve3 trimmed) {
            return liesOnCurve(point, trimmed.basisCurve());
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return liesOnCurve(point, surfaceCurve.curve3d());
        }
        // Fast path: try the curve's own contains() check first.
        if (curve.contains(point)) {
            return true;
        }
        // Industrial STEP files often have vertex coordinates rounded to limited
        // precision, so use a tolerant distance check for all remaining curve types.
        double distance = curve.distanceTo(point);
        if (distance <= IMPORT_CURVE_TOLERANCE) {
            return true;
        }
        // Fall back to sample-based check for complex curves
        List<CartesianPoint> sampled = sampleCurve(curve);
        if (sampled.size() < 2) {
            return false;
        }
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < sampled.size() - 1; index++) {
            double dist = distanceToSegment(point, sampled.get(index), sampled.get(index + 1));
            if (dist < nearestDistance) {
                nearestDistance = dist;
            }
        }
        return nearestDistance <= IMPORT_CURVE_TOLERANCE;
    }

    private static List<CartesianPoint> sampleCurve(Curve3 curve) {
        if (curve instanceof Line3 line) {
            return List.of(line.origin(), line.pointAt(1.0));
        }
        if (curve instanceof Circle circle) {
            List<CartesianPoint> samples = new ArrayList<>(IMPORT_CURVE_SAMPLES + 1);
            for (int index = 0; index <= IMPORT_CURVE_SAMPLES; index++) {
                samples.add(circle.pointAt(Math.PI * 2.0 * index / IMPORT_CURVE_SAMPLES));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof Ellipse3 ellipse) {
            List<CartesianPoint> samples = new ArrayList<>(IMPORT_CURVE_SAMPLES + 1);
            for (int index = 0; index <= IMPORT_CURVE_SAMPLES; index++) {
                samples.add(ellipse.pointAt(Math.PI * 2.0 * index / IMPORT_CURVE_SAMPLES));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof BSplineCurve3 spline) {
            return spline.sample(IMPORT_CURVE_SAMPLES);
        }
        if (curve instanceof RationalBSplineCurve3 spline) {
            return spline.sample(IMPORT_CURVE_SAMPLES);
        }
        if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        }
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleCurve(trimmedCurve.basisCurve());
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleCurve(surfaceCurve.curve3d());
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> samples = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentSamples = sampleCurve(segment);
                int startIndex = first ? 0 : 1;
                for (int index = startIndex; index < segmentSamples.size(); index++) {
                    samples.add(segmentSamples.get(index));
                }
                first = false;
            }
            return List.copyOf(samples);
        }
        return List.of();
    }

    private static double distanceToSegment(CartesianPoint point, CartesianPoint start, CartesianPoint end) {
        double dx = end.x() - start.x();
        double dy = end.y() - start.y();
        double dz = end.z() - start.z();
        double lengthSquared = dx * dx + dy * dy + dz * dz;
        if (lengthSquared <= Epsilon.EPS) {
            return point.distanceTo(start);
        }
        double t = ((point.x() - start.x()) * dx + (point.y() - start.y()) * dy + (point.z() - start.z()) * dz) / lengthSquared;
        double clamped = Math.max(0.0, Math.min(1.0, t));
        CartesianPoint nearest = new CartesianPoint(
                start.x() + dx * clamped,
                start.y() + dy * clamped,
                start.z() + dz * clamped
        );
        return point.distanceTo(nearest);
    }

    private static boolean isClosedCurve(Curve3 curve) {
        if (curve instanceof Circle || curve instanceof Ellipse3) {
            return true;
        }
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return isClosedCurve(trimmedCurve.basisCurve());
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return isClosedCurve(surfaceCurve.curve3d());
        }
        return false;
    }

    /**
     * Returns samples along the edge curve.
     *
     * @param segments number of segments to sample
     * @return list of sampled points along the curve
     */
    public List<CartesianPoint> sample(int segments) {
        int safeSegments = Math.max(1, segments);
        if (curve instanceof Line3) {
            List<CartesianPoint> samples = new ArrayList<>(safeSegments + 1);
            for (int i = 0; i <= safeSegments; i++) {
                double t = (double) i / safeSegments;
                samples.add(start.point().interpolate(end.point(), t));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof Circle circle) {
            double startAngle = circle.parameterAt(start.point());
            double endAngle = circle.parameterAt(end.point());
            double delta = sameSense ? endAngle - startAngle : startAngle - endAngle;
            if (sameSense && delta < 0.0) {
                delta += Math.PI * 2.0;
            } else if (!sameSense && delta < 0.0) {
                delta += Math.PI * 2.0;
            }
            if (start.point().distanceTo(end.point()) <= Epsilon.EPS) {
                delta = Math.PI * 2.0;
            }
            List<CartesianPoint> samples = new ArrayList<>(safeSegments + 1);
            for (int i = 0; i <= safeSegments; i++) {
                double fraction = (double) i / safeSegments;
                double angle = sameSense
                        ? startAngle + delta * fraction
                        : startAngle - delta * fraction;
                samples.add(circle.pointAt(angle));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof Ellipse3 ellipse) {
            double startAngle = ellipse.parameterAt(start.point());
            double endAngle = ellipse.parameterAt(end.point());
            double delta = sameSense ? endAngle - startAngle : startAngle - endAngle;
            if (sameSense && delta < 0.0) {
                delta += Math.PI * 2.0;
            } else if (!sameSense && delta < 0.0) {
                delta += Math.PI * 2.0;
            }
            if (start.point().distanceTo(end.point()) <= Epsilon.EPS) {
                delta = Math.PI * 2.0;
            }
            List<CartesianPoint> samples = new ArrayList<>(safeSegments + 1);
            for (int i = 0; i <= safeSegments; i++) {
                double fraction = (double) i / safeSegments;
                double angle = sameSense
                        ? startAngle + delta * fraction
                        : startAngle - delta * fraction;
                samples.add(ellipse.pointAt(angle));
            }
            return List.copyOf(samples);
        }
        return sampleCurveWithSegments(curve, safeSegments);
    }

    /**
     * Returns the midpoint of the edge.
     *
     * @return midpoint of the edge
     */
    public CartesianPoint midpoint() {
        List<CartesianPoint> samples = sample(100);
        if (samples.size() < 2) {
            return start.point().midpoint(end.point());
        }
        // Find midpoint by cumulative distance
        double halfLength = length() / 2.0;
        double cumulative = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            double segmentLength = samples.get(i).distanceTo(samples.get(i + 1));
            if (cumulative + segmentLength >= halfLength) {
                double t = (halfLength - cumulative) / segmentLength;
                return samples.get(i).interpolate(samples.get(i + 1), t);
            }
            cumulative += segmentLength;
        }
        return samples.get(samples.size() - 1);
    }

    /**
     * Returns the closest point on the edge to a given point.
     *
     * @param point target point
     * @return closest point on the edge
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        List<CartesianPoint> samples = sample(256);
        CartesianPoint closest = samples.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the edge.
     *
     * @param point target point
     * @return minimum distance to the edge
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Checks if a point lies on the edge within tolerance.
     *
     * @param point point to check
     * @return true if point lies on the edge
     */
    public boolean contains(CartesianPoint point) {
        return distanceTo(point) <= IMPORT_CURVE_TOLERANCE;
    }

    private static List<CartesianPoint> sampleCurveWithSegments(Curve3 curve, int segments) {
        if (curve instanceof Line3 line) {
            return List.of(line.origin(), line.pointAt(1.0));
        }
        if (curve instanceof Circle circle) {
            List<CartesianPoint> samples = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                samples.add(circle.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof Ellipse3 ellipse) {
            List<CartesianPoint> samples = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                samples.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(samples);
        }
        if (curve instanceof BSplineCurve3 spline) {
            return spline.sample(segments);
        }
        if (curve instanceof RationalBSplineCurve3 spline) {
            return spline.sample(segments);
        }
        if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        }
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleCurveWithSegments(trimmedCurve.basisCurve(), segments);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleCurveWithSegments(surfaceCurve.curve3d(), segments);
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> samples = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentSamples = sampleCurveWithSegments(segment, segments / compositeCurve.segments().size());
                int startIndex = first ? 0 : 1;
                for (int index = startIndex; index < segmentSamples.size(); index++) {
                    samples.add(segmentSamples.get(index));
                }
                first = false;
            }
            return List.copyOf(samples);
        }
        return List.of();
    }
}
