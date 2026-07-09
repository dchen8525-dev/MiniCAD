package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal conversion-based unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param conversionFactor referenced conversion factor
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepConversionBasedUnit implements StepEntity {
    private final int id;
    private final String name;
    private final String unitKind;
    private final StepMeasureWithUnit conversionFactor;
    private final String entityName;

    public StepConversionBasedUnit(int id, String name, String unitKind, StepMeasureWithUnit conversionFactor, String entityName) {
        this.id = id;
        this.name = name;
        this.unitKind = unitKind;
        this.conversionFactor = conversionFactor;
        this.entityName = entityName;
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

    public StepMeasureWithUnit getConversionFactor() {
        return conversionFactor;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String unitKind() { return unitKind; }
    public StepMeasureWithUnit conversionFactor() { return conversionFactor; }
    public String entityName() { return entityName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionBasedUnit that = (StepConversionBasedUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(unitKind, that.unitKind) && Objects.equals(conversionFactor, that.conversionFactor) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitKind, conversionFactor, entityName);
    }

    @Override
    public String toString() {
        return "StepConversionBasedUnit{" + "id=" + id + "name=" + name + "unitKind=" + unitKind + "conversionFactor=" + conversionFactor + "entityName=" + entityName + "}";
    }
}
