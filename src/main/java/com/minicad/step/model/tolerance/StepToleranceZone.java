package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TOLERANCE_ZONE.
 * Defines a tolerance zone with specific form and appearance.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form tolerance zone form reference
 */
/**
 * Resolved TOLERANCE_ZONE.
 * Defines a tolerance zone with specific form and appearance.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form tolerance zone form reference
 */
public final class StepToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity form;

    public StepToleranceZone(int id, String name, StepEntity form) {
        this.id = id;
        this.name = name;
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getForm() {
        return form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToleranceZone that = (StepToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(form, that.form);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, form);
    }

    @Override
    public String toString() {
        return "StepToleranceZone{" + "id=" + id + "name=" + name + "form=" + form + "}";
    }
}
