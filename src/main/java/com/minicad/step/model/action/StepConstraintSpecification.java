package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONSTRAINT_SPECIFICATION.
 * A constraint specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param constraintType constraint type (geometric, functional, assembly)
 * @varianceSubject constraint variance subject
 * @varianceValue constraint variance value
 * @varianceTolerance tolerance variance if applicable
 * @varianceUnit constraint variance unit
 * @varianceStatus specification variance status
 */
public record StepConstraintSpecification(
    int id,
    String name,
    String constraintType,
    StepEntity varianceSubject,
    double varianceValue,
    double varianceTolerance,
    StepEntity varianceUnit,
    String varianceStatus) implements StepEntity {
}