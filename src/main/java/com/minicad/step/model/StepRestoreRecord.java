package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RESTORE_RECORD.
 * A restore record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData restored variance data
 * @varianceSource restore variance source/backup
 * @varianceDate restore variance date
 * @varianceVerified verification variance status
 * @varianceStatus record variance status
 */
public record StepRestoreRecord(
    int id,
    String name,
    StepEntity varianceData,
    StepEntity varianceSource,
    StepEntity varianceDate,
    boolean varianceVerified,
    String varianceStatus) implements StepEntity {
}