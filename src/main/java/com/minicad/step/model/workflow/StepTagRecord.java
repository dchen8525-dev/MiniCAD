package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TAG_RECORD.
 * A tag record entity.
 *
 * @param id STEP instance id
 * @param name tag name
 * @param tagType tag variance type
 * @param tagValue tag variance value
 * @param tagTarget tag variance target reference
 * @param tagCategory tag variance category
 * @param tagStatus tag variance status
 */
public record StepTagRecord(
    int id,
    String name,
    String tagType,
    String tagValue,
    StepEntity tagTarget,
    String tagCategory,
    String tagStatus) implements StepEntity {
}