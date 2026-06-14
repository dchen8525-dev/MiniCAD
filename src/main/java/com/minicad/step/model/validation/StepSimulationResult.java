package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SIMULATION_RESULT.
 * A simulation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceModel simulation variance model reference
 * @varianceScenario simulation variance scenario
 * @varianceOutputs simulation variance output values
 * @varianceTime simulation variance time steps
 * @varianceConvergence convergence variance status
 * @varianceStatus result variance status
 */
/**
 * Resolved SIMULATION_RESULT.
 * A simulation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceModel simulation variance model reference
 * @varianceScenario simulation variance scenario
 * @varianceOutputs simulation variance output values
 * @varianceTime simulation variance time steps
 * @varianceConvergence convergence variance status
 * @varianceStatus result variance status
 */
public final class StepSimulationResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceModel;
    private final StepEntity varianceScenario;
    private final List<Double> varianceOutputs;
    private final List<Double> varianceTime;
    private final boolean varianceConvergence;
    private final String varianceStatus;

    public StepSimulationResult(int id, String name, StepEntity varianceModel, StepEntity varianceScenario, List<Double> varianceOutputs, List<Double> varianceTime, boolean varianceConvergence, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceModel = varianceModel;
        this.varianceScenario = varianceScenario;
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.varianceTime = varianceTime == null ? null : java.util.List.copyOf(varianceTime);
        this.varianceConvergence = varianceConvergence;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceModel() {
        return varianceModel;
    }

    public StepEntity getVarianceScenario() {
        return varianceScenario;
    }

    public List<Double> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<Double> getVarianceTime() {
        return varianceTime;
    }

    public boolean isVarianceConvergence() {
        return varianceConvergence;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSimulationResult that = (StepSimulationResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceModel, that.varianceModel) && Objects.equals(varianceScenario, that.varianceScenario) && Objects.equals(varianceOutputs, that.varianceOutputs) && Objects.equals(varianceTime, that.varianceTime) && varianceConvergence == that.varianceConvergence && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceModel, varianceScenario, varianceOutputs, varianceTime, varianceConvergence, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSimulationResult{" + "id=" + id + "name=" + name + "varianceModel=" + varianceModel + "varianceScenario=" + varianceScenario + "varianceOutputs=" + varianceOutputs + "varianceTime=" + varianceTime + "varianceConvergence=" + varianceConvergence + "varianceStatus=" + varianceStatus + "}";
    }
}