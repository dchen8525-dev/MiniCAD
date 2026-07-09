package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RADIAL_TOLERANCE_ZONE.
 * A radial tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneRadius radius of the tolerance zone
 */
/**
 * Resolved RADIAL_TOLERANCE_ZONE.
 * A radial tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneRadius radius of the tolerance zone
 */
public final class StepRadialToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final Double zoneRadius;

    public StepRadialToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, Double zoneRadius) {
        this.id = id;
        this.name = name;
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneRadius = zoneRadius;
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

    public Double getZoneRadius() {
        return zoneRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRadialToleranceZone that = (StepRadialToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(definingTolerance, that.definingTolerance) && Objects.equals(zoneForm, that.zoneForm) && Objects.equals(zoneRadius, that.zoneRadius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, definingTolerance, zoneForm, zoneRadius);
    }

    @Override
    public String toString() {
        return "StepRadialToleranceZone{" + "id=" + id + "name=" + name + "definingTolerance=" + definingTolerance + "zoneForm=" + zoneForm + "zoneRadius=" + zoneRadius + "}";
    }
}