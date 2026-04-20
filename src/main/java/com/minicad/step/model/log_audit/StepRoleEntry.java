package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ROLE_ENTRY.
 * A role entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryRole entry variance role reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public record StepRoleEntry(
    int id,
    String name,
    String entryType,
    StepEntity entryRole,
    StepEntity entryHolder,
    boolean entryGranted,
    StepEntity entryTimestamp,
    String entryStatus) implements StepEntity {
}