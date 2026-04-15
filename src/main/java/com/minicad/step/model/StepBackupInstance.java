package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BACKUP_INSTANCE.
 * A backup instance entity.
 *
 * @param id STEP instance id
 * @param name backup instance name
 * @param backupDefinition backup variance definition reference
 * @param backupStartTime backup variance start time
 * @param backupEndTime backup variance end time
 * @param backupSize backup variance size
 * @param backupValid backup variance valid flag
 * @param backupStatus backup variance status
 */
public record StepBackupInstance(
    int id,
    String name,
    StepEntity backupDefinition,
    StepEntity backupStartTime,
    StepEntity backupEndTime,
    long backupSize,
    boolean backupValid,
    String backupStatus) implements StepEntity {
}