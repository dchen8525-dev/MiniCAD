package com.minicad.common;

/**
 * Structured diagnostic produced during parsing, resolution, geometry building,
 * validation, preview, or export.
 *
 * @param severity issue severity
 * @param code stable machine-readable code
 * @param entityId optional STEP entity id
 * @param entityType optional STEP entity type
 * @param message human-readable diagnostic
 */
/**
 * Structured diagnostic produced during parsing, resolution, geometry building,
 * validation, preview, or export.
 *
 * @param severity issue severity
 * @param code stable machine-readable code
 * @param entityId optional STEP entity id
 * @param entityType optional STEP entity type
 * @param message human-readable diagnostic
 */
public final class MiniCadIssue {
    private final Severity severity;
    private final String code;
    private final Integer entityId;
    private final String entityType;
    private final String message;

    /**
     * Diagnostic severity.
     */
    public enum Severity {
        INFO,
        WARNING,
        ERROR
    }

    public MiniCadIssue(Severity severity, String code, Integer entityId, String entityType, String message) {
        Preconditions.requireNonNull(severity, "severity");
        Preconditions.requireNonNull(code, "code");
        Preconditions.requireNonNull(message, "message");
        if (code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        this.severity = severity;
        this.code = code;
        this.entityId = entityId;
        this.entityType = entityType;
        this.message = message;
    }

    public static MiniCadIssue error(String code, Integer entityId, String entityType, String message) {
        return new MiniCadIssue(Severity.ERROR, code, entityId, entityType, message);
    }

    public static MiniCadIssue warning(String code, Integer entityId, String entityType, String message) {
        return new MiniCadIssue(Severity.WARNING, code, entityId, entityType, message);
    }

    public static MiniCadIssue unsupported(Integer entityId, String entityType, String message) {
        return warning("step.unsupported", entityId, entityType, message);
    }

    public Severity severity() {
        return severity;
    }

    public String code() {
        return code;
    }

    public Integer entityId() {
        return entityId;
    }

    public String entityType() {
        return entityType;
    }

    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniCadIssue that = (MiniCadIssue) o;
        return Objects.equals(severity, that.severity) && Objects.equals(code, that.code) && Objects.equals(entityId, that.entityId) && Objects.equals(entityType, that.entityType) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, code, entityId, entityType, message);
    }

    @Override
    public String toString() {
        return "MiniCadIssue{" + "severity=" + severity + "code=" + code + "entityId=" + entityId + "entityType=" + entityType + "message=" + message + "}";
    }
}
