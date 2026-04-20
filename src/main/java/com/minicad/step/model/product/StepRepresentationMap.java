package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
/**
 * Minimal REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped representation
 */
public record StepRepresentationMap(
        int id,
        StepEntity mappedOrigin,
        StepRepresentation mappedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
