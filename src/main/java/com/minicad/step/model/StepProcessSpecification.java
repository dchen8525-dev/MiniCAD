package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROCESS_SPECIFICATION.
 * A process specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceProcess specified variance process
 * @varianceParameters process variance parameters
 * @varianceRanges parameter variance ranges
 * @varianceMaterials material variance requirements
 * @varianceTools tool variance requirements
 * @varianceStatus specification variance status
 */
/**
 * Resolved PROCESS_SPECIFICATION.
 * A process specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceProcess specified variance process
 * @varianceParameters process variance parameters
 * @varianceRanges parameter variance ranges
 * @varianceMaterials material variance requirements
 * @varianceTools tool variance requirements
 * @varianceStatus specification variance status
 */
public final class StepProcessSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceProcess;
    private final List<String> varianceParameters;
    private final List<Double> varianceRanges;
    private final List<StepEntity> varianceMaterials;
    private final List<StepEntity> varianceTools;
    private final String varianceStatus;

    public StepProcessSpecification(int id, String name, StepEntity varianceProcess, List<String> varianceParameters, List<Double> varianceRanges, List<StepEntity> varianceMaterials, List<StepEntity> varianceTools, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProcess = varianceProcess;
        this.varianceParameters = varianceParameters == null ? null : java.util.List.copyOf(varianceParameters);
        this.varianceRanges = varianceRanges == null ? null : java.util.List.copyOf(varianceRanges);
        this.varianceMaterials = varianceMaterials == null ? null : java.util.List.copyOf(varianceMaterials);
        this.varianceTools = varianceTools == null ? null : java.util.List.copyOf(varianceTools);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceProcess() {
        return varianceProcess;
    }

    public List<String> getVarianceParameters() {
        return varianceParameters;
    }

    public List<Double> getVarianceRanges() {
        return varianceRanges;
    }

    public List<StepEntity> getVarianceMaterials() {
        return varianceMaterials;
    }

    public List<StepEntity> getVarianceTools() {
        return varianceTools;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcessSpecification that = (StepProcessSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProcess, that.varianceProcess) && Objects.equals(varianceParameters, that.varianceParameters) && Objects.equals(varianceRanges, that.varianceRanges) && Objects.equals(varianceMaterials, that.varianceMaterials) && Objects.equals(varianceTools, that.varianceTools) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProcess, varianceParameters, varianceRanges, varianceMaterials, varianceTools, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepProcessSpecification{" + "id=" + id + "name=" + name + "varianceProcess=" + varianceProcess + "varianceParameters=" + varianceParameters + "varianceRanges=" + varianceRanges + "varianceMaterials=" + varianceMaterials + "varianceTools=" + varianceTools + "varianceStatus=" + varianceStatus + "}";
    }
}