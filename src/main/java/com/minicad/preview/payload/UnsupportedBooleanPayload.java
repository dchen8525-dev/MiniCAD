package com.minicad.preview.payload;

/**
 * Unsupported boolean payload for unsupported boolean operations.
 */
public final class UnsupportedBooleanPayload {
    private final int stepId;
    private final String name;
    private final String type;
    private final String reason;

    public UnsupportedBooleanPayload(int stepId, String name, String type, String reason) {
        this.stepId = stepId;
        this.name = name;
        this.type = type;
        this.reason = reason;
    }

    public int getStepId() { return stepId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getReason() { return reason; }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String type() { return type; }
    public String reason() { return reason; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsupportedBooleanPayload)) return false;
        UnsupportedBooleanPayload that = (UnsupportedBooleanPayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(type, that.type) && java.util.Objects.equals(reason, that.reason);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, type, reason);
    }

    @Override public String toString() {
        return "UnsupportedBooleanPayload{" + "stepId=stepId, name=name, type=type, reason=reason" + "}";
    }
}
