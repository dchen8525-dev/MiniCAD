package com.minicad.preview.payload;

/**
 * Binary loop payload for STEP preview export.
 */
public final class BinaryLoopPayload {
    private final boolean outer;
    private final int pointOffset;
    private final int pointCount;

    public BinaryLoopPayload(boolean outer, int pointOffset, int pointCount) {
        this.outer = outer;
        this.pointOffset = pointOffset;
        this.pointCount = pointCount;
    }

    public boolean getOuter() {
        return outer;
    }
    public int getPointOffset() {
        return pointOffset;
    }
    public int getPointCount() {
        return pointCount;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public int pointOffset() { return pointOffset; }
    public int pointCount() { return pointCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryLoopPayload that = (BinaryLoopPayload) o;
        return outer == that.outer && pointOffset == that.pointOffset && pointCount == that.pointCount;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), pointOffset, pointCount);
    }

    @Override
    public String toString() {
        return "BinaryLoopPayload{outer=" + outer + ", pointOffset=" + pointOffset + ", pointCount=" + pointCount + "}";
    }
}
