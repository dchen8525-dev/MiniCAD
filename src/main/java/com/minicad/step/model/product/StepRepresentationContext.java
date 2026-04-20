package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal semantic representation context.
 *
 * @param id STEP instance id
 * @param contextIdentifier context identifier
 * @param contextType context type
 */
public record StepRepresentationContext(int id, String contextIdentifier, String contextType) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
