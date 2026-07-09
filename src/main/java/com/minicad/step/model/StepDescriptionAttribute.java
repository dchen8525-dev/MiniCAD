package com.minicad.step.model.classification;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DESCRIPTION_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue description value
 * @param describedItem described entity
 */
/**
 * Minimal DESCRIPTION_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue description value
 * @param describedItem described entity
 */
public final class StepDescriptionAttribute implements StepEntity {
    private final int id;
    private final String attributeValue;
    private final StepEntity describedItem;

    public StepDescriptionAttribute(int id, String attributeValue, StepEntity describedItem) {
        this.id = id;
        this.attributeValue = attributeValue;
        this.describedItem = describedItem;
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

    public StepEntity getDescribedItem() {
        return describedItem;
    }

    // Record-style accessor
    public StepEntity describedItem() {
        return describedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDescriptionAttribute that = (StepDescriptionAttribute) o;
        return id == that.id && Objects.equals(attributeValue, that.attributeValue) && Objects.equals(describedItem, that.describedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attributeValue, describedItem);
    }

    @Override
    public String toString() {
        return "StepDescriptionAttribute{" + "id=" + id + "attributeValue=" + attributeValue + "describedItem=" + describedItem + "}";
    }
}
