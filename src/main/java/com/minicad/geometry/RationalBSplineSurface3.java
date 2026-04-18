package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal rational tensor-product B-spline surface.
 *
 * @param uDegree degree in the U direction
 * @param vDegree degree in the V direction
 * @param controlPoints control-point grid indexed as [u][v]
 * @param weightsData weight grid aligned with control points
 * @param uMultiplicities U-knot multiplicities
 * @param vMultiplicities V-knot multiplicities
 * @param uKnots unique U-knot values
 * @param vKnots unique V-knot values
 */
public record RationalBSplineSurface3(
        int uDegree,
        int vDegree,
        List<List<CartesianPoint>> controlPoints,
        List<List<Double>> weightsData,
        List<Integer> uMultiplicities,
        List<Integer> vMultiplicities,
        List<Double> uKnots,
        List<Double> vKnots
) implements SurfaceGeometry {

    public RationalBSplineSurface3 {
        if (uDegree < 1 || vDegree < 1) {
            throw new GeometryException("surface degrees must be at least 1");
        }
        controlPoints = controlPoints.stream().map(List::copyOf).toList();
        weightsData = weightsData.stream().map(List::copyOf).toList();
        uMultiplicities = List.copyOf(uMultiplicities);
        vMultiplicities = List.copyOf(vMultiplicities);
        uKnots = List.copyOf(uKnots);
        vKnots = List.copyOf(vKnots);
        if (controlPoints.size() < uDegree + 1) {
            throw new GeometryException("U control-point count must be at least degree + 1");
        }
        int vCount = controlPoints.getFirst().size();
        if (vCount < vDegree + 1) {
            throw new GeometryException("V control-point count must be at least degree + 1");
        }
        if (weightsData.size() != controlPoints.size()) {
            throw new GeometryException("weight rows must match control-point rows");
        }
        for (int row = 0; row < controlPoints.size(); row++) {
            if (controlPoints.get(row).size() != vCount || weightsData.get(row).size() != vCount) {
                throw new GeometryException("control-point and weight rows must have uniform length");
            }
        }
        if (uMultiplicities.size() != uKnots.size() || vMultiplicities.size() != vKnots.size()) {
            throw new GeometryException("knot multiplicities and knot values must have matching sizes");
        }
    }

    public double uStart() {
        List<Double> expanded = expandedKnots(uKnots, uMultiplicities);
        return expanded.get(uDegree);
    }

    public double uEnd() {
        List<Double> expanded = expandedKnots(uKnots, uMultiplicities);
        return expanded.get(controlPoints.size());
    }

    public double vStart() {
        List<Double> expanded = expandedKnots(vKnots, vMultiplicities);
        return expanded.get(vDegree);
    }

    public double vEnd() {
        List<Double> expanded = expandedKnots(vKnots, vMultiplicities);
        return expanded.get(controlPoints.getFirst().size());
    }

    public CartesianPoint pointAt(double u, double v) {
        List<Double> uExpanded = expandedKnots(uKnots, uMultiplicities);
        List<Double> vExpanded = expandedKnots(vKnots, vMultiplicities);
        double clampedU = clamp(u, uExpanded.get(uDegree), uExpanded.get(controlPoints.size()));
        double clampedV = clamp(v, vExpanded.get(vDegree), vExpanded.get(controlPoints.getFirst().size()));

        int uCount = controlPoints.size();
        int vCount = controlPoints.getFirst().size();
        int uSpan = findSpan(uCount - 1, uDegree, clampedU, uExpanded);
        int vSpan = findSpan(vCount - 1, vDegree, clampedV, vExpanded);

        Vector3 numerator = new Vector3(0.0, 0.0, 0.0);
        double denominator = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            double nu = basisValue(ui, uDegree, clampedU, uExpanded);
            for (int j = 0; j <= vDegree; j++) {
                int vj = vSpan - vDegree + j;
                double nv = basisValue(vj, vDegree, clampedV, vExpanded);
                double weightedBasis = nu * nv * weightsData.get(ui).get(vj);
                CartesianPoint control = controlPoints.get(ui).get(vj);
                numerator = numerator.add(new Vector3(
                        control.x() * weightedBasis,
                        control.y() * weightedBasis,
                        control.z() * weightedBasis
                ));
                denominator += weightedBasis;
            }
        }
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("rational surface denominator is zero");
        }
        return new CartesianPoint(
                numerator.x() / denominator,
                numerator.y() / denominator,
                numerator.z() / denominator
        );
    }

    public Vector3 normalAt(double u, double v) {
        List<Double> uExpanded = expandedKnots(uKnots, uMultiplicities);
        List<Double> vExpanded = expandedKnots(vKnots, vMultiplicities);
        double clampedU = clamp(u, uStart(), uEnd());
        double clampedV = clamp(v, vStart(), vEnd());

        int uCount = controlPoints.size();
        int vCount = controlPoints.getFirst().size();

        int uSpan = findSpan(uCount - 1, uDegree, clampedU, uExpanded);
        int vSpan = findSpan(vCount - 1, vDegree, clampedV, vExpanded);

        // For rational surface S(u,v) = A(u,v) / W(u,v)
        // ∂S/∂u = (∂A/∂u * W - A * ∂W/∂u) / W^2
        Vector3 A = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdv = new Vector3(0.0, 0.0, 0.0);
        double W = 0.0;
        double dWdu = 0.0;
        double dWdv = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            double nu = basisValue(ui, uDegree, clampedU, uExpanded);
            double dNu = derivativeBasisValue(ui, uDegree, clampedU, uExpanded);
            for (int j = 0; j <= vDegree; j++) {
                int vj = vSpan - vDegree + j;
                double nv = basisValue(vj, vDegree, clampedV, vExpanded);
                double dNv = derivativeBasisValue(vj, vDegree, clampedV, vExpanded);
                double w = weightsData.get(ui).get(vj);
                double weightedBasis = w * nu * nv;
                CartesianPoint cp = controlPoints.get(ui).get(vj);
                Vector3 cpVec = new Vector3(cp.x(), cp.y(), cp.z());
                A = A.add(cpVec.scale(weightedBasis));
                dAdu = dAdu.add(cpVec.scale(w * dNu * nv));
                dAdv = dAdv.add(cpVec.scale(w * nu * dNv));
                W += weightedBasis;
                dWdu += w * dNu * nv;
                dWdv += w * nu * dNv;
            }
        }
        double W2 = W * W;
        if (Epsilon.isZero(W2)) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        Vector3 dSdu = dAdu.scale(W).subtract(A.scale(dWdu)).scale(1.0 / W2);
        Vector3 dSdv = dAdv.scale(W).subtract(A.scale(dWdv)).scale(1.0 / W2);
        Vector3 normal = dSdu.cross(dSdv);
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    /**
     * Computes the derivative of a B-spline basis function.
     */
    private static double derivativeBasisValue(int i, int degree, double parameter, List<Double> knots) {
        double left = 0.0;
        double right = 0.0;
        double leftDenom = knots.get(i + degree) - knots.get(i);
        if (!Epsilon.isZero(leftDenom)) {
            left = degree / leftDenom * basisValue(i, degree - 1, parameter, knots);
        }
        double rightDenom = knots.get(i + degree + 1) - knots.get(i + 1);
        if (!Epsilon.isZero(rightDenom)) {
            right = degree / rightDenom * basisValue(i + 1, degree - 1, parameter, knots);
        }
        return left - right;
    }

    public List<List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        int uCount = Math.max(uSegments, 1);
        int vCount = Math.max(vSegments, 1);
        List<List<CartesianPoint>> rows = new ArrayList<>(uCount + 1);
        for (int ui = 0; ui <= uCount; ui++) {
            double u = uStart() + (uEnd() - uStart()) * ui / uCount;
            List<CartesianPoint> row = new ArrayList<>(vCount + 1);
            for (int vi = 0; vi <= vCount; vi++) {
                double v = vStart() + (vEnd() - vStart()) * vi / vCount;
                row.add(pointAt(u, v));
            }
            rows.add(List.copyOf(row));
        }
        return List.copyOf(rows);
    }

    /**
     * Returns the approximate bounding box based on control points.
     * This is a conservative bound that may be larger than the actual surface extent.
     *
     * @return bounding box enclosing all control points
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (List<CartesianPoint> row : controlPoints) {
            for (CartesianPoint point : row) {
                box = box.union(point);
            }
        }
        return box;
    }

    /**
     * Returns a more accurate bounding box by sampling the surface.
     *
     * @param uSegments number of segments along U
     * @param vSegments number of segments along V
     * @return bounding box enclosing sampled surface points
     */
    public BoundingBox3 boundingBox(int uSegments, int vSegments) {
        BoundingBox3 box = BoundingBox3.empty();
        List<List<CartesianPoint>> samples = sampleGrid(uSegments, vSegments);
        for (List<CartesianPoint> row : samples) {
            for (CartesianPoint point : row) {
                box = box.union(point);
            }
        }
        return box;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    private static List<Double> expandedKnots(List<Double> knots, List<Integer> multiplicities) {
        List<Double> expanded = new ArrayList<>();
        for (int index = 0; index < knots.size(); index++) {
            for (int repeat = 0; repeat < multiplicities.get(index); repeat++) {
                expanded.add(knots.get(index));
            }
        }
        return List.copyOf(expanded);
    }

    private static int findSpan(int n, int degree, double parameter, List<Double> knots) {
        if (parameter >= knots.get(n + 1)) {
            return n;
        }
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (parameter < knots.get(mid) || parameter >= knots.get(mid + 1)) {
            if (parameter < knots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    private static double basisValue(int i, int degree, double parameter, List<Double> knots) {
        if (degree == 0) {
            if ((parameter >= knots.get(i) && parameter < knots.get(i + 1))
                    || (Epsilon.equals(parameter, knots.getLast()) && Epsilon.equals(parameter, knots.get(i + 1)))) {
                return 1.0;
            }
            return 0.0;
        }
        double leftDenominator = knots.get(i + degree) - knots.get(i);
        double rightDenominator = knots.get(i + degree + 1) - knots.get(i + 1);
        double left = Epsilon.isZero(leftDenominator)
                ? 0.0
                : (parameter - knots.get(i)) / leftDenominator * basisValue(i, degree - 1, parameter, knots);
        double right = Epsilon.isZero(rightDenominator)
                ? 0.0
                : (knots.get(i + degree + 1) - parameter) / rightDenominator * basisValue(i + 1, degree - 1, parameter, knots);
        return left + right;
    }

    /**
     * Returns the closest point on the surface to a given point using sampling.
     * For rational B-spline surfaces, this uses a grid sampling approach since analytical
     * inversion is complex.
     *
     * @param point target point
     * @return approximate closest point on the surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        // Sample at multiple resolutions to find closest point
        for (int resolution : new int[]{16, 32, 64}) {
            List<List<CartesianPoint>> grid = sampleGrid(resolution, resolution);
            for (List<CartesianPoint> row : grid) {
                for (CartesianPoint sample : row) {
                    double distance = point.distanceTo(sample);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = sample;
                    }
                }
            }
        }
        return closest != null ? closest : pointAt(uStart(), vStart());
    }

    /**
     * Returns the shortest distance from a point to the surface.
     *
     * @param point target point
     * @return approximate shortest distance to the surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}
