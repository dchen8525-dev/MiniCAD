package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ANALYSIS_INSTANCE.
 * An analysis instance entity.
 *
 * @param id STEP instance id
 * @param name analysis instance name
 * @param analysisDefinition analysis variance definition reference
 * @param analysisState analysis variance state
 * @param analysisResults analysis variance results
 * @param analysisConclusions analysis variance conclusions
 * @param analysisStatus analysis variance status
 */
/**
 * Resolved ANALYSIS_INSTANCE.
 * An analysis instance entity.
 *
 * @param id STEP instance id
 * @param name analysis instance name
 * @param analysisDefinition analysis variance definition reference
 * @param analysisState analysis variance state
 * @param analysisResults analysis variance results
 * @param analysisConclusions analysis variance conclusions
 * @param analysisStatus analysis variance status
 */
public final class StepAnalysisInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity analysisDefinition;
    private final String analysisState;
    private final List<String> analysisResults;
    private final String analysisConclusions;
    private final String analysisStatus;

    public StepAnalysisInstance(int id, String name, StepEntity analysisDefinition, String analysisState, List<String> analysisResults, String analysisConclusions, String analysisStatus) {
        this.id = id;
        this.name = name;
        this.analysisDefinition = analysisDefinition;
        this.analysisState = analysisState;
        this.analysisResults = analysisResults == null ? null : java.util.List.copyOf(analysisResults);
        this.analysisConclusions = analysisConclusions;
        this.analysisStatus = analysisStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAnalysisDefinition() {
        return analysisDefinition;
    }

    public String getAnalysisState() {
        return analysisState;
    }

    public List<String> getAnalysisResults() {
        return analysisResults;
    }

    public String getAnalysisConclusions() {
        return analysisConclusions;
    }

    public String getAnalysisStatus() {
        return analysisStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnalysisInstance that = (StepAnalysisInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisDefinition, that.analysisDefinition) && Objects.equals(analysisState, that.analysisState) && Objects.equals(analysisResults, that.analysisResults) && Objects.equals(analysisConclusions, that.analysisConclusions) && Objects.equals(analysisStatus, that.analysisStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisDefinition, analysisState, analysisResults, analysisConclusions, analysisStatus);
    }

    @Override
    public String toString() {
        return "StepAnalysisInstance{" + "id=" + id + "name=" + name + "analysisDefinition=" + analysisDefinition + "analysisState=" + analysisState + "analysisResults=" + analysisResults + "analysisConclusions=" + analysisConclusions + "analysisStatus=" + analysisStatus + "}";
    }
}