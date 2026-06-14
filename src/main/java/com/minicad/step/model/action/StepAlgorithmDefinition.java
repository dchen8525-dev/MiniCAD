package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ALGORITHM_DEFINITION.
 * An algorithm definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceAlgorithm defined variance algorithm
 * @varianceInputs algorithm variance inputs
 * @varianceOutputs algorithm variance outputs
 * @varianceSteps algorithm variance steps/procedure
 * @varianceComplexity algorithm variance complexity level
 * @varianceStatus definition variance status
 */
/**
 * Resolved ALGORITHM_DEFINITION.
 * An algorithm definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceAlgorithm defined variance algorithm
 * @varianceInputs algorithm variance inputs
 * @varianceOutputs algorithm variance outputs
 * @varianceSteps algorithm variance steps/procedure
 * @varianceComplexity algorithm variance complexity level
 * @varianceStatus definition variance status
 */
public final class StepAlgorithmDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceAlgorithm;
    private final List<StepEntity> varianceInputs;
    private final List<StepEntity> varianceOutputs;
    private final List<String> varianceSteps;
    private final int varianceComplexity;
    private final String varianceStatus;

    public StepAlgorithmDefinition(int id, String name, String varianceAlgorithm, List<StepEntity> varianceInputs, List<StepEntity> varianceOutputs, List<String> varianceSteps, int varianceComplexity, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceAlgorithm = varianceAlgorithm;
        this.varianceInputs = varianceInputs == null ? null : java.util.List.copyOf(varianceInputs);
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.varianceSteps = varianceSteps == null ? null : java.util.List.copyOf(varianceSteps);
        this.varianceComplexity = varianceComplexity;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceAlgorithm() {
        return varianceAlgorithm;
    }

    public List<StepEntity> getVarianceInputs() {
        return varianceInputs;
    }

    public List<StepEntity> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<String> getVarianceSteps() {
        return varianceSteps;
    }

    public int getVarianceComplexity() {
        return varianceComplexity;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAlgorithmDefinition that = (StepAlgorithmDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceAlgorithm, that.varianceAlgorithm) && Objects.equals(varianceInputs, that.varianceInputs) && Objects.equals(varianceOutputs, that.varianceOutputs) && Objects.equals(varianceSteps, that.varianceSteps) && varianceComplexity == that.varianceComplexity && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceAlgorithm, varianceInputs, varianceOutputs, varianceSteps, varianceComplexity, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAlgorithmDefinition{" + "id=" + id + "name=" + name + "varianceAlgorithm=" + varianceAlgorithm + "varianceInputs=" + varianceInputs + "varianceOutputs=" + varianceOutputs + "varianceSteps=" + varianceSteps + "varianceComplexity=" + varianceComplexity + "varianceStatus=" + varianceStatus + "}";
    }
}