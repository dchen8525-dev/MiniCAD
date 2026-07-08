package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MEASURE_REPRESENTATION_ITEM_WITH_UNIT.
 * A measure with unit as a representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureValue measure value
 * @param unit unit reference
 */
/**
 * Resolved MEASURE_REPRESENTATION_ITEM_WITH_UNIT.
 * A measure with unit as a representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureValue measure value
 * @param unit unit reference
 */
public final class StepMeasureRepresentationItemWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final double measureValue;
    private final StepEntity unit;

    public StepMeasureRepresentationItemWithUnit(int id, String name, double measureValue, StepEntity unit) {
        this.id = id;
        this.name = name;
        this.measureValue = measureValue;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMeasureValue() {
        return measureValue;
    }

    public StepEntity getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasureRepresentationItemWithUnit that = (StepMeasureRepresentationItemWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && measureValue == that.measureValue && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, measureValue, unit);
    }

    @Override
    public String toString() {
        return "StepMeasureRepresentationItemWithUnit{" + "id=" + id + "name=" + name + "measureValue=" + measureValue + "unit=" + unit + "}";
    }
}