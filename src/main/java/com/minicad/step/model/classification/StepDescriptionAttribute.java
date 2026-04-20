package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DESCRIPTION_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue description value
 * @param describedItem described entity
 */
public record StepDescriptionAttribute(
        int id,
        String attributeValue,
        StepEntity describedItem
) implements StepEntity {

    @Override
    public String name() {
        return attributeValue;
    }
}
