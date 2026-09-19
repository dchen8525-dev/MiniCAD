package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Minimal rational tensor-product B-spline surface.
 * Expanded knot vectors are cached after first use to avoid repeated allocations.
 * The parameter domain - degrees, control-point counts, natural domain in each
 * direction and the basis lookup at a parameter pair - lives in
 * {@link BSplineSurfaceDomain} and domain sampling, bounds and nearest-point search
 * are shared with {@link BSplineSurface3} through {@link BSplineSurfaceHelper}; only
 * the weighted point evaluation, the weighted normal and the weight-grid validation
 * are specific to this class.
 */
public final class RationalBSplineSurface3 implements SurfaceGeometry {

    private final List<List<CartesianPoint>> controlPoints;
    private final List<List<Double>> weightsData;
    private final BSplineSurfaceDomain domain;

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
        this.controlPoints = controlPoints.stream().map(List::copyOf).collect(Collectors.toList());
        this.weightsData = weightsData.stream().map(List::copyOf).collect(Collectors.toList());
        this.domain = BSplineSurfaceDomain.of(
                uDegree, vDegree, this.controlPoints, uKnots, uMultiplicities, vKnots, vMultiplicities);
        requireUsableWeights();
    }

    /**
     * Rejects a weight grid that cannot be paired with the control-point grid. The
     * control-point side of this is what {@link BSplineSurfaceDomain} already checked;
     * the weights are the part only this class has, so the check lives here rather than
     * in the shared domain.
     */
    private void requireUsableWeights() {
        if (weightsData.size() != controlPoints.size()) {
            throw new GeometryException("weight rows must match control-point rows");
        }
        int vCount = domain.vCount();
        for (int row = 0; row < controlPoints.size(); row++) {
            if (weightsData.get(row).size() != vCount) {
                throw new GeometryException("weight rows must have uniform length");
            }
            for (double weight : weightsData.get(row)) {
                if (!Double.isFinite(weight) || weight <= 0.0) {
                    throw new GeometryException("weights must be finite and positive");
                }
            }
        }
    }

    public int uDegree() { return domain.uDegree(); }
    public int vDegree() { return domain.vDegree(); }
    public List<List<CartesianPoint>> controlPoints() { return controlPoints; }
    public List<List<Double>> weightsData() { return weightsData; }
    public List<Integer> uMultiplicities() { return domain.uMultiplicities(); }
    public List<Integer> vMultiplicities() { return domain.vMultiplicities(); }
    public List<Double> uKnots() { return domain.uKnots(); }
    public List<Double> vKnots() { return domain.vKnots(); }

    // Java Bean getters
    public int getUDegree() { return domain.uDegree(); }
    public int getVDegree() { return domain.vDegree(); }
    public List<List<CartesianPoint>> getControlPoints() { return controlPoints; }
    public List<List<Double>> getWeightsData() { return weightsData; }
    public List<Integer> getUMultiplicities() { return domain.uMultiplicities(); }
    public List<Integer> getVMultiplicities() { return domain.vMultiplicities(); }
    public List<Double> getUKnots() { return domain.uKnots(); }
    public List<Double> getVKnots() { return domain.vKnots(); }

    public double uStart() {
        return domain.uStart();
    }

    public double uEnd() {
        return domain.uEnd();
    }

    public double vStart() {
        return domain.vStart();
    }

    public double vEnd() {
        return domain.vEnd();
    }

    public CartesianPoint pointAt(double u, double v) {
        int uDegree = domain.uDegree();
        int vDegree = domain.vDegree();
        BSplineSurfaceDomain.BasisAt basis = domain.basisAt(u, v);

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double denominator = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(basis.uIndex(i));
            List<Double> weightRow = weightsData.get(basis.uIndex(i));
            double bu = basis.uBasis(i);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = basis.vIndex(j);
                double weightedBasis = bu * basis.vBasis(j) * weightRow.get(vIndex);
                CartesianPoint control = row.get(vIndex);
                x += control.getX() * weightedBasis;
                y += control.getY() * weightedBasis;
                z += control.getZ() * weightedBasis;
                denominator += weightedBasis;
            }
        }
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("rational surface denominator is zero");
        }
        return new CartesianPoint(x / denominator, y / denominator, z / denominator);
    }

    public Vector3 normalAt(double u, double v) {
        int uDegree = domain.uDegree();
        int vDegree = domain.vDegree();
        BSplineSurfaceDomain.BasisAt basis = domain.basisAt(u, v);

        Vector3 A = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdv = new Vector3(0.0, 0.0, 0.0);
        double W = 0.0;
        double dWdu = 0.0;
        double dWdv = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(basis.uIndex(i));
            List<Double> weightRow = weightsData.get(basis.uIndex(i));
            double bu = basis.uBasis(i);
            double dBu = basis.uDerivative(i);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = basis.vIndex(j);
                double bv = basis.vBasis(j);
                double dBv = basis.vDerivative(j);
                double w = weightRow.get(vIndex);
                double weightedBasis = w * bu * bv;
                CartesianPoint cp = row.get(vIndex);
                Vector3 cpVec = new Vector3(cp.getX(), cp.getY(), cp.getZ());
                A = A.add(cpVec.scale(weightedBasis));
                dAdu = dAdu.add(cpVec.scale(w * dBu * bv));
                dAdv = dAdv.add(cpVec.scale(w * bu * dBv));
                W += weightedBasis;
                dWdu += w * dBu * bv;
                dWdv += w * bu * dBv;
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

    public List<List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        return BSplineSurfaceHelper.sampleGrid(
                uSegments, vSegments, uStart(), uEnd(), vStart(), vEnd(), this::pointAt);
    }

    public BoundingBox3 boundingBox() {
        return BSplineSurfaceHelper.boundingBoxOf(controlPoints);
    }

    public BoundingBox3 boundingBox(int uSegments, int vSegments) {
        return BSplineSurfaceHelper.boundingBoxOf(sampleGrid(uSegments, vSegments));
    }

    public CartesianPoint closestPointTo(CartesianPoint point) {
        return BSplineSurfaceHelper.closestPointTo(point, uStart(), uEnd(), vStart(), vEnd(), this::pointAt);
    }

    public double distanceTo(CartesianPoint point) {
        return BSplineSurfaceHelper.distanceTo(point, uStart(), uEnd(), vStart(), vEnd(), this::pointAt);
    }
}
