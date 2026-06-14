package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTimeEstimation implements StepEntity {
    private final int id;
    private final String name;
    private final String estimationType;
    private final double estimatedTime;
    private final String timeUnit;
    private final List<StepEntity> timeBreakdown;
    private final String estimationMethod;
    private final List<Double> estimationFactors;

    public StepTimeEstimation(int id, String name, String estimationType, double estimatedTime, String timeUnit, List<StepEntity> timeBreakdown, String estimationMethod, List<Double> estimationFactors) {
        this.id = id;
        this.name = name;
        this.estimationType = estimationType;
        this.estimatedTime = estimatedTime;
        this.timeUnit = timeUnit;
        this.timeBreakdown = timeBreakdown == null ? null : java.util.List.copyOf(timeBreakdown);
        this.estimationMethod = estimationMethod;
        this.estimationFactors = estimationFactors == null ? null : java.util.List.copyOf(estimationFactors);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEstimationType() {
        return estimationType;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public List<StepEntity> getTimeBreakdown() {
        return timeBreakdown;
    }

    public String getEstimationMethod() {
        return estimationMethod;
    }

    public List<Double> getEstimationFactors() {
        return estimationFactors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTimeEstimation that = (StepTimeEstimation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(estimationType, that.estimationType) && estimatedTime == that.estimatedTime && Objects.equals(timeUnit, that.timeUnit) && Objects.equals(timeBreakdown, that.timeBreakdown) && Objects.equals(estimationMethod, that.estimationMethod) && Objects.equals(estimationFactors, that.estimationFactors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, estimationType, estimatedTime, timeUnit, timeBreakdown, estimationMethod, estimationFactors);
    }

    @Override
    public String toString() {
        return "StepTimeEstimation{" + "id=" + id + "name=" + name + "estimationType=" + estimationType + "estimatedTime=" + estimatedTime + "timeUnit=" + timeUnit + "timeBreakdown=" + timeBreakdown + "estimationMethod=" + estimationMethod + "estimationFactors=" + estimationFactors + "}";
    }
}