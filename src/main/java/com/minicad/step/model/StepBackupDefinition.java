package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BACKUP_DEFINITION.
 * A backup definition entity.
 *
 * @param id STEP instance id
 * @param name backup name
 * @param backupType backup variance type
 * @param backupSource backup variance source reference
 * @param backupTarget backup variance target reference
 * @param backupSchedule backup variance schedule
 * @param backupRetention backup variance retention period
 * @param backupStatus backup variance status
 */
public record StepBackupDefinition(
    int id,
    String name,
    String backupType,
    StepEntity backupSource,
    StepEntity backupTarget,
    String backupSchedule,
    int backupRetention,
    String backupStatus) implements StepEntity {
}