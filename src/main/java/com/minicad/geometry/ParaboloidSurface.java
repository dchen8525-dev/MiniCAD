package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal paraboloid surface (rotationally symmetric).
 * Parametrized as z = (x^2 + y^2) / (4*f) in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param focalLength focal distance, must be positive
 */
/**
 * Minimal paraboloid surface (rotationally symmetric).
 * Parametrized as z = (x^2 + y^2) / (4*f) in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param focalLength focal distance, must be positive
 */
public final class ParaboloidSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double focalLength;

    public ParaboloidSurface(Axis2Placement3D position, double focalLength) {
        this.position = position;
        this.focalLength = focalLength;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getFocalLength() {
        return focalLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParaboloidSurface that = (ParaboloidSurface) o;
        return Objects.equals(position, that.position) && focalLength == that.focalLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, focalLength);
    }

    @Override
    public String toString() {
        return "ParaboloidSurface{" + "position=" + position + "focalLength=" + focalLength + "}";
    }
}
