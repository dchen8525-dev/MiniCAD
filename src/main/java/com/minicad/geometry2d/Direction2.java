package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Objects;

/**
 * Immutable unit 2D direction.
 *
 * @param x x component
 * @param y y component
 */
/**
 * Immutable unit 2D direction.
 *
 * @param x x component
 * @param y y component
 */
public final class Direction2 {
    private final double x;
    private final double y;

    public Direction2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction2 that = (Direction2) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Direction2{" + "x=" + x + "y=" + y + "}";
    }
}
