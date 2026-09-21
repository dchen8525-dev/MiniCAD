package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RUNOUT_ZONE_DEFINITION.
 * Defines the orientation and form of a runout tolerance zone.
 */
public final class StepRunoutZoneDefinition extends AbstractStepEntity {
    private final StepEntity zoneForm;

    public StepRunoutZoneDefinition(int id, String name, StepEntity zoneForm) {
        super(id, name);
        this.zoneForm = zoneForm;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneForm", zoneForm);
        return state;
    }
}
