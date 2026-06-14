package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Collection;
import java.util.Objects;

/**
 * 2D axis-aligned bounding box.
 */
/**
 * 2D axis-aligned bounding box.
 */
public final class BoundingBox2 {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    public BoundingBox2(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundingBox2 that = (BoundingBox2) o;
        return minX == that.minX && minY == that.minY && maxX == that.maxX && maxY == that.maxY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, maxX, maxY);
    }

    @Override
    public String toString() {
        return "BoundingBox2{" + "minX=" + minX + "minY=" + minY + "maxX=" + maxX + "maxY=" + maxY + "}";
    }
}