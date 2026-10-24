package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved RUNOUT_ZONE_DEFINITION_ORIENTATION.
 * Defines the orientation of a runout tolerance zone.
 */
public final class StepRunoutZoneDefinitionOrientation extends AbstractStepEntity {
    private final StepEntity runoutZone;
    private final StepEntity orientationAxis;
    private final Double angle;

    public StepRunoutZoneDefinitionOrientation(int id, String name, StepEntity runoutZone, StepEntity orientationAxis, Double angle) {
        super(id, name);
        this.runoutZone = runoutZone;
        this.orientationAxis = orientationAxis;
        this.angle = angle;
    }

    public StepEntity getRunoutZone() {
        return runoutZone;
    }

    public StepEntity getOrientationAxis() {
        return orientationAxis;
    }

    public Double getAngle() {
        return angle;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "runoutZone", runoutZone,
                "orientationAxis", orientationAxis, "angle", angle);
    }
}
