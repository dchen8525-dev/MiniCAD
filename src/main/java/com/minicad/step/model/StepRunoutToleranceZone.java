package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved RUNOUT_TOLERANCE_ZONE.
 * A tolerance zone specifically for runout tolerances.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form zone form reference
 */
/**
 * Resolved RUNOUT_TOLERANCE_ZONE.
 * A tolerance zone specifically for runout tolerances.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form zone form reference
 */
public final class StepRunoutToleranceZone implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity form;

    public StepRunoutToleranceZone(int id, String name, StepEntity form) {
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
        StepRunoutToleranceZone that = (StepRunoutToleranceZone) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(form, that.form);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, form);
    }

    @Override
    public String toString() {
        return "StepRunoutToleranceZone{" + "id=" + id + "name=" + name + "form=" + form + "}";
    }
}
