package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
public final class SurfaceOfLinearExtrusion3 implements SurfaceGeometry {
    private final Curve3 sweptCurve;
    private final Vector3 extrusionVector;

    public SurfaceOfLinearExtrusion3(Curve3 sweptCurve, Vector3 extrusionVector) {
        this.sweptCurve = sweptCurve;
        this.extrusionVector = extrusionVector;
    }

    public Curve3 getSweptCurve() {
        return sweptCurve;
    }

    public Vector3 getExtrusionVector() {
        return extrusionVector;
    }

    // Record-style accessors
    public Curve3 sweptCurve() { return getSweptCurve(); }
    public Vector3 extrusionVector() { return getExtrusionVector(); }

    /**
     * Returns a point on the surface of linear extrusion at the given parametric coordinates.
     *
     * @param u parameter along the swept curve
     * @param v extrusion parameter (0 = base, 1 = full extrusion)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint curvePoint = sweptCurve.pointAt(u);
        return curvePoint.add(extrusionVector.scale(v));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceOfLinearExtrusion3 that = (SurfaceOfLinearExtrusion3) o;
        return Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(extrusionVector, that.extrusionVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptCurve, extrusionVector);
    }

    @Override
    public String toString() {
        return "SurfaceOfLinearExtrusion3{" + "sweptCurve=" + sweptCurve + "extrusionVector=" + extrusionVector + "}";
    }
}
