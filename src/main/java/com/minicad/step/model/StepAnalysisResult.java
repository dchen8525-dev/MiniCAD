package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAnalysisResult implements StepEntity {
    private final int id;
    private final String name;
    private final String resultType;
    private final StepEntity analysisModel;
    private final StepEntity resultGeometry;
    private final List<Double> resultValues;
    private final List<StepEntity> resultLocations;
    private final double maxValue;
    private final double minValue;

    public StepAnalysisResult(int id, String name, String resultType, StepEntity analysisModel, StepEntity resultGeometry, List<Double> resultValues, List<StepEntity> resultLocations, double maxValue, double minValue) {
        this.id = id;
        this.name = name;
        this.resultType = resultType;
        this.analysisModel = analysisModel;
        this.resultGeometry = resultGeometry;
        this.resultValues = resultValues == null ? null : java.util.List.copyOf(resultValues);
        this.resultLocations = resultLocations == null ? null : java.util.List.copyOf(resultLocations);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnalysisResult that = (StepAnalysisResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(resultType, that.resultType) && Objects.equals(analysisModel, that.analysisModel) && Objects.equals(resultGeometry, that.resultGeometry) && Objects.equals(resultValues, that.resultValues) && Objects.equals(resultLocations, that.resultLocations) && maxValue == that.maxValue && minValue == that.minValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resultType, analysisModel, resultGeometry, resultValues, resultLocations, maxValue, minValue);
    }

    @Override
    public String toString() {
        return "StepAnalysisResult{" + "id=" + id + "name=" + name + "resultType=" + resultType + "analysisModel=" + analysisModel + "resultGeometry=" + resultGeometry + "resultValues=" + resultValues + "resultLocations=" + resultLocations + "maxValue=" + maxValue + "minValue=" + minValue + "}";
    }
}