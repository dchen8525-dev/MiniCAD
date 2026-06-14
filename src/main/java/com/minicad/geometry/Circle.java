package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
public final class Circle implements Curve3 {
    private final Axis2Placement3D position;
    private final double radius;

    public Circle(Axis2Placement3D position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle that = (Circle) o;
        return Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius);
    }

    @Override
    public String toString() {
        return "Circle{" + "position=" + position + "radius=" + radius + "}";
    }
}
