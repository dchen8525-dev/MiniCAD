package com.minicad.step.model;

import java.util.List;

/**
 * Resolved AUDIT_DEFINITION.
 * An audit definition entity.
 *
 * @param id STEP instance id
 * @param name audit name
 * @param auditType audit variance type
 * @param auditDescription audit variance description
 * @param auditCriteria audit variance criteria
 * @param auditScope audit variance scope
 * @param auditFrequency audit variance frequency
 * @param auditStatus audit variance status
 */
public record StepAuditDefinition(
    int id,
    String name,
    String auditType,
    String auditDescription,
    List<String> auditCriteria,
    String auditScope,
    String auditFrequency,
    String auditStatus) implements StepEntity {
}