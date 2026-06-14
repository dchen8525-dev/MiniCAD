package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal uncertainty measure with unit.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 * @param name uncertainty name
 * @param description uncertainty description
 */
/**
 * Minimal uncertainty measure with unit.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 * @param name uncertainty name
 * @param description uncertainty description
 */
public final class StepUncertaintyMeasureWithUnit implements StepEntity {
    private final int id;
    private final double valueComponent;
    private final StepEntity unitComponent;
    private final String name;
    private final String description;

    public StepUncertaintyMeasureWithUnit(int id, double valueComponent, StepEntity unitComponent, String name, String description) {
        this.id = id;
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
        this.name = name;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUncertaintyMeasureWithUnit that = (StepUncertaintyMeasureWithUnit) o;
        return id == that.id && valueComponent == that.valueComponent && Objects.equals(unitComponent, that.unitComponent) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valueComponent, unitComponent, name, description);
    }

    @Override
    public String toString() {
        return "StepUncertaintyMeasureWithUnit{" + "id=" + id + "valueComponent=" + valueComponent + "unitComponent=" + unitComponent + "name=" + name + "description=" + description + "}";
    }
}
