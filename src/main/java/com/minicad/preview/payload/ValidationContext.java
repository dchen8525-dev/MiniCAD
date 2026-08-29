package com.minicad.preview.payload;

/**
 * Validation context payload for STEP preview export.
 * Extracted from StepPreviewPayloadTypes to reduce file size.
 */
public final class ValidationContext {
    private final int representationCount;
    private final int instanceCount;
    private final PointPayload center;
    private final double sizeX;
    private final double sizeY;
    private final double sizeZ;

    public ValidationContext(int representationCount, int instanceCount, PointPayload center, double sizeX, double sizeY, double sizeZ) {
        this.representationCount = representationCount;
        this.instanceCount = instanceCount;
        this.center = center;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public int getRepresentationCount() {
        return representationCount;
    }
    public int getInstanceCount() {
        return instanceCount;
    }
    public PointPayload getCenter() {
        return center;
    }
    public double getSizeX() {
        return sizeX;
    }
    public double getSizeY() {
        return sizeY;
    }
    public double getSizeZ() {
        return sizeZ;
    }

    // Record-style accessors
    public int representationCount() {
        return representationCount;
    }

    public int instanceCount() {
        return instanceCount;
    }

    public PointPayload center() {
        return center;
    }

    public double sizeX() {
        return sizeX;
    }

    public double sizeY() {
        return sizeY;
    }

    public double sizeZ() {
        return sizeZ;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationContext)) return false;
        ValidationContext that = (ValidationContext) o;
        return representationCount == that.representationCount && instanceCount == that.instanceCount && java.util.Objects.equals(center, that.center) && Double.compare(that.sizeX, sizeX) == 0 && Double.compare(that.sizeY, sizeY) == 0 && Double.compare(that.sizeZ, sizeZ) == 0;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(representationCount, instanceCount, center, Double.hashCode(sizeX), Double.hashCode(sizeY), Double.hashCode(sizeZ));
    }

    @Override public String toString() {
        return "ValidationContext{" + "representationCount=representationCount, instanceCount=instanceCount, center=center, sizeX=sizeX, sizeY=sizeY, sizeZ=sizeZ" + "}";
    }
}
