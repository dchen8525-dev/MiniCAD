package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TIME_ESTIMATION.
 * A time estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (setup, operation, total)
 * @param estimatedTime estimated time value
 * @param timeUnit time unit specification
 * @param timeBreakdown time breakdown items
 * @param estimationMethod estimation method used
 * @param estimationFactors estimation factors applied
 */
public record StepTimeEstimation(
    int id,
    String name,
    String estimationType,
    double estimatedTime,
    String timeUnit,
    List<StepEntity> timeBreakdown,
    String estimationMethod,
    List<Double> estimationFactors) implements StepEntity {
}