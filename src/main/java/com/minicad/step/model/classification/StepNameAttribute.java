package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal NAME_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue name value
 * @param namedItem named entity
 */
/**
 * Minimal NAME_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue name value
 * @param namedItem named entity
 */
public final class StepNameAttribute implements StepEntity {
    private final int id;
    private final String attributeValue;
    private final StepEntity namedItem;

    public StepNameAttribute(int id, String attributeValue, StepEntity namedItem) {
        this.id = id;
        this.attributeValue = attributeValue;
        this.namedItem = namedItem;
    }

    public int getId() {
        return id;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public StepEntity getNamedItem() {
        return namedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNameAttribute that = (StepNameAttribute) o;
        return id == that.id && Objects.equals(attributeValue, that.attributeValue) && Objects.equals(namedItem, that.namedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attributeValue, namedItem);
    }

    @Override
    public String toString() {
        return "StepNameAttribute{" + "id=" + id + "attributeValue=" + attributeValue + "namedItem=" + namedItem + "}";
    }
}
