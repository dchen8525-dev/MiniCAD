package com.minicad.step.model;

import java.util.List;

/**
 * Resolved AUDIT_INSTANCE.
 * An audit instance entity.
 *
 * @param id STEP instance id
 * @param name audit instance name
 * @param auditDefinition audit variance definition reference
 * @param auditStartTime audit variance start time
 * @param auditEndTime audit variance end time
 * @param auditFindings audit variance findings count
 * @param auditPassed audit variance passed flag
 * @param auditStatus audit variance status
 */
public record StepAuditInstance(
    int id,
    String name,
    StepEntity auditDefinition,
    StepEntity auditStartTime,
    StepEntity auditEndTime,
    int auditFindings,
    boolean auditPassed,
    String auditStatus) implements StepEntity {
}