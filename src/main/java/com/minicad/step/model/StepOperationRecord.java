package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OPERATION_RECORD.
 * An operation record entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType operation variance type
 * @param operationName operation variance operation name
 * @param operationTarget operation variance target reference
 * @param operationActor operation variance actor reference
 * @param operationTimestamp operation variance timestamp
 * @param operationResult operation variance result
 * @param operationStatus operation variance status
 */
public record StepOperationRecord(
    int id,
    String name,
    String operationType,
    String operationName,
    StepEntity operationTarget,
    StepEntity operationActor,
    StepEntity operationTimestamp,
    String operationResult,
    String operationStatus) implements StepEntity {
}