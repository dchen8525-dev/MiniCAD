package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STRUCTURAL_ANALYSIS_MODEL.
 * A structural analysis model for FEA.
 */
public final class StepStructuralAnalysisModel extends AbstractStepEntity {
    private final String analysisType;
    private final List<StepEntity> elements;
    private final List<StepEntity> loads;
    private final List<StepEntity> boundaryConditions;

    public StepStructuralAnalysisModel(int id, String name, String analysisType, List<StepEntity> elements, List<StepEntity> loads, List<StepEntity> boundaryConditions) {
        super(id, name);
        this.analysisType = analysisType;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.loads = loads == null ? null : java.util.List.copyOf(loads);
        this.boundaryConditions = boundaryConditions == null ? null : java.util.List.copyOf(boundaryConditions);
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("analysisType", analysisType);
        state.put("elements", elements);
        state.put("loads", loads);
        state.put("boundaryConditions", boundaryConditions);
        return state;
    }
}
