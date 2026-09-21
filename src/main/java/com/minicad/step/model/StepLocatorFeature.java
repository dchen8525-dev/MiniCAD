package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLocatorFeature extends AbstractStepEntity {
    private final String locatorType;
    private final StepEntity locatorGeometry;
    private final StepEntity locatorPosition;
    private final double varianceTolerance;
    private final StepEntity locatorMaterial;

    public StepLocatorFeature(int id, String name, String locatorType, StepEntity locatorGeometry, StepEntity locatorPosition, double varianceTolerance, StepEntity locatorMaterial) {
        super(id, name);
        this.locatorType = locatorType;
        this.locatorGeometry = locatorGeometry;
        this.locatorPosition = locatorPosition;
        this.varianceTolerance = varianceTolerance;
        this.locatorMaterial = locatorMaterial;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("locatorType", locatorType);
        state.put("locatorGeometry", locatorGeometry);
        state.put("locatorPosition", locatorPosition);
        state.put("varianceTolerance", varianceTolerance);
        state.put("locatorMaterial", locatorMaterial);
        return state;
    }
}
