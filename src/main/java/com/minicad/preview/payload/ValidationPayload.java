package com.minicad.preview.payload;

/**
 * Validation payload for geometry validation results.
 */
public final class ValidationPayload {
    private final int representationCount;
    private final int instanceCount;
    private final int renderedFaceCount;
    private final int renderedEdgeCount;
    private final double approxSurfaceArea;
    private final double approxEdgeLength;
    private final PointPayload center;
    private final ValidationReportPayload report;

    public ValidationPayload(int representationCount, int instanceCount, int renderedFaceCount, int renderedEdgeCount, double approxSurfaceArea, double approxEdgeLength, PointPayload center, ValidationReportPayload report) {
        this.representationCount = representationCount;
        this.instanceCount = instanceCount;
        this.renderedFaceCount = renderedFaceCount;
        this.renderedEdgeCount = renderedEdgeCount;
        this.approxSurfaceArea = approxSurfaceArea;
        this.approxEdgeLength = approxEdgeLength;
        this.center = center;
        this.report = report;
    }

    public int getRepresentationCount() { return representationCount; }
    public int getInstanceCount() { return instanceCount; }
    public int getRenderedFaceCount() { return renderedFaceCount; }
    public int getRenderedEdgeCount() { return renderedEdgeCount; }
    public double getApproxSurfaceArea() { return approxSurfaceArea; }
    public double getApproxEdgeLength() { return approxEdgeLength; }
    public PointPayload getCenter() { return center; }
    public ValidationReportPayload getReport() { return report; }

    // Record-style accessors
    public int representationCount() { return representationCount; }
    public int instanceCount() { return instanceCount; }
    public int renderedFaceCount() { return renderedFaceCount; }
    public int renderedEdgeCount() { return renderedEdgeCount; }
    public double approxSurfaceArea() { return approxSurfaceArea; }
    public double approxEdgeLength() { return approxEdgeLength; }
    public PointPayload center() { return center; }
    public ValidationReportPayload report() { return report; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationPayload)) return false;
        ValidationPayload that = (ValidationPayload) o;
        return representationCount == that.representationCount && instanceCount == that.instanceCount && renderedFaceCount == that.renderedFaceCount && renderedEdgeCount == that.renderedEdgeCount && Double.compare(that.approxSurfaceArea, approxSurfaceArea) == 0 && Double.compare(that.approxEdgeLength, approxEdgeLength) == 0 && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(report, that.report);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(representationCount, instanceCount, renderedFaceCount, renderedEdgeCount, Double.hashCode(approxSurfaceArea), Double.hashCode(approxEdgeLength), center, report);
    }

    @Override public String toString() {
        return "ValidationPayload{" + "representationCount=representationCount, instanceCount=instanceCount, renderedFaceCount=renderedFaceCount, renderedEdgeCount=renderedEdgeCount, approxSurfaceArea=approxSurfaceArea, approxEdgeLength=approxEdgeLength, center=center, report=report" + "}";
    }
}