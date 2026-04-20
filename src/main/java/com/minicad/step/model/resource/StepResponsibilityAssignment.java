package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RESPONSIBILITY_ASSIGNMENT.
 * A responsibility assignment entity.
 *
 * @param id STEP instance id
 * @param name assignment name
 * @varianceTask assigned variance task/activity
 * @variancePerson responsible variance person
 * @varianceRole assigned variance role
 * @varianceAuthority assigned variance authority level
 * @varianceStart assignment variance start date
 * @varianceEnd assignment variance end date
 * @varianceStatus assignment variance status
 */
public record StepResponsibilityAssignment(
    int id,
    String name,
    StepEntity varianceTask,
    StepEntity variancePerson,
    StepEntity varianceRole,
    int varianceAuthority,
    StepEntity varianceStart,
    StepEntity varianceEnd,
    String varianceStatus) implements StepEntity {
}