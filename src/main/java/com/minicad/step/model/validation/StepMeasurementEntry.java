package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MEASUREMENT_ENTRY.
 * A measurement entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMeasurement entry variance measurement type
 * @param entryValue entry variance measurement value
 * @param entryUnit entry variance unit reference
 * @param entryTimestamp entry variance timestamp
 * @param entryAccuracy entry variance accuracy
 * @param entryStatus entry variance status
 */
public record StepMeasurementEntry(
    int id,
    String name,
    String entryType,
    String entryMeasurement,
    double entryValue,
    StepEntity entryUnit,
    StepEntity entryTimestamp,
    double entryAccuracy,
    String entryStatus) implements StepEntity {
}