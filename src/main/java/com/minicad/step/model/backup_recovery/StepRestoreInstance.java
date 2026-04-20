package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RESTORE_INSTANCE.
 * A restore instance entity.
 *
 * @param id STEP instance id
 * @param name restore instance name
 * @param restoreDefinition restore variance definition reference
 * @param restoreStartTime restore variance start time
 * @param restoreEndTime restore variance end time
 * @param restoreResult restore variance result
 * @param restoreValid restore variance valid flag
 * @param restoreStatus restore variance status
 */
public record StepRestoreInstance(
    int id,
    String name,
    StepEntity restoreDefinition,
    StepEntity restoreStartTime,
    StepEntity restoreEndTime,
    String restoreResult,
    boolean restoreValid,
    String restoreStatus) implements StepEntity {
}