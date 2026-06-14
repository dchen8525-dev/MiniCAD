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

        assertEquals(MiniCadIssue.Severity.WARNING, issue.getSeverity());
        assertEquals("step.unsupported", issue.getCode());
        assertEquals(42, issue.getEntityId());
        assertEquals("ADVANCED_FACE", issue.getEntityType());
        assertEquals("B-spline surface preview is unsupported", issue.getMessage());
    }

    @Test
    void shouldRejectBlankCode() {
        assertThrows(IllegalArgumentException.class,
                () -> MiniCadIssue.warning(" ", null, null, "message"));
    }
}
