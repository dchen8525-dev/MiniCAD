package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis1Placement;
import java.util.Objects;
/**
 * Resolved REVOLVED_AREA_SOLID_TAPERED.
 * A revolved solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to revolve
 * @param axis axis of revolution
 * @param angle revolution angle
 * @param taperAngle taper angle
 */
/**
 * Resolved REVOLVED_AREA_SOLID_TAPERED.
 * A revolved solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to revolve
 * @param axis axis of revolution
 * @param angle revolution angle
 * @param taperAngle taper angle
 */
public final class StepRevolvedAreaSolidTapered implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptArea;
    private final StepAxis1Placement axis;
    private final double angle;
    private final double taperAngle;

    public StepRevolvedAreaSolidTapered(int id, String name, StepEntity sweptArea, StepAxis1Placement axis, double angle, double taperAngle) {
        this.id = id;
        this.name = name;
        this.sweptArea = sweptArea;
        this.axis = axis;
        this.angle = angle;
        this.taperAngle = taperAngle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSweptArea() {
        return sweptArea;
    }

    public StepAxis1Placement getAxis() {
        return axis;
    }

    public double getAngle() {
        return angle;
    }

    public double getTaperAngle() {
        return taperAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRevolvedAreaSolidTapered that = (StepRevolvedAreaSolidTapered) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptArea, that.sweptArea) && Objects.equals(axis, that.axis) && angle == that.angle && taperAngle == that.taperAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptArea, axis, angle, taperAngle);
    }

    @Override
    public String toString() {
        return "StepRevolvedAreaSolidTapered{" + "id=" + id + "name=" + name + "sweptArea=" + sweptArea + "axis=" + axis + "angle=" + angle + "taperAngle=" + taperAngle + "}";
    }
}
