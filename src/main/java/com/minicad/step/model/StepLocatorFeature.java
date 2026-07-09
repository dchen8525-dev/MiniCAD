package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOCATOR_FEATURE.
 * A locator feature entity.
 *
 * @param id STEP instance id
 * @param name locator name
 * @param locatorType locator type (pin, surface, datum)
 * @param locatorGeometry locator geometry representation
 * @param locatorPosition locator position placement
 * @varianceTolerance locator variance tolerance
 * @param locatorMaterial locator material reference
 */
/**
 * Resolved LOCATOR_FEATURE.
 * A locator feature entity.
 *
 * @param id STEP instance id
 * @param name locator name
 * @param locatorType locator type (pin, surface, datum)
 * @param locatorGeometry locator geometry representation
 * @param locatorPosition locator position placement
 * @varianceTolerance locator variance tolerance
 * @param locatorMaterial locator material reference
 */
public final class StepLocatorFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String locatorType;
    private final StepEntity locatorGeometry;
    private final StepEntity locatorPosition;
    private final double varianceTolerance;
    private final StepEntity locatorMaterial;

    public StepLocatorFeature(int id, String name, String locatorType, StepEntity locatorGeometry, StepEntity locatorPosition, double varianceTolerance, StepEntity locatorMaterial) {
        this.id = id;
        this.name = name;
        this.locatorType = locatorType;
        this.locatorGeometry = locatorGeometry;
        this.locatorPosition = locatorPosition;
        this.varianceTolerance = varianceTolerance;
        this.locatorMaterial = locatorMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public StepEntity getLocatorGeometry() {
        return locatorGeometry;
    }

    public StepEntity getLocatorPosition() {
        return locatorPosition;
    }

    public double getVarianceTolerance() {
        return varianceTolerance;
    }

    public StepEntity getLocatorMaterial() {
        return locatorMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLocatorFeature that = (StepLocatorFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(locatorType, that.locatorType) && Objects.equals(locatorGeometry, that.locatorGeometry) && Objects.equals(locatorPosition, that.locatorPosition) && varianceTolerance == that.varianceTolerance && Objects.equals(locatorMaterial, that.locatorMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, locatorType, locatorGeometry, locatorPosition, varianceTolerance, locatorMaterial);
    }

    @Override
    public String toString() {
        return "StepLocatorFeature{" + "id=" + id + "name=" + name + "locatorType=" + locatorType + "locatorGeometry=" + locatorGeometry + "locatorPosition=" + locatorPosition + "varianceTolerance=" + varianceTolerance + "locatorMaterial=" + locatorMaterial + "}";
    }
}