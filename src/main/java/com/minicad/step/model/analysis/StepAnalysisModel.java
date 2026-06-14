package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAnalysisModel implements StepEntity {
    private final int id;
    private final String name;
    private final String modelType;
    private final StepEntity modelGeometry;
    private final StepEntity mesh;
    private final List<StepEntity> boundaryConditions;
    private final List<StepEntity> loads;
    private final List<StepEntity> materialProperties;

    public StepAnalysisModel(int id, String name, String modelType, StepEntity modelGeometry, StepEntity mesh, List<StepEntity> boundaryConditions, List<StepEntity> loads, List<StepEntity> materialProperties) {
        this.id = id;
        this.name = name;
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.mesh = mesh;
        this.boundaryConditions = boundaryConditions == null ? null : java.util.List.copyOf(boundaryConditions);
        this.loads = loads == null ? null : java.util.List.copyOf(loads);
        this.materialProperties = materialProperties == null ? null : java.util.List.copyOf(materialProperties);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnalysisModel that = (StepAnalysisModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modelType, that.modelType) && Objects.equals(modelGeometry, that.modelGeometry) && Objects.equals(mesh, that.mesh) && Objects.equals(boundaryConditions, that.boundaryConditions) && Objects.equals(loads, that.loads) && Objects.equals(materialProperties, that.materialProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelType, modelGeometry, mesh, boundaryConditions, loads, materialProperties);
    }

    @Override
    public String toString() {
        return "StepAnalysisModel{" + "id=" + id + "name=" + name + "modelType=" + modelType + "modelGeometry=" + modelGeometry + "mesh=" + mesh + "boundaryConditions=" + boundaryConditions + "loads=" + loads + "materialProperties=" + materialProperties + "}";
    }
}