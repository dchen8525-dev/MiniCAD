package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ANALYSIS_MODEL.
 * An analysis model entity.
 *
 * @param id STEP instance id
 * @param name model name
 * @param modelType analysis model type (structural, thermal, fluid)
 * @param modelGeometry geometry for analysis
 * @param mesh mesh representation
 * @param boundaryConditions boundary conditions
 * @param loads applied loads
 * @param materialProperties material properties for analysis
 */
public final class StepAnalysisModel extends AbstractStepEntity {
    private final String modelType;
    private final StepEntity modelGeometry;
    private final StepEntity mesh;
    private final List<StepEntity> boundaryConditions;
    private final List<StepEntity> loads;
    private final List<StepEntity> materialProperties;

    public StepAnalysisModel(int id, String name, String modelType, StepEntity modelGeometry, StepEntity mesh, List<StepEntity> boundaryConditions, List<StepEntity> loads, List<StepEntity> materialProperties) {
        super(id, name);
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.mesh = mesh;
        this.boundaryConditions = boundaryConditions == null ? null : java.util.List.copyOf(boundaryConditions);
        this.loads = loads == null ? null : java.util.List.copyOf(loads);
        this.materialProperties = materialProperties == null ? null : java.util.List.copyOf(materialProperties);
    }

    public String getModelType() {
        return modelType;
    }

    public StepEntity getModelGeometry() {
        return modelGeometry;
    }

    public StepEntity getMesh() {
        return mesh;
    }

    public List<StepEntity> getBoundaryConditions() {
        return boundaryConditions;
    }

    public List<StepEntity> getLoads() {
        return loads;
    }

    public List<StepEntity> getMaterialProperties() {
        return materialProperties;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modelType", modelType);
        state.put("modelGeometry", modelGeometry);
        state.put("mesh", mesh);
        state.put("boundaryConditions", boundaryConditions);
        state.put("loads", loads);
        state.put("materialProperties", materialProperties);
        return state;
    }
}
