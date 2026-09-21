package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOLERANCE_ZONE_FORM.
 * Defines the shape of a tolerance zone (e.g., cylindrical, spherical, planar).
 *
 * @param id STEP instance id
 * @param name form name
 * @param zoneShape the shape description for the tolerance zone
 */
public final class StepToleranceZoneForm extends AbstractStepEntity {
    private final String zoneShape;

    public StepToleranceZoneForm(int id, String name, String zoneShape) {
        super(id, name);
        this.zoneShape = zoneShape;
    }

    public String getZoneShape() {
        return zoneShape;
    }

    // Record-style accessor
    public String zoneShape() {
        return zoneShape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneShape", zoneShape);
        return state;
    }
}
