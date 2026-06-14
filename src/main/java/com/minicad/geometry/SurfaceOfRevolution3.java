package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of revolution representation.
 *
 * @param sweptCurve generatrix curve
 * @param axisOrigin point on revolution axis
 * @param axisDirection revolution axis direction
 */
/**
 * Minimal surface of revolution representation.
 *
 * @param sweptCurve generatrix curve
 * @param axisOrigin point on revolution axis
 * @param axisDirection revolution axis direction
 */
public final class SurfaceOfRevolution3 implements SurfaceGeometry {
    private final Curve3 sweptCurve;
    private final CartesianPoint axisOrigin;
    private final Direction3 axisDirection;

    public SurfaceOfRevolution3(Curve3 sweptCurve, CartesianPoint axisOrigin, Direction3 axisDirection) {
        this.sweptCurve = sweptCurve;
        this.axisOrigin = axisOrigin;
        this.axisDirection = axisDirection;
    }

    public Curve3 getSweptCurve() {
        return sweptCurve;
    }

    public CartesianPoint getAxisOrigin() {
        return axisOrigin;
    }

    public Direction3 getAxisDirection() {
        return axisDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceOfRevolution3 that = (SurfaceOfRevolution3) o;
        return Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(axisOrigin, that.axisOrigin) && Objects.equals(axisDirection, that.axisDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptCurve, axisOrigin, axisDirection);
    }

    @Override
    public String toString() {
        return "SurfaceOfRevolution3{" + "sweptCurve=" + sweptCurve + "axisOrigin=" + axisOrigin + "axisDirection=" + axisDirection + "}";
    }
}
