package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved STRUCTURAL_ANALYSIS_MODEL.
 * A structural analysis model for FEA.
 */
/**
 * Resolved STRUCTURAL_ANALYSIS_MODEL.
 * A structural analysis model for FEA.
 */
public final class StepStructuralAnalysisModel implements StepEntity {
    private final int id;
    private final String name;
    private final String analysisType;
    private final List<StepEntity> elements;
    private final List<StepEntity> loads;
    private final List<StepEntity> boundaryConditions;

    public StepStructuralAnalysisModel(int id, String name, String analysisType, List<StepEntity> elements, List<StepEntity> loads, List<StepEntity> boundaryConditions) {
        this.id = id;
        this.name = name;
        this.analysisType = analysisType;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.loads = loads == null ? null : java.util.List.copyOf(loads);
        this.boundaryConditions = boundaryConditions == null ? null : java.util.List.copyOf(boundaryConditions);
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

    public List<StepEntity> getElements() {
        return elements;
    }

    public List<StepEntity> getLoads() {
        return loads;
    }

    public List<StepEntity> getBoundaryConditions() {
        return boundaryConditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStructuralAnalysisModel that = (StepStructuralAnalysisModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisType, that.analysisType) && Objects.equals(elements, that.elements) && Objects.equals(loads, that.loads) && Objects.equals(boundaryConditions, that.boundaryConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisType, elements, loads, boundaryConditions);
    }

    @Override
    public String toString() {
        return "StepStructuralAnalysisModel{" + "id=" + id + "name=" + name + "analysisType=" + analysisType + "elements=" + elements + "loads=" + loads + "boundaryConditions=" + boundaryConditions + "}";
    }
}
