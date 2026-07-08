package com.minicad.step.model.technical.unit;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SI unit definition.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param prefix optional SI prefix enum name
 * @param unitName SI base unit enum name
 */
/**
 * Minimal SI unit definition.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param prefix optional SI prefix enum name
 * @param unitName SI base unit enum name
 */
public final class StepSiUnit implements StepEntity {
    private final int id;
    private final String unitKind;
    private final String prefix;
    private final String unitName;

    public StepSiUnit(int id, String unitKind, String prefix, String unitName) {
        this.id = id;
        this.unitKind = unitKind;
        this.prefix = prefix;
        this.unitName = unitName;
    }

    public int getId() {
        return id;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getUnitName() {
        return unitName;
    }

    // Record-style accessors
    public int id() { return id; }
    public String getName() { return ""; }
    public String name() { return getName(); }
    public String unitKind() { return unitKind; }
    public String prefix() { return prefix; }
    public String unitName() { return unitName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSiUnit that = (StepSiUnit) o;
        return id == that.id && Objects.equals(unitKind, that.unitKind) && Objects.equals(prefix, that.prefix) && Objects.equals(unitName, that.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitKind, prefix, unitName);
    }

    @Override
    public String toString() {
        return "StepSiUnit{" + "id=" + id + "unitKind=" + unitKind + "prefix=" + prefix + "unitName=" + unitName + "}";
    }
}
