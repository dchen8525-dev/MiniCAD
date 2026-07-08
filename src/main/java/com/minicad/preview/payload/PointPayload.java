package com.minicad.preview.payload;

/**
 * 3D point payload for preview geometry.
 */
public final class PointPayload {
    private final double x;
    private final double y;
    private final double z;

    public PointPayload(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    // Record-style accessors
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointPayload that = (PointPayload) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(x), Double.hashCode(y), Double.hashCode(z));
    }

    @Override
    public String toString() {
        return "PointPayload{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}