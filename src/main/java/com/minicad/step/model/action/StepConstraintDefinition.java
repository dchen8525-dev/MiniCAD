package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONSTRAINT_DEFINITION.
 * A constraint definition entity.
 *
 * @param id STEP instance id
 * @param name constraint name
 * @param constraintType constraint variance type
 * @param constraintExpression constraint variance expression
 * @param constraintParameters constraint variance parameters
 * @param constraintSeverity constraint variance severity level
 * @param constraintStatus constraint variance status
 */
public record StepConstraintDefinition(
    int id,
    String name,
    String constraintType,
    String constraintExpression,
    List<String> constraintParameters,
    int constraintSeverity,
    String constraintStatus) implements StepEntity {
}