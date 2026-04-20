package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.workflow.StepPropertyDefinition;
/**
 * Minimal back chaining rule body link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation property representation
 */
public record StepBackChainingRuleBody(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
