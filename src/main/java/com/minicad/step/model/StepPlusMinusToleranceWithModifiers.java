package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PLUS_MINUS_TOLERANCE_WITH_MODIFIERS.
 * A plus-minus tolerance with modifiers entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperDeviation upper deviation value
 * @param lowerDeviation lower deviation value
 * * @param deviationUnit deviation unit
 * @param modifiers tolerance modifiers
 */
public final class StepPlusMinusToleranceWithModifiers extends AbstractStepEntity {
    private final Double upperDeviation;
    private final Double lowerDeviation;
    private final StepEntity deviationUnit;
    private final List<String> modifiers;

    public StepPlusMinusToleranceWithModifiers(int id, String name, Double upperDeviation, Double lowerDeviation, StepEntity deviationUnit, List<String> modifiers) {
        super(id, name);
        this.upperDeviation = upperDeviation;
        this.lowerDeviation = lowerDeviation;
        this.deviationUnit = deviationUnit;
        this.modifiers = modifiers == null ? null : java.util.List.copyOf(modifiers);
    }

    public Double getUpperDeviation() {
        return upperDeviation;
    }

    public Double getLowerDeviation() {
        return lowerDeviation;
    }

    public StepEntity getDeviationUnit() {
        return deviationUnit;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("upperDeviation", upperDeviation);
        state.put("lowerDeviation", lowerDeviation);
        state.put("deviationUnit", deviationUnit);
        state.put("modifiers", modifiers);
        return state;
    }
}
