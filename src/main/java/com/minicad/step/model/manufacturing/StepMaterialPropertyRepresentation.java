package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MATERIAL_PROPERTY_REPRESENTATION.
 * A material property representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param propertyName property variance name
 * @param propertyValue property variance value
 * @param propertyUnit property variance unit reference
 * @param propertyStatus property variance status
 */
/**
 * Resolved MATERIAL_PROPERTY_REPRESENTATION.
 * A material property representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param propertyName property variance name
 * @param propertyValue property variance value
 * @param propertyUnit property variance unit reference
 * @param propertyStatus property variance status
 */
public final class StepMaterialPropertyRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final String propertyName;
    private final double propertyValue;
    private final StepEntity propertyUnit;
    private final String propertyStatus;

    public StepMaterialPropertyRepresentation(int id, String name, String propertyName, double propertyValue, StepEntity propertyUnit, String propertyStatus) {
        this.id = id;
        this.name = name;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.propertyUnit = propertyUnit;
        this.propertyStatus = propertyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public StepEntity getPropertyUnit() {
        return propertyUnit;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterialPropertyRepresentation that = (StepMaterialPropertyRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(propertyName, that.propertyName) && propertyValue == that.propertyValue && Objects.equals(propertyUnit, that.propertyUnit) && Objects.equals(propertyStatus, that.propertyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, propertyName, propertyValue, propertyUnit, propertyStatus);
    }

    @Override
    public String toString() {
        return "StepMaterialPropertyRepresentation{" + "id=" + id + "name=" + name + "propertyName=" + propertyName + "propertyValue=" + propertyValue + "propertyUnit=" + propertyUnit + "propertyStatus=" + propertyStatus + "}";
    }
}