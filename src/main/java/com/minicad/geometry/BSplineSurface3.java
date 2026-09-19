package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Minimal non-rational tensor-product B-spline surface with knot multiplicities.
 * Expanded knot vectors are cached after first use to avoid repeated allocations.
 * Domain sampling, bounds and nearest-point search are shared with
 * {@link RationalBSplineSurface3} through {@link BSplineSurfaceHelper}.
 */
public final class BSplineSurface3 implements SurfaceGeometry {

    private final int uDegree;
    private final int vDegree;
    private final List<List<CartesianPoint>> controlPoints;
    private final KnotVector uKnotVector;
    private final KnotVector vKnotVector;

    public BSplineSurface3(
            int uDegree,
            int vDegree,
            List<List<CartesianPoint>> controlPoints,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots
    ) {
        if (uDegree < 1 || vDegree < 1) {
            throw new GeometryException("surface degrees must be at least 1");
        }
        this.controlPoints = controlPoints.stream().map(List::copyOf).collect(Collectors.toList());
        if (this.controlPoints.size() < uDegree + 1) {
            throw new GeometryException("U control-point count must be at least degree + 1");
        }
        int vCount = this.controlPoints.get(0).size();
        if (vCount < vDegree + 1) {
            throw new GeometryException("V control-point count must be at least degree + 1");
        }
        for (List<CartesianPoint> row : this.controlPoints) {
            if (row.size() != vCount) {
                throw new GeometryException("control-point rows must have uniform length");
            }
        }
        if (uMultiplicities.size() != uKnots.size() || vMultiplicities.size() != vKnots.size()) {
            throw new GeometryException("knot multiplicities and knot values must have matching sizes");
        }
        BSplineSurfaceHelper.validateKnots(uDegree, this.controlPoints.size(), uKnots, uMultiplicities);
        BSplineSurfaceHelper.validateKnots(vDegree, vCount, vKnots, vMultiplicities);
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.uKnotVector = new KnotVector(uKnots, uMultiplicities);
        this.vKnotVector = new KnotVector(vKnots, vMultiplicities);
    }

    public int uDegree() { return uDegree; }
    public int vDegree() { return vDegree; }
    public List<List<CartesianPoint>> controlPoints() { return controlPoints; }
    public List<Integer> uMultiplicities() { return uKnotVector.multiplicities(); }
    public List<Integer> vMultiplicities() { return vKnotVector.multiplicities(); }
    public List<Double> uKnots() { return uKnotVector.knots(); }
    public List<Double> vKnots() { return vKnotVector.knots(); }

    // Java Bean getters
    public int getUDegree() { return uDegree; }
    public int getVDegree() { return vDegree; }
    public List<List<CartesianPoint>> getControlPoints() { return controlPoints; }
    public List<Integer> getUMultiplicities() { return uKnotVector.multiplicities(); }
    public List<Integer> getVMultiplicities() { return vKnotVector.multiplicities(); }
    public List<Double> getUKnots() { return uKnotVector.knots(); }
    public List<Double> getVKnots() { return vKnotVector.knots(); }

    public double uStart() {
        return uKnotVector.expandedStart(uDegree);
    }

    public double uEnd() {
        return uKnotVector.expandedEnd(controlPoints.size());
    }

    public double vStart() {
        return vKnotVector.expandedStart(vDegree);
    }

    public double vEnd() {
        return vKnotVector.expandedEnd(controlPoints.get(0).size());
    }

    public CartesianPoint pointAt(double u, double v) {
        List<Double> uExp = uKnotVector.expanded();
        List<Double> vExp = vKnotVector.expanded();
        double clampedU = BSplineKernel.clamp(u, uExp.get(uDegree), uExp.get(controlPoints.size()));
        double clampedV = BSplineKernel.clamp(v, vExp.get(vDegree), vExp.get(controlPoints.get(0).size()));

        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();

        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            List<CartesianPoint> row = controlPoints.get(ui);
            double bu = nu[i];
            for (int j = 0; j <= vDegree; j++) {
                double b = bu * nv[j];
                CartesianPoint control = row.get(vj(vSpan, vDegree, j));
                x += b * control.getX();
                y += b * control.getY();
                z += b * control.getZ();
            }
        }
        return new CartesianPoint(x, y, z);
    }

    private static int vj(int vSpan, int vDegree, int j) {
        return vSpan - vDegree + j;
    }

    public Vector3 normalAt(double u, double v) {
        List<Double> uExp = uKnotVector.expanded();
        List<Double> vExp = vKnotVector.expanded();
        double clampedU = BSplineKernel.clamp(u, uStart(), uEnd());
        double clampedV = BSplineKernel.clamp(v, vStart(), vEnd());

        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();

        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        Vector3 dSdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dSdv = new Vector3(0.0, 0.0, 0.0);
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            List<CartesianPoint> row = controlPoints.get(ui);
            double bu = nu[i];
            double dBu = BSplineKernel.derivativeBasisValue(uSpan - uDegree + i, uDegree, clampedU, uExp);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = vj(vSpan, vDegree, j);
                CartesianPoint cp = row.get(vIndex);
                Vector3 cpVec = new Vector3(cp.getX(), cp.getY(), cp.getZ());
                double bv = nv[j];
                double dBv = BSplineKernel.derivativeBasisValue(vIndex, vDegree, clampedV, vExp);
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
