package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;

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
        double du = Math.max((uEnd() - uStart()) / 200.0, 1.0e-4);
        double dv = Math.max((vEnd() - vStart()) / 200.0, 1.0e-4);
        CartesianPoint p = pointAt(u, v);
        CartesianPoint pu = pointAt(clamp(u + du, uStart(), uEnd()), v);
        CartesianPoint pv = pointAt(u, clamp(v + dv, vStart(), vEnd()));
        Vector3 tangentU = pu.subtract(p);
        Vector3 tangentV = pv.subtract(p);
        Vector3 normal = tangentU.cross(tangentV);
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
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
}
