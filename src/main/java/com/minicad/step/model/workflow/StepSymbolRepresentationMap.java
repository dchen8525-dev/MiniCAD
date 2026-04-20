package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SYMBOL_REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped symbol representation
 */
public record StepSymbolRepresentationMap(
        int id,
        StepEntity mappedOrigin,
        StepRepresentation mappedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
