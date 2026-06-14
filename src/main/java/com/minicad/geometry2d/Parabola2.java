package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D parabola representation.
 * A parabola is a conic section defined by a focus and directrix.
 *
 * @param vertex parabola vertex
 * @param axisDirection axis direction (from vertex towards focus)
 * @param focalDistance distance from vertex to focus
 */
/**
 * Minimal 2D parabola representation.
 * A parabola is a conic section defined by a focus and directrix.
 *
 * @param vertex parabola vertex
 * @param axisDirection axis direction (from vertex towards focus)
 * @param focalDistance distance from vertex to focus
 */
public final class Parabola2 implements Curve2 {
    private final Point2 vertex;
    private final Direction2 axisDirection;
    private final double focalDistance;

    public Parabola2(Point2 vertex, Direction2 axisDirection, double focalDistance) {
        this.vertex = vertex;
        this.axisDirection = axisDirection;
        this.focalDistance = focalDistance;
    }

    public Point2 getVertex() {
        return vertex;
    }

    public Direction2 getAxisDirection() {
        return axisDirection;
    }

    public double getFocalDistance() {
        return focalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parabola2 that = (Parabola2) o;
        return Objects.equals(vertex, that.vertex) && Objects.equals(axisDirection, that.axisDirection) && focalDistance == that.focalDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex, axisDirection, focalDistance);
    }

    @Override
    public String toString() {
        return "Parabola2{" + "vertex=" + vertex + "axisDirection=" + axisDirection + "focalDistance=" + focalDistance + "}";
    }
}