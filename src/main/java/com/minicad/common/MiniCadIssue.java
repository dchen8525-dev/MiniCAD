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
public record MiniCadIssue(
        Severity severity,
        String code,
        Integer entityId,
        String entityType,
        String message
) {

    public MiniCadIssue {
        Preconditions.requireNonNull(severity, "severity");
        Preconditions.requireNonNull(code, "code");
        Preconditions.requireNonNull(message, "message");
        if (code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
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

    /**
     * Diagnostic severity.
     */
    public enum Severity {
        INFO,
        WARNING,
        ERROR
    }
}
