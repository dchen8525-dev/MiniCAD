package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CURVED_TOLERANCE_ZONE.
 * A curved tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneCurve the curve defining the tolerance zone shape
 */
public final class StepCurvedToleranceZone extends AbstractStepEntity {
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final StepEntity zoneCurve;

    public StepCurvedToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, StepEntity zoneCurve) {
        super(id, name);
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneCurve = zoneCurve;
    }

    public StepEntity getDefiningTolerance() {
        return definingTolerance;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    public StepEntity getZoneCurve() {
        return zoneCurve;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("definingTolerance", definingTolerance);
        state.put("zoneForm", zoneForm);
        state.put("zoneCurve", zoneCurve);
        return state;
    }
}
