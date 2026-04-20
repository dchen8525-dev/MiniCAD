package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONSTRAINT_INSTANCE.
 * A constraint instance entity.
 *
 * @param id STEP instance id
 * @param name constraint instance name
 * @param constraintDefinition constraint variance definition reference
 * @param constraintState constraint variance state
 * @param constraintValue constraint variance current value
 * @param constraintViolations constraint variance violation count
 * @param constraintStatus constraint variance status
 */
public record StepConstraintInstance(
    int id,
    String name,
    StepEntity constraintDefinition,
    String constraintState,
    String constraintValue,
    int constraintViolations,
    String constraintStatus) implements StepEntity {
}