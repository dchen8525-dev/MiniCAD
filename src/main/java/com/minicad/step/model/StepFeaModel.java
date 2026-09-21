package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_MODEL.
 * A finite element analysis model.
 */
public final class StepFeaModel extends AbstractStepEntity {
    private final String modelType;
    private final List<StepEntity> elements;
    private final List<StepEntity> loads;
    private final List<StepEntity> boundaryConditions;

    public StepFeaModel(int id, String name, String modelType, List<StepEntity> elements, List<StepEntity> loads, List<StepEntity> boundaryConditions) {
        super(id, name);
        this.modelType = modelType;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.loads = loads == null ? null : java.util.List.copyOf(loads);
        this.boundaryConditions = boundaryConditions == null ? null : java.util.List.copyOf(boundaryConditions);
    }

    public String getModelType() {
        return modelType;
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
        state.put("modelType", modelType);
        state.put("elements", elements);
        state.put("loads", loads);
        state.put("boundaryConditions", boundaryConditions);
        return state;
    }
}
