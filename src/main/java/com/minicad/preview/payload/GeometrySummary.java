package com.minicad.preview.payload;

/**
 * Geometry summary for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class GeometrySummary {
    private final int faceCount;
    private final int edgeCount;
    private final double approxSurfaceArea;
    private final double approxEdgeLength;

    public GeometrySummary(int faceCount, int edgeCount, double approxSurfaceArea, double approxEdgeLength) {
        this.faceCount = faceCount;
        this.edgeCount = edgeCount;
        this.approxSurfaceArea = approxSurfaceArea;
        this.approxEdgeLength = approxEdgeLength;
    }

    public int getFaceCount() { return faceCount; }
    public int getEdgeCount() { return edgeCount; }
    public double getApproxSurfaceArea() { return approxSurfaceArea; }
    public double getApproxEdgeLength() { return approxEdgeLength; }

    // Record-style accessors
    public int faceCount() { return faceCount; }
    public int edgeCount() { return edgeCount; }
    public double approxSurfaceArea() { return approxSurfaceArea; }
    public double approxEdgeLength() { return approxEdgeLength; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeometrySummary that = (GeometrySummary) o;
        return faceCount == that.faceCount && edgeCount == that.edgeCount && Double.compare(that.approxSurfaceArea, approxSurfaceArea) == 0 && Double.compare(that.approxEdgeLength, approxEdgeLength) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(faceCount, edgeCount, Double.hashCode(approxSurfaceArea), Double.hashCode(approxEdgeLength));
    }

    @Override
    public String toString() {
        return "GeometrySummary{faceCount=" + faceCount + ", edgeCount=" + edgeCount + ", approxSurfaceArea=" + approxSurfaceArea + ", approxEdgeLength=" + approxEdgeLength + "}";
    }
}
