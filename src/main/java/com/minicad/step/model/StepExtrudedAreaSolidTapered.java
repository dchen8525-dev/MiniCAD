package com.minicad.step.model;

import com.minicad.step.model.StepEntity;

import com.minicad.step.model.StepDirection;
import java.util.Objects;
/**
 * Resolved EXTRUDED_AREA_SOLID_TAPERED.
 * An extruded solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to extrude
 * @param direction extrusion direction
 * @param depth extrusion depth
 * @param taperAngle taper angle
 */
/**
 * Resolved EXTRUDED_AREA_SOLID_TAPERED.
 * An extruded solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to extrude
 * @param direction extrusion direction
 * @param depth extrusion depth
 * @param taperAngle taper angle
 */
public final class StepExtrudedAreaSolidTapered implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptArea;
    private final StepDirection direction;
    private final double depth;
    private final double taperAngle;

    public StepExtrudedAreaSolidTapered(int id, String name, StepEntity sweptArea, StepDirection direction, double depth, double taperAngle) {
        this.id = id;
        this.name = name;
        this.sweptArea = sweptArea;
        this.direction = direction;
        this.depth = depth;
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

    public StepDirection getDirection() {
        return direction;
    }

    public double getDepth() {
        return depth;
    }

    public double getTaperAngle() {
        return taperAngle;
    }

    // Record-style accessors
    public StepEntity sweptArea() { return sweptArea; }
    public StepDirection direction() { return direction; }
    public double depth() { return depth; }
    public double taperAngle() { return taperAngle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExtrudedAreaSolidTapered that = (StepExtrudedAreaSolidTapered) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptArea, that.sweptArea) && Objects.equals(direction, that.direction) && depth == that.depth && taperAngle == that.taperAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptArea, direction, depth, taperAngle);
    }

    @Override
    public String toString() {
        return "StepExtrudedAreaSolidTapered{" + "id=" + id + "name=" + name + "sweptArea=" + sweptArea + "direction=" + direction + "depth=" + depth + "taperAngle=" + taperAngle + "}";
    }
}
