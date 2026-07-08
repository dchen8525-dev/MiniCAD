package com.minicad.preview.payload;

/**
 * Binary edge payload for STEP preview export.
 */
public final class BinaryEdgePayload {
    private final int stepId;
    private final int pointOffset;
    private final int pointCount;
    private final EdgeCurvePayload curve;
    private final ColorPayload color;

    public BinaryEdgePayload(int stepId, int pointOffset, int pointCount, EdgeCurvePayload curve, ColorPayload color) {
        this.stepId = stepId;
        this.pointOffset = pointOffset;
        this.pointCount = pointCount;
        this.curve = curve;
        this.color = color;
    }

    public int getStepId() {
        return stepId;
    }
    public int getPointOffset() {
        return pointOffset;
    }
    public int getPointCount() {
        return pointCount;
    }
    public EdgeCurvePayload getCurve() {
        return curve;
    }
    public ColorPayload getColor() {
        return color;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public int pointOffset() { return pointOffset; }
    public int pointCount() { return pointCount; }
    public EdgeCurvePayload curve() { return curve; }
    public ColorPayload color() { return color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryEdgePayload that = (BinaryEdgePayload) o;
        return stepId == that.stepId && pointOffset == that.pointOffset && pointCount == that.pointCount && java.util.Objects.equals(curve, that.curve) && java.util.Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(stepId, pointOffset, pointCount, curve, color);
    }

    @Override
    public String toString() {
        return "BinaryEdgePayload{stepId=" + stepId + ", pointOffset=" + pointOffset + ", pointCount=" + pointCount + ", curve=" + curve + ", color=" + color + "}";
    }
}