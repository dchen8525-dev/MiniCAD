package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved KINEMATIC_PROPERTY.
 * A kinematic property definition.
 */
/**
 * Resolved KINEMATIC_PROPERTY.
 * A kinematic property definition.
 */
public final class StepKinematicProperty implements StepEntity {
    private final int id;
    private final String name;
    private final String propertyType;
    private final StepEntity value;

    public StepKinematicProperty(int id, String name, String propertyType, StepEntity value) {
        this.id = id;
        this.name = name;
        this.propertyType = propertyType;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public StepEntity getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicProperty that = (StepKinematicProperty) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(propertyType, that.propertyType) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, propertyType, value);
    }

    @Override
    public String toString() {
        return "StepKinematicProperty{" + "id=" + id + "name=" + name + "propertyType=" + propertyType + "value=" + value + "}";
    }
}
