package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal rational tensor-product B-spline surface.
 * Expanded knot vectors are cached after first use to avoid repeated allocations.
 */
public final class RationalBSplineSurface3 implements SurfaceGeometry {

    private final int uDegree;
    private final int vDegree;
    private final List<List<CartesianPoint>> controlPoints;
    private final List<List<Double>> weightsData;
    private final List<Integer> uMultiplicities;
    private final List<Integer> vMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private volatile List<Double> uExpandedKnots;
    private volatile List<Double> vExpandedKnots;

    public RationalBSplineSurface3(
            int uDegree,
            int vDegree,
            List<List<CartesianPoint>> controlPoints,
            List<List<Double>> weightsData,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots
    ) {
        if (uDegree < 1 || vDegree < 1) {
            throw new GeometryException("surface degrees must be at least 1");
        }
        this.controlPoints = controlPoints.stream().map(List::copyOf).toList();
        this.weightsData = weightsData.stream().map(List::copyOf).toList();
        this.uMultiplicities = List.copyOf(uMultiplicities);
        this.vMultiplicities = List.copyOf(vMultiplicities);
        this.uKnots = List.copyOf(uKnots);
        this.vKnots = List.copyOf(vKnots);
        if (this.controlPoints.size() < uDegree + 1) {
            throw new GeometryException("U control-point count must be at least degree + 1");
        }
        int vCount = this.controlPoints.getFirst().size();
        if (vCount < vDegree + 1) {
            throw new GeometryException("V control-point count must be at least degree + 1");
        }
        if (this.weightsData.size() != this.controlPoints.size()) {
            throw new GeometryException("weight rows must match control-point rows");
        }
        for (int row = 0; row < this.controlPoints.size(); row++) {
            if (this.controlPoints.get(row).size() != vCount || this.weightsData.get(row).size() != vCount) {
                throw new GeometryException("control-point and weight rows must have uniform length");
            }
        }
        if (this.uMultiplicities.size() != this.uKnots.size() || this.vMultiplicities.size() != this.vKnots.size()) {
            throw new GeometryException("knot multiplicities and knot values must have matching sizes");
        }
        this.uDegree = uDegree;
        this.vDegree = vDegree;
    }

    public int uDegree() { return uDegree; }
    public int vDegree() { return vDegree; }
    public List<List<CartesianPoint>> controlPoints() { return controlPoints; }
    public List<List<Double>> weightsData() { return weightsData; }
    public List<Integer> uMultiplicities() { return uMultiplicities; }
    public List<Integer> vMultiplicities() { return vMultiplicities; }
    public List<Double> uKnots() { return uKnots; }
    public List<Double> vKnots() { return vKnots; }

    private List<Double> uExpanded() {
        List<Double> local = uExpandedKnots;
        if (local == null) {
            local = expandedKnots(uKnots, uMultiplicities);
            uExpandedKnots = local;
        }
        return local;
    }

    private List<Double> vExpanded() {
        List<Double> local = vExpandedKnots;
        if (local == null) {
            local = expandedKnots(vKnots, vMultiplicities);
            vExpandedKnots = local;
        }
        return local;
    }

    public double uStart() {
        return uExpanded().get(uDegree);
    }

    public double uEnd() {
        return uExpanded().get(controlPoints.size());
    }

    public double vStart() {
        return vExpanded().get(vDegree);
    }

    public double vEnd() {
        return vExpanded().get(controlPoints.getFirst().size());
    }

    public CartesianPoint pointAt(double u, double v) {
        List<Double> uExp = uExpanded();
        List<Double> vExp = vExpanded();
        double clampedU = clamp(u, uExp.get(uDegree), uExp.get(controlPoints.size()));
        double clampedV = clamp(v, vExp.get(vDegree), vExp.get(controlPoints.getFirst().size()));

        int uCount = controlPoints.size();
        int vCount = controlPoints.getFirst().size();
        int uSpan = findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = findSpan(vCount - 1, vDegree, clampedV, vExp);

        Vector3 numerator = new Vector3(0.0, 0.0, 0.0);
        double denominator = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            double nu = basisValue(ui, uDegree, clampedU, uExp);
            for (int j = 0; j <= vDegree; j++) {
                int vj = vSpan - vDegree + j;
                double nv = basisValue(vj, vDegree, clampedV, vExp);
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
        List<Double> uExp = uExpanded();
        List<Double> vExp = vExpanded();
        double clampedU = clamp(u, uStart(), uEnd());
        double clampedV = clamp(v, vStart(), vEnd());

        int uCount = controlPoints.size();
        int vCount = controlPoints.getFirst().size();

        int uSpan = findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = findSpan(vCount - 1, vDegree, clampedV, vExp);

        Vector3 A = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdv = new Vector3(0.0, 0.0, 0.0);
        double W = 0.0;
        double dWdu = 0.0;
        double dWdv = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            double nu = basisValue(ui, uDegree, clampedU, uExp);
            double dNu = derivativeBasisValue(ui, uDegree, clampedU, uExp);
            for (int j = 0; j <= vDegree; j++) {
                int vj = vSpan - vDegree + j;
                double nv = basisValue(vj, vDegree, clampedV, vExp);
                double dNv = derivativeBasisValue(vj, vDegree, clampedV, vExp);
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
        double uRange = uEnd() - uStart();
        double vRange = vEnd() - vStart();
        for (int ui = 0; ui <= uCount; ui++) {
            double u = uStart() + uRange * ui / uCount;
            List<CartesianPoint> row = new ArrayList<>(vCount + 1);
            for (int vi = 0; vi <= vCount; vi++) {
                double v = vStart() + vRange * vi / vCount;
                row.add(pointAt(u, v));
            }
            rows.add(List.copyOf(row));
        }
        return List.copyOf(rows);
    }

    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (List<CartesianPoint> row : controlPoints) {
            for (CartesianPoint point : row) {
                box = box.union(point);
            }
        }
        return box;
    }

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
            int m = multiplicities.get(index);
            double k = knots.get(index);
            for (int repeat = 0; repeat < m; repeat++) {
                expanded.add(k);
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

    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

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

    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}
