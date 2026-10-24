package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal ID_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue identifier value
 * @param identifiedItem identified entity
 */
public final class StepIdAttribute extends AbstractStepEntity {
    private final String attributeValue;
    private final StepEntity identifiedItem;

    public StepIdAttribute(int id, String attributeValue, StepEntity identifiedItem) {
        super(id, "");
        this.attributeValue = attributeValue;
        this.identifiedItem = identifiedItem;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    // Record-style accessor
    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "attributeValue", attributeValue, "identifiedItem", identifiedItem);
    }
}
