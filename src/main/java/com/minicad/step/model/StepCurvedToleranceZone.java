package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved CURVED_TOLERANCE_ZONE.
 * A curved tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneCurve the curve defining the tolerance zone shape
 */
/**
 * Resolved CURVED_TOLERANCE_ZONE.
 * A curved tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneCurve the curve defining the tolerance zone shape
 */
public final class StepCurvedToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity definingTolerance;
    private final StepEntity zoneForm;
    private final StepEntity zoneCurve;

    public StepCurvedToleranceZone(int id, String name, StepEntity definingTolerance, StepEntity zoneForm, StepEntity zoneCurve) {
        this.id = id;
        this.name = name;
        this.definingTolerance = definingTolerance;
        this.zoneForm = zoneForm;
        this.zoneCurve = zoneCurve;
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

    public StepEntity getZoneCurve() {
        return zoneCurve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurvedToleranceZone that = (StepCurvedToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(definingTolerance, that.definingTolerance) && Objects.equals(zoneForm, that.zoneForm) && Objects.equals(zoneCurve, that.zoneCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, definingTolerance, zoneForm, zoneCurve);
    }

    @Override
    public String toString() {
        return "StepCurvedToleranceZone{" + "id=" + id + "name=" + name + "definingTolerance=" + definingTolerance + "zoneForm=" + zoneForm + "zoneCurve=" + zoneCurve + "}";
    }
}