package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal context-dependent unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
/**
 * Minimal context-dependent unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public final class StepContextDependentUnit implements StepEntity {
    private final int id;
    private final String name;
    private final String unitKind;

    public StepContextDependentUnit(int id, String name, String unitKind) {
        this.id = id;
        this.name = name;
        this.unitKind = unitKind;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitKind() {
        return unitKind;
    }

    // Record-style accessor
    public String unitKind() { return unitKind; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepContextDependentUnit that = (StepContextDependentUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(unitKind, that.unitKind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitKind);
    }

    @Override
    public String toString() {
        return "StepContextDependentUnit{" + "id=" + id + "name=" + name + "unitKind=" + unitKind + "}";
    }
}
