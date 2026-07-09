package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal named unit marker.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
/**
 * Minimal named unit marker.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public final class StepNamedUnit implements StepEntity {
    private final int id;
    private final String unitKind;

    public StepNamedUnit(int id, String unitKind) {
        this.id = id;
        this.unitKind = unitKind;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public String getUnitKind() {
        return unitKind;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return getName(); }
    public String unitKind() { return unitKind; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNamedUnit that = (StepNamedUnit) o;
        return id == that.id && Objects.equals(unitKind, that.unitKind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitKind);
    }

    @Override
    public String toString() {
        return "StepNamedUnit{" + "id=" + id + "unitKind=" + unitKind + "}";
    }
}
