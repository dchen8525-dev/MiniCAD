package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHANGE_RECORD.
 * A change record entity.
 *
 * @param id STEP instance id
 * @param name change name
 * @param changeType change variance type
 * @param changeDescription change variance description
 * @param changeTarget change variance target reference
 * @param changeReason change variance reason
 * @param changeTimestamp change variance timestamp
 * @param changeStatus change variance status
 */
public record StepChangeRecord(
    int id,
    String name,
    String changeType,
    String changeDescription,
    StepEntity changeTarget,
    String changeReason,
    StepEntity changeTimestamp,
    String changeStatus) implements StepEntity {
}