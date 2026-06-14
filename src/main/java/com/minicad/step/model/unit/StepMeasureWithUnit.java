package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal measure-with-unit value.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
/**
 * Minimal measure-with-unit value.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public final class StepMeasureWithUnit implements StepEntity {
    private final int id;
    private final double valueComponent;
    private final StepEntity unitComponent;

    public StepMeasureWithUnit(int id, double valueComponent, StepEntity unitComponent) {
        this.id = id;
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public double getValueComponent() {
        return valueComponent;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasureWithUnit that = (StepMeasureWithUnit) o;
        return id == that.id && valueComponent == that.valueComponent && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valueComponent, unitComponent);
    }

    @Override
    public String toString() {
        return "StepMeasureWithUnit{" + "id=" + id + "valueComponent=" + valueComponent + "unitComponent=" + unitComponent + "}";
    }
}
