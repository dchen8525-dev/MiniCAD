package com.minicad.preview.payload;

import java.util.List;

/**
 * Parametric loop payload for UV coordinates.
 */
public final class ParametricLoopPayload {
    private final boolean outer;
    private final List<UvPoint> points;

    public ParametricLoopPayload(boolean outer, List<UvPoint> points) {
        this.outer = outer;
        this.points = PreviewPayloadCopies.copy(points);
    }

    public boolean getOuter() {
        return outer;
    }
    public List<UvPoint> getPoints() {
        return points;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public List<UvPoint> points() { return points; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametricLoopPayload that = (ParametricLoopPayload) o;
        return outer == that.outer && java.util.Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), points);
    }

    @Override
    public String toString() {
        return "ParametricLoopPayload{outer=" + outer + ", points=" + points + "}";
    }
}
