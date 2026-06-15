package com.minicad.step.model.base;

import java.util.Objects;

/**
 * Minimal measure representation item for native validation payloads.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureType typed measure wrapper name
 * @param value numeric value
 * @param unit unit reference
 */
/**
 * Minimal measure representation item for native validation payloads.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureType typed measure wrapper name
 * @param value numeric value
 * @param unit unit reference
 */
public final class StepMeasureRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String measureType;
    private final double value;
    private final StepEntity unit;

    public StepMeasureRepresentationItem(int id, String name, String measureType, double value, StepEntity unit) {
        this.id = id;
        this.name = name;
        this.measureType = measureType;
        this.value = value;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeasureType() {
        return measureType;
    }

    public double getValue() {
        return value;
    }

    public StepEntity getUnit() {
        return unit;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String measureType() {
        return measureType;
    }

    public double value() {
        return value;
    }

    public StepEntity unit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasureRepresentationItem that = (StepMeasureRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(measureType, that.measureType) && value == that.value && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, measureType, value, unit);
    }

    @Override
    public String toString() {
        return "StepMeasureRepresentationItem{" + "id=" + id + "name=" + name + "measureType=" + measureType + "value=" + value + "unit=" + unit + "}";
    }
}
