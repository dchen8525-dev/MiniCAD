package com.minicad.preview.payload;

/**
 * Assembly metrics payload for STEP preview export.
 */
public final class AssemblyMetrics {
    private final GeometrySummary summary;
    private final BoundsPayload bounds;

    public AssemblyMetrics(GeometrySummary summary, BoundsPayload bounds) {
        this.summary = summary;
        this.bounds = bounds;
    }

    public GeometrySummary getSummary() { return summary; }
    public BoundsPayload getBounds() { return bounds; }

    public GeometrySummary summary() { return summary; }
    public BoundsPayload bounds() { return bounds; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssemblyMetrics)) return false;
        AssemblyMetrics that = (AssemblyMetrics) o;
        return java.util.Objects.equals(summary, that.summary) && java.util.Objects.equals(bounds, that.bounds);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(summary, bounds);
    }

    @Override public String toString() {
        return "AssemblyMetrics{" + "summary=summary, bounds=bounds" + "}";
    }
}