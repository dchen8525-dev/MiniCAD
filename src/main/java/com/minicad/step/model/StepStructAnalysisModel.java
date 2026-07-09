package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved STRUCT_ANALYSIS_MODEL.
 * A structural analysis model (AP209).
 */
/**
 * Resolved STRUCT_ANALYSIS_MODEL.
 * A structural analysis model (AP209).
 */
public final class StepStructAnalysisModel implements StepEntity {
    private final int id;
    private final String name;
    private final String analysisType;

    public StepStructAnalysisModel(int id, String name, String analysisType) {
        this.id = id;
        this.name = name;
        this.analysisType = analysisType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStructAnalysisModel that = (StepStructAnalysisModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisType, that.analysisType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisType);
    }

    @Override
    public String toString() {
        return "StepStructAnalysisModel{" + "id=" + id + "name=" + name + "analysisType=" + analysisType + "}";
    }
}
