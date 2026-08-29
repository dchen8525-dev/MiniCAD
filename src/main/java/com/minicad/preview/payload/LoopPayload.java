package com.minicad.preview.payload;

import java.util.List;

/**
 * Loop payload for face boundary representation.
 */
public final class LoopPayload {
    private final boolean outer;
    private final List<PointPayload> points;

    public LoopPayload(boolean outer, List<PointPayload> points) {
        this.outer = outer;
        this.points = PreviewPayloadCopies.copy(points);
    }

    public boolean getOuter() {
        return outer;
    }
    public List<PointPayload> getPoints() {
        return points;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public List<PointPayload> points() { return points; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoopPayload that = (LoopPayload) o;
        return outer == that.outer && java.util.Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), points);
    }

    @Override
    public String toString() {
        return "LoopPayload{outer=" + outer + ", points=" + points + "}";
    }
}
