package com.minicad.geometry2d;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Unit 2D direction.
 *
 * @param x x component
 * @param y y component
 */
public record Direction2(double x, double y) {

    public Direction2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        double norm = Math.sqrt(x * x + y * y);
        if (norm == 0.0) {
            throw new GeometryException("direction must not be zero");
        }
        x /= norm;
        y /= norm;
    }

    public static Direction2 from(Vector2 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new Direction2(vector.x(), vector.y());
    }

    public Vector2 asVector() {
        return new Vector2(x, y);
    }
}
