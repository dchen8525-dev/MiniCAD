package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal conversion-based unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param conversionFactor referenced conversion factor
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepConversionBasedUnit extends AbstractStepEntity {
    private final String unitKind;
    private final StepMeasureWithUnit conversionFactor;
    private final String entityName;

    public StepConversionBasedUnit(int id, String name, String unitKind, StepMeasureWithUnit conversionFactor, String entityName) {
        super(id, name);
        this.unitKind = unitKind;
        this.conversionFactor = conversionFactor;
        this.entityName = entityName;
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
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String unitKind() { return unitKind; }
    public StepMeasureWithUnit conversionFactor() { return conversionFactor; }
    public String entityName() { return entityName; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("unitKind", unitKind);
        state.put("conversionFactor", conversionFactor);
        state.put("entityName", entityName);
        return state;
    }
}
