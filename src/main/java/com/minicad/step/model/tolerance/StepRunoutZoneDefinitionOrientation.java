package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RUNOUT_ZONE_DEFINITION_ORIENTATION.
 * Defines the orientation of a runout tolerance zone.
 */
/**
 * Resolved RUNOUT_ZONE_DEFINITION_ORIENTATION.
 * Defines the orientation of a runout tolerance zone.
 */
public final class StepRunoutZoneDefinitionOrientation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity runoutZone;
    private final StepEntity orientationAxis;
    private final Double angle;

    public StepRunoutZoneDefinitionOrientation(int id, String name, StepEntity runoutZone, StepEntity orientationAxis, Double angle) {
        this.id = id;
        this.name = name;
        this.runoutZone = runoutZone;
        this.orientationAxis = orientationAxis;
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRunoutZoneDefinitionOrientation that = (StepRunoutZoneDefinitionOrientation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(runoutZone, that.runoutZone) && Objects.equals(orientationAxis, that.orientationAxis) && Objects.equals(angle, that.angle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, runoutZone, orientationAxis, angle);
    }

    @Override
    public String toString() {
        return "StepRunoutZoneDefinitionOrientation{" + "id=" + id + "name=" + name + "runoutZone=" + runoutZone + "orientationAxis=" + orientationAxis + "angle=" + angle + "}";
    }
}
