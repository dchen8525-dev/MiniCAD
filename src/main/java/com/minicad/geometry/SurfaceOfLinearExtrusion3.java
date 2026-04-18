package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
public record SurfaceOfLinearExtrusion3(Curve3 sweptCurve, Vector3 extrusionVector) implements SurfaceGeometry {

    public SurfaceOfLinearExtrusion3 {
        Preconditions.requireNonNull(sweptCurve, "sweptCurve");
        Preconditions.requireNonNull(extrusionVector, "extrusionVector");
        if (extrusionVector.norm() <= Epsilon.EPS) {
            throw new GeometryException("extrusionVector must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the surface of linear extrusion.
     *
     * @param curveParameter parameter along the swept curve
     * @param extrusionParameter parameter along the extrusion (0 to 1)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double curveParameter, double extrusionParameter) {
        Preconditions.requireFinite(curveParameter, "curveParameter");
        Preconditions.requireFinite(extrusionParameter, "extrusionParameter");
        CartesianPoint curvePoint = getPointOnCurveInternal(sweptCurve, curveParameter);
        Vector3 offset = extrusionVector.scale(extrusionParameter);
        return curvePoint.add(offset);
    }

    /**
     * Gets a point on a curve at the given parameter.
     */
    private static CartesianPoint getPointOnCurveInternal(Curve3 curve, double parameter) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.pointAt(parameter);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.pointAt(parameter);
        } else if (curve instanceof Circle circle) {
            return circle.pointAt(parameter);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.pointAt(parameter);
        } else if (curve instanceof Line3 line) {
            return line.pointAt(parameter);
        } else if (curve instanceof Polyline3 polyline) {
            return getPointOnPolyline(polyline, parameter);
        } else if (curve instanceof CompositeCurve3 composite) {
            return getPointOnCompositeInternal(composite, parameter);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.pointAt(parameter);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.pointAt(parameter);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.pointAt(parameter);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return getPointOnCurveInternal(surfaceCurve.curve3d(), parameter);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.pointAt(parameter);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.pointAt(parameter);
        }
        throw new GeometryException("unsupported swept curve type for point evaluation");
    }

