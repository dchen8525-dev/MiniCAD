package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal placed datum target feature link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public record StepPlacedDatumTargetFeature(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
