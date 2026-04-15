package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ARCHIVE_RECORD.
 * An archive record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData archived variance data
 * @varianceLocation archive variance location
 * @varianceDate archive variance date
 * @varianceRetention retention variance period
 * @varianceAccess access variance restrictions
 * @varianceStatus record variance status
 */
public record StepArchiveRecord(
    int id,
    String name,
    StepEntity varianceData,
    String varianceLocation,
    StepEntity varianceDate,
    double varianceRetention,
    String varianceAccess,
    String varianceStatus) implements StepEntity {
}