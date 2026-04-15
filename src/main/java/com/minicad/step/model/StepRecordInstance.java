package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RECORD_INSTANCE.
 * A record instance entity.
 *
 * @param id STEP instance id
 * @param name record instance name
 * @param recordDefinition record variance definition reference
 * @param recordValues record variance field values
 * @param recordTimestamp record variance timestamp
 * @param recordStatus record variance status
 */
public record StepRecordInstance(
    int id,
    String name,
    StepEntity recordDefinition,
    List<String> recordValues,
    StepEntity recordTimestamp,
    String recordStatus) implements StepEntity {
}