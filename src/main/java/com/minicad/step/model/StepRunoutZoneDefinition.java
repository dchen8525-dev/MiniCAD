package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "zoneForm", zoneForm);
    }
}
