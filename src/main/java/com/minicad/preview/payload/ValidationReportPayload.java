package com.minicad.preview.payload;

import java.util.List;

/**
 * Validation report payload for validation results.
 */
public final class ValidationReportPayload {
    private final String status;
    private final int okCount;
    private final int warnCount;
    private final List<ValidationCheckPayload> checks;

    public ValidationReportPayload(String status, int okCount, int warnCount, List<ValidationCheckPayload> checks) {
        this.status = status;
        this.okCount = okCount;
        this.warnCount = warnCount;
        this.checks = PreviewPayloadCopies.copy(checks);
    }

    public String getStatus() { return status; }
    public int getOkCount() { return okCount; }
    public int getWarnCount() { return warnCount; }
    public List<ValidationCheckPayload> getChecks() { return checks; }

    // Record-style accessors
    public String status() { return status; }
    public int okCount() { return okCount; }
    public int warnCount() { return warnCount; }
    public List<ValidationCheckPayload> checks() { return checks; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationReportPayload)) return false;
        ValidationReportPayload that = (ValidationReportPayload) o;
        return java.util.Objects.equals(status, that.status) && okCount == that.okCount && warnCount == that.warnCount && java.util.Objects.equals(checks, that.checks);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(status, okCount, warnCount, checks);
    }

    @Override public String toString() {
        return "ValidationReportPayload{" + "status=status, okCount=okCount, warnCount=warnCount, checks=checks" + "}";
    }
}
