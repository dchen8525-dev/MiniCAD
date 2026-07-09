package com.minicad.step.model.technical.unit;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved VOLUME_UNIT_WITH_UNIT.
 */
/**
 * Resolved VOLUME_UNIT_WITH_UNIT.
 */
public final class StepVolumeUnitWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity volumeUnit;
    private final StepEntity unitComponent;

    public StepVolumeUnitWithUnit(int id, String name, StepEntity volumeUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.volumeUnit = volumeUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVolumeUnit() {
        return volumeUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVolumeUnitWithUnit that = (StepVolumeUnitWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(volumeUnit, that.volumeUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, volumeUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepVolumeUnitWithUnit{" + "id=" + id + "name=" + name + "volumeUnit=" + volumeUnit + "unitComponent=" + unitComponent + "}";
    }
}
