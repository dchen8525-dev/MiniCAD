package com.minicad.preview.payload;

/**
 * Unsupported face payload for unsupported face geometry.
 */
public final class UnsupportedFacePayload {
    private final int stepId;
    private final String name;
    private final String surfaceType;
    private final String reason;

    public UnsupportedFacePayload(int stepId, String name, String surfaceType, String reason) {
        this.stepId = stepId;
        this.name = name;
        this.surfaceType = surfaceType;
        this.reason = reason;
    }

    public int getStepId() { return stepId; }
    public String getName() { return name; }
    public String getSurfaceType() { return surfaceType; }
    public String getReason() { return reason; }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String surfaceType() { return surfaceType; }
    public String reason() { return reason; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsupportedFacePayload)) return false;
        UnsupportedFacePayload that = (UnsupportedFacePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(surfaceType, that.surfaceType) && java.util.Objects.equals(reason, that.reason);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, surfaceType, reason);
    }

    @Override public String toString() {
        return "UnsupportedFacePayload{" + "stepId=stepId, name=name, surfaceType=surfaceType, reason=reason" + "}";
    }
}
