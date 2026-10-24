package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved ANALYSIS_RESULT.
 * An analysis result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param resultType result type (stress, displacement, temperature)
 * @param analysisModel reference analysis model
 * @param resultGeometry result geometry with computed values
 * @param resultValues computed result values
 * @param resultLocations locations of result values
 * @param maxValue maximum result value
 * @param minValue minimum result value
 */
public final class StepAnalysisResult extends AbstractStepEntity {
    private final String resultType;
    private final StepEntity analysisModel;
    private final StepEntity resultGeometry;
    private final List<Double> resultValues;
    private final List<StepEntity> resultLocations;
    private final double maxValue;
    private final double minValue;

    public StepAnalysisResult(int id, String name, String resultType, StepEntity analysisModel, StepEntity resultGeometry, List<Double> resultValues, List<StepEntity> resultLocations, double maxValue, double minValue) {
        super(id, name);
        this.resultType = resultType;
        this.analysisModel = analysisModel;
        this.resultGeometry = resultGeometry;
        this.resultValues = resultValues == null ? null : java.util.List.copyOf(resultValues);
        this.resultLocations = resultLocations == null ? null : java.util.List.copyOf(resultLocations);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public String getResultType() {
        return resultType;
    }

    public StepEntity getAnalysisModel() {
        return analysisModel;
    }

    public StepEntity getResultGeometry() {
        return resultGeometry;
    }

    public List<Double> getResultValues() {
        return resultValues;
    }

    public List<StepEntity> getResultLocations() {
        return resultLocations;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "resultType", resultType, "analysisModel", analysisModel,
                "resultGeometry", resultGeometry, "resultValues", resultValues,
                "resultLocations", resultLocations, "maxValue", maxValue, "minValue", minValue);
    }
}
