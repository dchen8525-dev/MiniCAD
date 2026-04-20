package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RESTORE_DEFINITION.
 * A restore definition entity.
 *
 * @param id STEP instance id
 * @param name restore name
 * @param restoreType restore variance type
 * @param restoreSource restore variance source backup reference
 * @param restoreTarget restore variance target reference
 * @param restoreOptions restore variance options
 * @param restoreStatus restore variance status
 */
public record StepRestoreDefinition(
    int id,
    String name,
    String restoreType,
    StepEntity restoreSource,
    StepEntity restoreTarget,
    List<String> restoreOptions,
    String restoreStatus) implements StepEntity {
}