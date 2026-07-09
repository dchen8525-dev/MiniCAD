package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepLinearToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneLength;

    public StepLinearToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneLength) {
        this.id = id;
        this.name = name;
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneLength = zoneLength;
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

    public Double getZoneLength() {
        return zoneLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLinearToleranceZone that = (StepLinearToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(definingTolerance, that.definingTolerance) && Objects.equals(zoneForm, that.zoneForm) && Objects.equals(zoneLength, that.zoneLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, definingTolerance, zoneForm, zoneLength);
    }

    @Override
    public String toString() {
        return "StepLinearToleranceZone{" + "id=" + id + "name=" + name + "definingTolerance=" + definingTolerance + "zoneForm=" + zoneForm + "zoneLength=" + zoneLength + "}";
    }
}