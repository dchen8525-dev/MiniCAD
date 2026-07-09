package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS.
 * Parameters for structural analysis representation.
 */
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS.
 * Parameters for structural analysis representation.
 */
public final class StepStructuralAnalysisRepresentationParameters implements StepEntity {
    private final int id;
    private final String name;
    private final String parameterType;

    public StepStructuralAnalysisRepresentationParameters(int id, String name, String parameterType) {
        this.id = id;
        this.name = name;
        this.parameterType = parameterType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParameterType() {
        return parameterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStructuralAnalysisRepresentationParameters that = (StepStructuralAnalysisRepresentationParameters) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parameterType, that.parameterType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parameterType);
    }

    @Override
    public String toString() {
        return "StepStructuralAnalysisRepresentationParameters{" + "id=" + id + "name=" + name + "parameterType=" + parameterType + "}";
    }
}
