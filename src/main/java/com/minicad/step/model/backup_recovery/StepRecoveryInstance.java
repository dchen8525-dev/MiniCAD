package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RECOVERY_INSTANCE.
 * A recovery instance entity.
 *
 * @param id STEP instance id
 * @param name recovery instance name
 * @param recoveryDefinition recovery variance definition reference
 * @param recoveryState recovery variance state
 * @param recoveryStartTime recovery variance start time
 * @param recoveryEndTime recovery variance end time
 * @param recoveryResult recovery variance result
 * @param recoveryStatus recovery variance status
 */
public record StepRecoveryInstance(
    int id,
    String name,
    StepEntity recoveryDefinition,
    String recoveryState,
    StepEntity recoveryStartTime,
    StepEntity recoveryEndTime,
    String recoveryResult,
    String recoveryStatus) implements StepEntity {
}