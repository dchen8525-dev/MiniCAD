package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FUNCTION_SPECIFICATION.
 * A function specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceFunction specified variance function
 * @varianceInputs function variance inputs
 * @varianceOutputs function variance outputs
 * @variancePerformance performance variance requirements
 * @varianceReliability reliability variance requirements
 * @varianceStatus specification variance status
 */
/**
 * Resolved FUNCTION_SPECIFICATION.
 * A function specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceFunction specified variance function
 * @varianceInputs function variance inputs
 * @varianceOutputs function variance outputs
 * @variancePerformance performance variance requirements
 * @varianceReliability reliability variance requirements
 * @varianceStatus specification variance status
 */
public final class StepFunctionSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceFunction;
    private final List<StepEntity> varianceInputs;
    private final List<StepEntity> varianceOutputs;
    private final List<Double> variancePerformance;
    private final List<Double> varianceReliability;
    private final String varianceStatus;

    public StepFunctionSpecification(int id, String name, String varianceFunction, List<StepEntity> varianceInputs, List<StepEntity> varianceOutputs, List<Double> variancePerformance, List<Double> varianceReliability, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceFunction = varianceFunction;
        this.varianceInputs = varianceInputs == null ? null : java.util.List.copyOf(varianceInputs);
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.variancePerformance = variancePerformance == null ? null : java.util.List.copyOf(variancePerformance);
        this.varianceReliability = varianceReliability == null ? null : java.util.List.copyOf(varianceReliability);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceFunction() {
        return varianceFunction;
    }

    public List<StepEntity> getVarianceInputs() {
        return varianceInputs;
    }

    public List<StepEntity> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<Double> getVariancePerformance() {
        return variancePerformance;
    }

    public List<Double> getVarianceReliability() {
        return varianceReliability;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFunctionSpecification that = (StepFunctionSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceFunction, that.varianceFunction) && Objects.equals(varianceInputs, that.varianceInputs) && Objects.equals(varianceOutputs, that.varianceOutputs) && Objects.equals(variancePerformance, that.variancePerformance) && Objects.equals(varianceReliability, that.varianceReliability) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceFunction, varianceInputs, varianceOutputs, variancePerformance, varianceReliability, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepFunctionSpecification{" + "id=" + id + "name=" + name + "varianceFunction=" + varianceFunction + "varianceInputs=" + varianceInputs + "varianceOutputs=" + varianceOutputs + "variancePerformance=" + variancePerformance + "varianceReliability=" + varianceReliability + "varianceStatus=" + varianceStatus + "}";
    }
}