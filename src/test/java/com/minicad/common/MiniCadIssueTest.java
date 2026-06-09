package com.minicad.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MiniCadIssueTest {

    @Test
    void shouldCreateStructuredUnsupportedIssue() {
        MiniCadIssue issue = MiniCadIssue.unsupported(
                42,
                "ADVANCED_FACE",
                "B-spline surface preview is unsupported"
        );

        assertEquals(MiniCadIssue.Severity.WARNING, issue.severity());
        assertEquals("step.unsupported", issue.code());
        assertEquals(42, issue.entityId());
        assertEquals("ADVANCED_FACE", issue.entityType());
        assertEquals("B-spline surface preview is unsupported", issue.message());
    }

    @Test
    void shouldRejectBlankCode() {
        assertThrows(IllegalArgumentException.class,
                () -> MiniCadIssue.warning(" ", null, null, "message"));
    }
}
