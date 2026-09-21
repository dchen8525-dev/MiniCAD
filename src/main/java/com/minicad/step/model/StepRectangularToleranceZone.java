package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RECTANGULAR_TOLERANCE_ZONE.
 * A rectangular tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneWidth width of the tolerance zone
 * @param zoneHeight height of the tolerance zone
 */
public final class StepRectangularToleranceZone extends AbstractStepEntity {
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneWidth;
    private final Double zoneHeight;

    public StepRectangularToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneWidth, Double zoneHeight) {
        super(id, name);
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
    }

    public StepEntity getDefiningTolerance() {
        return definingTolerance;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    public Double getZoneWidth() {
        return zoneWidth;
    }

    public Double getZoneHeight() {
        return zoneHeight;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("definingTolerance", definingTolerance);
        state.put("zoneForm", zoneForm);
        state.put("zoneWidth", zoneWidth);
        state.put("zoneHeight", zoneHeight);
        return state;
    }
}
