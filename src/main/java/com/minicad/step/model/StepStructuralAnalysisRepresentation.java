package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION.
 * A structural analysis representation.
 */
public final class StepStructuralAnalysisRepresentation extends AbstractStepEntity {
    private final String analysisType;
    private final List<StepEntity> items;

    public StepStructuralAnalysisRepresentation(int id, String name, String analysisType, List<StepEntity> items) {
        super(id, name);
        this.analysisType = analysisType;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("analysisType", analysisType);
        state.put("items", items);
        return state;
    }
}
