package com.minicad.preview.payload;

import java.util.List;

/**
 * Edge payload for edge geometry representation.
 */
public final class EdgePayload {
    private final int stepId;
    private final List<PointPayload> points;
    private final EdgeCurvePayload curve;
    private final ColorPayload color;

    public EdgePayload(int stepId, List<PointPayload> points, EdgeCurvePayload curve, ColorPayload color) {
        this.stepId = stepId;
        this.points = PreviewPayloadCopies.copy(points);
        this.curve = curve;
        this.color = color;
    }

    public int getStepId() { return stepId; }
    public List<PointPayload> getPoints() { return points; }
    public EdgeCurvePayload getCurve() { return curve; }
    public ColorPayload getColor() { return color; }

    // Record-style accessors
    public int stepId() { return stepId; }
    public List<PointPayload> points() { return points; }
    public EdgeCurvePayload curve() { return curve; }
    public ColorPayload color() { return color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePayload that = (EdgePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(points, that.points) && java.util.Objects.equals(curve, that.curve) && java.util.Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(stepId, points, curve, color);
    }

    @Override
    public String toString() {
        return "EdgePayload{stepId=" + stepId + ", points=" + points + ", curve=" + curve + ", color=" + color + "}";
    }
}