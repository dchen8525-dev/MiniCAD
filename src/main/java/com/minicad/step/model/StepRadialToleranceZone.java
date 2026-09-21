package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RADIAL_TOLERANCE_ZONE.
 * A radial tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneRadius radius of the tolerance zone
 */
public final class StepRadialToleranceZone extends AbstractStepEntity {
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneRadius;

    public StepRadialToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneRadius) {
        super(id, name);
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneRadius = zoneRadius;
    }

    public StepEntity getDefiningTolerance() {
        return definingTolerance;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    public Double getZoneRadius() {
        return zoneRadius;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("definingTolerance", definingTolerance);
        state.put("zoneForm", zoneForm);
        state.put("zoneRadius", zoneRadius);
        return state;
    }
}
