package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved RECTANGULAR_TOLERANCE_ZONE.
 * A rectangular tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneWidth width of the tolerance zone
 * @param zoneHeight height of the tolerance zone
 */
/**
 * Resolved RECTANGULAR_TOLERANCE_ZONE.
 * A rectangular tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneWidth width of the tolerance zone
 * @param zoneHeight height of the tolerance zone
 */
public final class StepRectangularToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneWidth;
    private final Double zoneHeight;

    public StepRectangularToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneWidth, Double zoneHeight) {
        this.id = id;
        this.name = name;
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getDefiningTolerance() {
        return definingTolerance;
    }

    public StepEntity getZoneForm() {
        return zoneForm;
    }

    public Double getZoneWidth() {
        return zoneWidth;
    }

    public Double getZoneHeight() {
        return zoneHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRectangularToleranceZone that = (StepRectangularToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(definingTolerance, that.definingTolerance) && Objects.equals(zoneForm, that.zoneForm) && Objects.equals(zoneWidth, that.zoneWidth) && Objects.equals(zoneHeight, that.zoneHeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, definingTolerance, zoneForm, zoneWidth, zoneHeight);
    }

    @Override
    public String toString() {
        return "StepRectangularToleranceZone{" + "id=" + id + "name=" + name + "definingTolerance=" + definingTolerance + "zoneForm=" + zoneForm + "zoneWidth=" + zoneWidth + "zoneHeight=" + zoneHeight + "}";
    }
}