package com.minicad.step.model;

/**
 * Minimal ID_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue identifier value
 * @param identifiedItem identified entity
 */
public record StepIdAttribute(
        int id,
        String attributeValue,
        StepEntity identifiedItem
) implements StepEntity {

    @Override
    public String name() {
        return attributeValue;
    }
}
