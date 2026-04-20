package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BOUNDARY_CONDITION.
 * A boundary condition entity.
 *
 * @param id STEP instance id
 * @param name boundary condition name
 * @param conditionType condition variance type
 * @param conditionLocation condition variance location reference
 * @param conditionConstraints condition variance constraints
 * @param conditionStatus condition variance status
 */
public record StepBoundaryCondition(
    int id,
    String name,
    String conditionType,
    StepEntity conditionLocation,
    List<String> conditionConstraints,
    String conditionStatus) implements StepEntity {
}