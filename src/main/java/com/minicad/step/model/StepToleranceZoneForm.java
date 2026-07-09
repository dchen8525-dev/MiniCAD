package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TOLERANCE_ZONE_FORM.
 * Defines the shape of a tolerance zone (e.g., cylindrical, spherical, planar).
 *
 * @param id STEP instance id
 * @param name form name
 * @param zoneShape the shape description for the tolerance zone
 */
/**
 * Resolved TOLERANCE_ZONE_FORM.
 * Defines the shape of a tolerance zone (e.g., cylindrical, spherical, planar).
 *
 * @param id STEP instance id
 * @param name form name
 * @param zoneShape the shape description for the tolerance zone
 */
public final class StepToleranceZoneForm implements StepEntity {
    private final int id;
    private final String name;
    private final String zoneShape;

    public StepToleranceZoneForm(int id, String name, String zoneShape) {
        this.id = id;
        this.name = name;
        this.zoneShape = zoneShape;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZoneShape() {
        return zoneShape;
    }

    // Record-style accessor
    public String zoneShape() {
        return zoneShape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToleranceZoneForm that = (StepToleranceZoneForm) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneShape, that.zoneShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneShape);
    }

    @Override
    public String toString() {
        return "StepToleranceZoneForm{" + "id=" + id + "name=" + name + "zoneShape=" + zoneShape + "}";
    }
}
