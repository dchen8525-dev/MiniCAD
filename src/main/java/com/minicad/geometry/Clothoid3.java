package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D clothoid (Euler spiral / transition curve) representation.
 * The clothoid is defined by its curvature varying linearly with arc length.
 *
 * @param position clothoid placement (start point and local coordinate system)
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter (rate of curvature change per unit length)
 */
/**
 * Minimal 3D clothoid (Euler spiral / transition curve) representation.
 * The clothoid is defined by its curvature varying linearly with arc length.
 *
 * @param position clothoid placement (start point and local coordinate system)
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter (rate of curvature change per unit length)
 */
public final class Clothoid3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double xAxisIntercept;
    private final double curvature;

    public Clothoid3(Axis2Placement3D position, double xAxisIntercept, double curvature) {
        this.position = position;
        this.xAxisIntercept = xAxisIntercept;
        this.curvature = curvature;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getXAxisIntercept() {
        return xAxisIntercept;
    }

    public double getCurvature() {
        return curvature;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double xAxisIntercept() { return xAxisIntercept; }
    public double curvature() { return curvature; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothoid3 that = (Clothoid3) o;
        return Objects.equals(position, that.position) && xAxisIntercept == that.xAxisIntercept && curvature == that.curvature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, xAxisIntercept, curvature);
    }

    @Override
    public String toString() {
        return "Clothoid3{" + "position=" + position + "xAxisIntercept=" + xAxisIntercept + "curvature=" + curvature + "}";
    }
}