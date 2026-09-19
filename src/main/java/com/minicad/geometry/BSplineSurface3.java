package com.minicad.geometry;

import com.minicad.common.Epsilon;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Minimal non-rational tensor-product B-spline surface with knot multiplicities.
 * Expanded knot vectors are cached after first use to avoid repeated allocations.
 * The whole parameter domain - degrees, control-point counts, natural domain in each
 * direction and the basis lookup at a parameter pair - lives in
 * {@link BSplineSurfaceDomain}; domain sampling, bounds and nearest-point search are
 * shared with {@link RationalBSplineSurface3} through {@link BSplineSurfaceHelper}.
 */
public final class BSplineSurface3 implements SurfaceGeometry {

    private final List<List<CartesianPoint>> controlPoints;
    private final BSplineSurfaceDomain domain;

    public BSplineSurface3(
            int uDegree,
            int vDegree,
            List<List<CartesianPoint>> controlPoints,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots
    ) {
        this.controlPoints = controlPoints.stream().map(List::copyOf).collect(Collectors.toList());
        this.domain = BSplineSurfaceDomain.of(
                uDegree, vDegree, this.controlPoints, uKnots, uMultiplicities, vKnots, vMultiplicities);
    }

    public int uDegree() { return domain.uDegree(); }
    public int vDegree() { return domain.vDegree(); }
    public List<List<CartesianPoint>> controlPoints() { return controlPoints; }
    public List<Integer> uMultiplicities() { return domain.uMultiplicities(); }
    public List<Integer> vMultiplicities() { return domain.vMultiplicities(); }
    public List<Double> uKnots() { return domain.uKnots(); }
    public List<Double> vKnots() { return domain.vKnots(); }

    // Java Bean getters
    public int getUDegree() { return domain.uDegree(); }
    public int getVDegree() { return domain.vDegree(); }
    public List<List<CartesianPoint>> getControlPoints() { return controlPoints; }
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
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(basis.uIndex(i));
            double bu = basis.uBasis(i);
            for (int j = 0; j <= vDegree; j++) {
                double b = bu * basis.vBasis(j);
                CartesianPoint control = row.get(basis.vIndex(j));
                x += b * control.getX();
                y += b * control.getY();
                z += b * control.getZ();
            }
        }
        return new CartesianPoint(x, y, z);
    }

    public Vector3 normalAt(double u, double v) {
        int uDegree = domain.uDegree();
        int vDegree = domain.vDegree();
        BSplineSurfaceDomain.BasisAt basis = domain.basisAt(u, v);

        Vector3 dSdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dSdv = new Vector3(0.0, 0.0, 0.0);
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(basis.uIndex(i));
            double bu = basis.uBasis(i);
            double dBu = basis.uDerivative(i);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = basis.vIndex(j);
                CartesianPoint cp = row.get(vIndex);
                Vector3 cpVec = new Vector3(cp.getX(), cp.getY(), cp.getZ());
                double bv = basis.vBasis(j);
                double dBv = basis.vDerivative(j);
                dSdu = dSdu.add(cpVec.scale(dBu * bv));
                dSdv = dSdv.add(cpVec.scale(bu * dBv));
            }
        }

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
