package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ID_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue identifier value
 * @param identifiedItem identified entity
 */
/**
 * Minimal ID_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue identifier value
 * @param identifiedItem identified entity
 */
public final class StepIdAttribute implements StepEntity {
    private final int id;
    private final String attributeValue;
    private final StepEntity identifiedItem;

    public StepIdAttribute(int id, String attributeValue, StepEntity identifiedItem) {
        this.id = id;
        this.attributeValue = attributeValue;
        this.identifiedItem = identifiedItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIdAttribute that = (StepIdAttribute) o;
        return id == that.id && Objects.equals(attributeValue, that.attributeValue) && Objects.equals(identifiedItem, that.identifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attributeValue, identifiedItem);
    }

    @Override
    public String toString() {
        return "StepIdAttribute{" + "id=" + id + "attributeValue=" + attributeValue + "identifiedItem=" + identifiedItem + "}";
    }
}
