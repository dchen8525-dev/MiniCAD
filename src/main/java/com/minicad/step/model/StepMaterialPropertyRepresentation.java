package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepMaterialPropertyRepresentation extends AbstractStepEntity {
    private final String propertyName;
    private final double propertyValue;
    private final StepEntity propertyUnit;
    private final String propertyStatus;

    public StepMaterialPropertyRepresentation(int id, String name, String propertyName, double propertyValue, StepEntity propertyUnit, String propertyStatus) {
        super(id, name);
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.propertyUnit = propertyUnit;
        this.propertyStatus = propertyStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "propertyName", propertyName,
                "propertyValue", propertyValue, "propertyUnit", propertyUnit, "propertyStatus", propertyStatus);
    }
}
