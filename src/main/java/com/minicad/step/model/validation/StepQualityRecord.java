package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved QUALITY_RECORD.
 * A quality record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param recordType record type classification
 * @param recordItems record items/data
 * @param recordContext record context reference
 * @param recordDate record date
 * @param recordAuthor record author/inspector
 * @param recordStatus record status (approved, pending)
 * @param attachments record attachments/references
 */
public record StepQualityRecord(
    int id,
    String name,
    String recordType,
    List<StepEntity> recordItems,
    StepEntity recordContext,
    StepEntity recordDate,
    StepEntity recordAuthor,
    String recordStatus,
    List<StepEntity> attachments) implements StepEntity {
}