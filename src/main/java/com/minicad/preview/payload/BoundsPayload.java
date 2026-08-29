package com.minicad.preview.payload;

/**
 * Bounds payload for bounding box representation.
 */
public final class BoundsPayload {
    private final PointPayload min;
    private final PointPayload max;

    public BoundsPayload(PointPayload min, PointPayload max) {
        this.min = min;
        this.max = max;
    }

    public PointPayload getMin() { return min; }
    public PointPayload getMax() { return max; }

    // Record-style accessors
    public PointPayload min() { return min; }
    public PointPayload max() { return max; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundsPayload that = (BoundsPayload) o;
        return java.util.Objects.equals(min, that.min) && java.util.Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "BoundsPayload{min=" + min + ", max=" + max + "}";
    }
}
