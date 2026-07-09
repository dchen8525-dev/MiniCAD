package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ATTRIBUTE_INSTANCE.
 * An attribute instance entity.
 *
 * @param id STEP instance id
 * @param name attribute instance name
 * @param attributeDefinition attribute variance definition reference
 * @param attributeValue attribute variance current value
 * @param attributeStatus attribute variance status
 */
/**
 * Resolved ATTRIBUTE_INSTANCE.
 * An attribute instance entity.
 *
 * @param id STEP instance id
 * @param name attribute instance name
 * @param attributeDefinition attribute variance definition reference
 * @param attributeValue attribute variance current value
 * @param attributeStatus attribute variance status
 */
public final class StepAttributeInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity attributeDefinition;
    private final String attributeValue;
    private final String attributeStatus;

    public StepAttributeInstance(int id, String name, StepEntity attributeDefinition, String attributeValue, String attributeStatus) {
        this.id = id;
        this.name = name;
        this.attributeDefinition = attributeDefinition;
        this.attributeValue = attributeValue;
        this.attributeStatus = attributeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAttributeDefinition() {
        return attributeDefinition;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getAttributeStatus() {
        return attributeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAttributeInstance that = (StepAttributeInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(attributeDefinition, that.attributeDefinition) && Objects.equals(attributeValue, that.attributeValue) && Objects.equals(attributeStatus, that.attributeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, attributeDefinition, attributeValue, attributeStatus);
    }

    @Override
    public String toString() {
        return "StepAttributeInstance{" + "id=" + id + "name=" + name + "attributeDefinition=" + attributeDefinition + "attributeValue=" + attributeValue + "attributeStatus=" + attributeStatus + "}";
    }
}