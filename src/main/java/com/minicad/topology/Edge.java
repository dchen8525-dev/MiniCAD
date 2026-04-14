package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
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

    private static final double IMPORT_CURVE_TOLERANCE = 1.0e-4;
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

    private static boolean liesOnCurve(CartesianPoint point, Curve3 curve) {
        if (curve.contains(point)) {
            return true;
        }
        List<CartesianPoint> sampled = sampleCurve(curve);
        if (sampled.size() < 2) {
            return false;
        }
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < sampled.size() - 1; index++) {
            double distance = distanceToSegment(point, sampled.get(index), sampled.get(index + 1));
            if (distance < nearestDistance) {
                nearestDistance = distance;
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
}
