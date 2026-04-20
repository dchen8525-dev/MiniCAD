package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAnalysisResult(
    int id,
    String name,
    String resultType,
    StepEntity analysisModel,
    StepEntity resultGeometry,
    List<Double> resultValues,
    List<StepEntity> resultLocations,
    double maxValue,
    double minValue) implements StepEntity {
}