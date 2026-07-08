package com.minicad.step.model.technical.unit;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal typed measure-with-unit subtype.
 *
 * @param id STEP instance id
 * @param entityName specific STEP entity name such as LENGTH_MEASURE_WITH_UNIT
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
/**
 * Minimal typed measure-with-unit subtype.
 *
 * @param id STEP instance id
 * @param entityName specific STEP entity name such as LENGTH_MEASURE_WITH_UNIT
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public final class StepTypedMeasureWithUnit implements StepEntity {
    private final int id;
    private final String entityName;
    private final double valueComponent;
    private final StepEntity unitComponent;

    public StepTypedMeasureWithUnit(int id, String entityName, double valueComponent, StepEntity unitComponent) {
        this.id = id;
        this.entityName = entityName;
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public double getValueComponent() {
        return valueComponent;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    // Record-style accessor
    public StepEntity unitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTypedMeasureWithUnit that = (StepTypedMeasureWithUnit) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && valueComponent == that.valueComponent && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, valueComponent, unitComponent);
    }

    @Override
    public String toString() {
        return "StepTypedMeasureWithUnit{" + "id=" + id + "entityName=" + entityName + "valueComponent=" + valueComponent + "unitComponent=" + unitComponent + "}";
    }
}
