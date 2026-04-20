package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal NAME_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue name value
 * @param namedItem named entity
 */
public record StepNameAttribute(
        int id,
        String attributeValue,
        StepEntity namedItem
) implements StepEntity {

    @Override
    public String name() {
        return attributeValue;
    }
}
