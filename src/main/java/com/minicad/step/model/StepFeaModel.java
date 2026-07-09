package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved FEA_MODEL.
 * A finite element analysis model.
 */
/**
 * Resolved FEA_MODEL.
 * A finite element analysis model.
 */
public final class StepFeaModel implements StepEntity {
    private final int id;
    private final String name;
    private final String modelType;
    private final List<StepEntity> elements;
    private final List<StepEntity> loads;
    private final List<StepEntity> boundaryConditions;

    public StepFeaModel(int id, String name, String modelType, List<StepEntity> elements, List<StepEntity> loads, List<StepEntity> boundaryConditions) {
        this.id = id;
        this.name = name;
        this.modelType = modelType;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaModel that = (StepFeaModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modelType, that.modelType) && Objects.equals(elements, that.elements) && Objects.equals(loads, that.loads) && Objects.equals(boundaryConditions, that.boundaryConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelType, elements, loads, boundaryConditions);
    }

    @Override
    public String toString() {
        return "StepFeaModel{" + "id=" + id + "name=" + name + "modelType=" + modelType + "elements=" + elements + "loads=" + loads + "boundaryConditions=" + boundaryConditions + "}";
    }
}
