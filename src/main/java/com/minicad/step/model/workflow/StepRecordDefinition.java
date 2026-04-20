package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RECORD_DEFINITION.
 * A record definition entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param recordType record variance type
 * @param recordFields record variance field definitions
 * @param recordKey record variance key fields
 * @param recordStatus record variance status
 */
public record StepRecordDefinition(
    int id,
    String name,
    String recordType,
    List<String> recordFields,
    List<String> recordKey,
    String recordStatus) implements StepEntity {
}