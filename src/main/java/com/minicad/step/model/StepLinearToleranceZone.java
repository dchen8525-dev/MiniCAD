package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved LINEAR_TOLERANCE_ZONE.
 * A linear tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneLength length of the tolerance zone
 */
public final class StepLinearToleranceZone extends AbstractStepEntity {
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneLength;

    public StepLinearToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneLength) {
        super(id, name);
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneLength = zoneLength;
    }

    public StepEntity getDefiningTolerance() {
        return definingTolerance;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    public Double getZoneLength() {
        return zoneLength;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "definingTolerance", definingTolerance,
                "zoneForm", zoneForm, "zoneLength", zoneLength);
    }
}
