package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BACKUP_RECORD.
 * A backup record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData backed variance up data
 * @varianceLocation backup variance location
 * @varianceDate backup variance date
 * @varianceSize backup variance size
 * @varianceType backup variance type (full, incremental)
 * @varianceStatus record variance status
 */
public record StepBackupRecord(
    int id,
    String name,
    StepEntity varianceData,
    String varianceLocation,
    StepEntity varianceDate,
    double varianceSize,
    String varianceType,
    String varianceStatus) implements StepEntity {
}