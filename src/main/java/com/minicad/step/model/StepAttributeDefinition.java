package com.minicad.step.model.classification;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ATTRIBUTE_DEFINITION.
 * An attribute definition entity.
 *
 * @param id STEP instance id
 * @param name attribute name
 * @param attributeType attribute variance type
 * @param attributeDataType attribute variance data type
 * @param attributeRange attribute variance valid range
 * @param attributeDefault attribute variance default value
 * @param attributeStatus attribute variance status
 */
/**
 * Resolved ATTRIBUTE_DEFINITION.
 * An attribute definition entity.
 *
 * @param id STEP instance id
 * @param name attribute name
 * @param attributeType attribute variance type
 * @param attributeDataType attribute variance data type
 * @param attributeRange attribute variance valid range
 * @param attributeDefault attribute variance default value
 * @param attributeStatus attribute variance status
 */
public final class StepAttributeDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String attributeType;
    private final String attributeDataType;
    private final List<String> attributeRange;
    private final String attributeDefault;
    private final String attributeStatus;

    public StepAttributeDefinition(int id, String name, String attributeType, String attributeDataType, List<String> attributeRange, String attributeDefault, String attributeStatus) {
        this.id = id;
        this.name = name;
        this.attributeType = attributeType;
        this.attributeDataType = attributeDataType;
        this.attributeRange = attributeRange == null ? null : java.util.List.copyOf(attributeRange);
        this.attributeDefault = attributeDefault;
        this.attributeStatus = attributeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public String getAttributeDataType() {
        return attributeDataType;
    }

    public List<String> getAttributeRange() {
        return attributeRange;
    }

    public String getAttributeDefault() {
        return attributeDefault;
    }

    public String getAttributeStatus() {
        return attributeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAttributeDefinition that = (StepAttributeDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(attributeType, that.attributeType) && Objects.equals(attributeDataType, that.attributeDataType) && Objects.equals(attributeRange, that.attributeRange) && Objects.equals(attributeDefault, that.attributeDefault) && Objects.equals(attributeStatus, that.attributeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, attributeType, attributeDataType, attributeRange, attributeDefault, attributeStatus);
    }

    @Override
    public String toString() {
        return "StepAttributeDefinition{" + "id=" + id + "name=" + name + "attributeType=" + attributeType + "attributeDataType=" + attributeDataType + "attributeRange=" + attributeRange + "attributeDefault=" + attributeDefault + "attributeStatus=" + attributeStatus + "}";
    }
}