    private static CartesianPoint getPointOnPolyline(Polyline3 polyline, double parameter) {
        java.util.List<CartesianPoint> points = polyline.points();
        if (points.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        double localT = t - index;
        CartesianPoint p1 = points.get(index);
        CartesianPoint p2 = points.get(index + 1);
        return p1.add(p2.subtract(p1).scale(localT));
    }

    private static CartesianPoint getPointOnCompositeInternal(CompositeCurve3 composite, double parameter) {
        java.util.List<Curve3> segments = composite.segments();
        if (segments.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getPointOnCurveInternal(segments.get(index), localT);
    }

    /**
     * Samples a patch on the surface of linear extrusion.
     *
     * @param curveSegments number of segments along the swept curve
     * @param extrusionSegments number of segments along the extrusion
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int curveSegments, int extrusionSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(curveSegments + 1);
        java.util.List<CartesianPoint> curveSamples = sampleCurveInternal(sweptCurve, curveSegments);
        if (curveSamples.isEmpty()) {
            return java.util.List.of();
        }
        for (int i = 0; i < curveSamples.size(); i++) {
            CartesianPoint curvePoint = curveSamples.get(i);
            java.util.List<CartesianPoint> column = new java.util.ArrayList<>(extrusionSegments + 1);
            for (int j = 0; j <= extrusionSegments; j++) {
                double t = (double) j / extrusionSegments;
                column.add(curvePoint.add(extrusionVector.scale(t)));
            }
            grid.add(java.util.List.copyOf(column));
        }
        return java.util.List.copyOf(grid);
    }

    private static java.util.List<CartesianPoint> sampleCurveInternal(Curve3 curve, int segments) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.sample(segments);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.sample(segments);
        } else if (curve instanceof Circle circle) {
            return circle.sample(segments);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.sample(segments);
        } else if (curve instanceof Line3 line) {
            return line.sample(segments, 0.0, 1.0);
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        } else if (curve instanceof CompositeCurve3 composite) {
            return sampleCompositeCurveInternal(composite, segments);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.sample(segments);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.sample(segments);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.sample(segments);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleCurveInternal(surfaceCurve.curve3d(), segments);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.sample(segments);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.sample(segments);
        }
        return java.util.List.of();
    }

    private static java.util.List<CartesianPoint> sampleCompositeCurveInternal(CompositeCurve3 composite, int segments) {
        java.util.List<CartesianPoint> allPoints = new java.util.ArrayList<>();
        int segmentsPerSegment = Math.max(1, segments / composite.segments().size());
        for (Curve3 segment : composite.segments()) {
            java.util.List<CartesianPoint> segmentPoints = sampleCurveInternal(segment, segmentsPerSegment);
            allPoints.addAll(segmentPoints);
        }
        return java.util.List.copyOf(allPoints);
    }

    /**
     * Computes the normal at a given position on the extrusion surface.
     * The normal is perpendicular to both the curve tangent and extrusion direction.
     *
     * @param u parameter along the swept curve
     * @param v parameter along the extrusion (unused for normal)
     * @return unit normal vector (perpendicular to surface)
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        Vector3 extrusionDir = extrusionVector.normalize().asVector();
        Vector3 curveTangent = getCurveTangentAt(sweptCurve, u);
        Vector3 normal = curveTangent.cross(extrusionDir);
        if (normal.norm() <= Epsilon.EPS) {
            Vector3 fallback = extrusionDir.cross(new Vector3(1, 0, 0));
            if (fallback.norm() <= Epsilon.EPS) {
                fallback = extrusionDir.cross(new Vector3(0, 1, 0));
            }
            return fallback.normalize().asVector();
        }
        return normal.normalize().asVector();
    }

    private static Vector3 getCurveTangentAt(Curve3 curve, double parameter) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.tangentAt(parameter);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.tangentAt(parameter);
        } else if (curve instanceof Circle circle) {
            return circle.tangentAt(parameter);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.tangentAt(parameter);
        } else if (curve instanceof Line3 line) {
            return line.tangentAt(parameter);
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.tangentAt(parameter);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.tangentAt(parameter);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.tangentAt(parameter);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.tangentAt(parameter);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.tangentAt(parameter);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.tangentAt(parameter);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.tangentAt(parameter);
        } else if (curve instanceof CompositeCurve3 composite) {
            return composite.tangentAt(parameter);
        }
        // Fallback: numerical differentiation
        double eps = 0.001;
        CartesianPoint p1 = getPointOnCurveInternal(curve, Math.max(0, parameter - eps));
        CartesianPoint p2 = getPointOnCurveInternal(curve, parameter + eps);
        Vector3 tangent = p2.subtract(p1);
        if (tangent.norm() <= Epsilon.EPS) {
            return new Vector3(1, 0, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the bounding box for the extrusion surface.
     *
     * @return bounding box enclosing the extruded surface
     */
    public BoundingBox3 boundingBox() {
        // Get bounding box of swept curve
        BoundingBox3 curveBox = getCurveBoundingBox(sweptCurve);
        // Expand along extrusion vector
        CartesianPoint start = curveBox.minCorner();
        CartesianPoint end = start.add(extrusionVector);
        BoundingBox3 fullBox = curveBox.union(end);
        return fullBox;
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
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.curve3d().boundingBox();
        } else if (curve instanceof Line3 line) {
            return line.boundingBox(0.0, 1.0);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.boundingBox();
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.boundingBox();
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.boundingBox();
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.boundingBox();
        }
        // For other curves, sample and compute
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<CartesianPoint> samples = sampleCurveInternal(curve, 64);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the closest point on the extrusion surface to a given point.
     * Uses sampling approach for extrusion surfaces.
     *
     * @param point target point
     * @return approximate closest point on the extrusion surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Sample at multiple resolutions to find closest point
        for (int resolution : new int[]{16, 32, 64}) {
            java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(resolution, resolution);
            for (java.util.List<CartesianPoint> row : grid) {
                for (CartesianPoint sample : row) {
                    double dist = point.distanceTo(sample);
                    if (dist < minDistance) {
                        minDistance = dist;
                        closest = sample;
                    }
                }
            }
        }
        return closest != null ? closest : pointAt(0, 0);
    }

    /**
     * Returns the shortest distance from a point to the extrusion surface.
     *
     * @param point target point
     * @return approximate shortest distance to the extrusion surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}