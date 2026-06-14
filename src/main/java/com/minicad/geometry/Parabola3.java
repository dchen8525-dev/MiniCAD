package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D parabola representation.
 * A parabola is a conic section defined by a focus and directrix, or parametrically.
 *
 * @param position parabola placement (vertex at origin, axis along local Y)
 * @param focalDistance distance from vertex to focus
 */
/**
 * Minimal 3D parabola representation.
 * A parabola is a conic section defined by a focus and directrix, or parametrically.
 *
 * @param position parabola placement (vertex at origin, axis along local Y)
 * @param focalDistance distance from vertex to focus
 */
public final class Parabola3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double focalDistance;

    public Parabola3(Axis2Placement3D position, double focalDistance) {
        this.position = position;
        this.focalDistance = focalDistance;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getFocalDistance() {
        return focalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parabola3 that = (Parabola3) o;
        return Objects.equals(position, that.position) && focalDistance == that.focalDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, focalDistance);
    }

    @Override
    public String toString() {
        return "Parabola3{" + "position=" + position + "focalDistance=" + focalDistance + "}";
    }
}