package com.minicad.preview.payload;

/**
 * Preview statistics payload.
 */
public final class PreviewStats {
    private final int entityCount;
    private final int solidCount;
    private final int shellCount;
    private final int faceCount;
    private final int edgeCount;
    private final int unsupportedFaceCount;
    private final int unsupportedBooleanCount;

    public PreviewStats(int entityCount, int solidCount, int shellCount, int faceCount, int edgeCount, int unsupportedFaceCount, int unsupportedBooleanCount) {
        this.entityCount = entityCount;
        this.solidCount = solidCount;
        this.shellCount = shellCount;
        this.faceCount = faceCount;
        this.edgeCount = edgeCount;
        this.unsupportedFaceCount = unsupportedFaceCount;
        this.unsupportedBooleanCount = unsupportedBooleanCount;
    }

    public int getEntityCount() { return entityCount; }
    public int getSolidCount() { return solidCount; }
    public int getShellCount() { return shellCount; }
    public int getFaceCount() { return faceCount; }
    public int getEdgeCount() { return edgeCount; }
    public int getUnsupportedFaceCount() { return unsupportedFaceCount; }
    public int getUnsupportedBooleanCount() { return unsupportedBooleanCount; }

    // Record-style accessors
    public int entityCount() { return entityCount; }
    public int solidCount() { return solidCount; }
    public int shellCount() { return shellCount; }
    public int faceCount() { return faceCount; }
    public int edgeCount() { return edgeCount; }
    public int unsupportedFaceCount() { return unsupportedFaceCount; }
    public int unsupportedBooleanCount() { return unsupportedBooleanCount; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreviewStats)) return false;
        PreviewStats that = (PreviewStats) o;
        return entityCount == that.entityCount && solidCount == that.solidCount && shellCount == that.shellCount && faceCount == that.faceCount && edgeCount == that.edgeCount && unsupportedFaceCount == that.unsupportedFaceCount && unsupportedBooleanCount == that.unsupportedBooleanCount;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(entityCount, solidCount, shellCount, faceCount, edgeCount, unsupportedFaceCount, unsupportedBooleanCount);
    }

    @Override public String toString() {
        return "PreviewStats{" + "entityCount=entityCount, solidCount=solidCount, shellCount=shellCount, faceCount=faceCount, edgeCount=edgeCount, unsupportedFaceCount=unsupportedFaceCount, unsupportedBooleanCount=unsupportedBooleanCount" + "}";
    }
}