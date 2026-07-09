package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ANALYSIS_DEFINITION.
 * An analysis definition entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @param analysisType analysis variance type
 * @param analysisMethod analysis variance method
 * @param analysisInputs analysis variance inputs
 * @param analysisOutputs analysis variance expected outputs
 * @param analysisStatus analysis variance status
 */
/**
 * Resolved ANALYSIS_DEFINITION.
 * An analysis definition entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @param analysisType analysis variance type
 * @param analysisMethod analysis variance method
 * @param analysisInputs analysis variance inputs
 * @param analysisOutputs analysis variance expected outputs
 * @param analysisStatus analysis variance status
 */
public final class StepAnalysisDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String analysisType;
    private final String analysisMethod;
    private final List<String> analysisInputs;
    private final List<String> analysisOutputs;
    private final String analysisStatus;

    public StepAnalysisDefinition(int id, String name, String analysisType, String analysisMethod, List<String> analysisInputs, List<String> analysisOutputs, String analysisStatus) {
        this.id = id;
        this.name = name;
        this.analysisType = analysisType;
        this.analysisMethod = analysisMethod;
        this.analysisInputs = analysisInputs == null ? null : java.util.List.copyOf(analysisInputs);
        this.analysisOutputs = analysisOutputs == null ? null : java.util.List.copyOf(analysisOutputs);
        this.analysisStatus = analysisStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public String getAnalysisMethod() {
        return analysisMethod;
    }

    public List<String> getAnalysisInputs() {
        return analysisInputs;
    }

    public List<String> getAnalysisOutputs() {
        return analysisOutputs;
    }

    public String getAnalysisStatus() {
        return analysisStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnalysisDefinition that = (StepAnalysisDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisType, that.analysisType) && Objects.equals(analysisMethod, that.analysisMethod) && Objects.equals(analysisInputs, that.analysisInputs) && Objects.equals(analysisOutputs, that.analysisOutputs) && Objects.equals(analysisStatus, that.analysisStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisType, analysisMethod, analysisInputs, analysisOutputs, analysisStatus);
    }

    @Override
    public String toString() {
        return "StepAnalysisDefinition{" + "id=" + id + "name=" + name + "analysisType=" + analysisType + "analysisMethod=" + analysisMethod + "analysisInputs=" + analysisInputs + "analysisOutputs=" + analysisOutputs + "analysisStatus=" + analysisStatus + "}";
    }
}