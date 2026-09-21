package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TOLERANCE_PAIR.
 * A tolerance pair entity (limits and fits).
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperTolerance upper tolerance value
 * @param lowerTolerance lower tolerance value
 * * @param toleranceUnit tolerance unit
 * @param fitType fit type classification
 */
public final class StepTolerancePair extends AbstractStepEntity {
    private final Double upperTolerance;
    private final Double lowerTolerance;
    private final StepEntity toleranceUnit;
    private final String fitType;

    public StepTolerancePair(int id, String name, Double upperTolerance, Double lowerTolerance, StepEntity toleranceUnit, String fitType) {
        super(id, name);
        this.upperTolerance = upperTolerance;
        this.lowerTolerance = lowerTolerance;
        this.toleranceUnit = toleranceUnit;
        this.fitType = fitType;
    }

    public Double getUpperTolerance() {
        return upperTolerance;
    }

    public Double getLowerTolerance() {
        return lowerTolerance;
    }

    public StepEntity getToleranceUnit() {
        return toleranceUnit;
    }

    public String getFitType() {
        return fitType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("upperTolerance", upperTolerance);
        state.put("lowerTolerance", lowerTolerance);
        state.put("toleranceUnit", toleranceUnit);
        state.put("fitType", fitType);
        return state;
    }
}
