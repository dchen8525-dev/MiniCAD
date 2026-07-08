package com.minicad.preview.payload;

/**
 * UV point for parametric surface coordinates.
 */
public final class UvPoint {
    private final double u;
    private final double v;

    public UvPoint(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public double getU() {
        return u;
    }
    public double getV() {
        return v;
    }

    // Record-style accessors
    public double u() { return u; }
    public double v() { return v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UvPoint that = (UvPoint) o;
        return Double.compare(that.u, u) == 0 && Double.compare(that.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(u), Double.hashCode(v));
    }

    @Override
    public String toString() {
        return "UvPoint{u=" + u + ", v=" + v + "}";
    }
}