package com.minicad.geometry2d;

import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public final class Point2 {
    private final double x;
    private final double y;

    public Point2(double x, double y) {
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
        Point2 that = (Point2) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2{" + "x=" + x + "y=" + y + "}";
    }
}
