package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ASSEMBLY_STRUCTURE.
 * An assembly structure entity.
 *
 * @param id STEP instance id
 * @param name assembly name
 * @param rootComponent root component of assembly
 * @param components list of assembly components
 * @param relationships component relationships
 * @param assemblyType assembly type classification
 */
/**
 * Resolved ASSEMBLY_STRUCTURE.
 * An assembly structure entity.
 *
 * @param id STEP instance id
 * @param name assembly name
 * @param rootComponent root component of assembly
 * @param components list of assembly components
 * @param relationships component relationships
 * @param assemblyType assembly type classification
 */
public final class StepAssemblyStructure implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity rootComponent;
    private final List<StepEntity> components;
    private final List<StepEntity> relationships;
    private final String assemblyType;

    public StepAssemblyStructure(int id, String name, StepEntity rootComponent, List<StepEntity> components, List<StepEntity> relationships, String assemblyType) {
        this.id = id;
        this.name = name;
        this.rootComponent = rootComponent;
        this.components = components == null ? null : java.util.List.copyOf(components);
        this.relationships = relationships == null ? null : java.util.List.copyOf(relationships);
        this.assemblyType = assemblyType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRootComponent() {
        return rootComponent;
    }

    public List<StepEntity> getComponents() {
        return components;
    }

    public List<StepEntity> getRelationships() {
        return relationships;
    }

    public String getAssemblyType() {
        return assemblyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblyStructure that = (StepAssemblyStructure) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(rootComponent, that.rootComponent) && Objects.equals(components, that.components) && Objects.equals(relationships, that.relationships) && Objects.equals(assemblyType, that.assemblyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rootComponent, components, relationships, assemblyType);
    }

    @Override
    public String toString() {
        return "StepAssemblyStructure{" + "id=" + id + "name=" + name + "rootComponent=" + rootComponent + "components=" + components + "relationships=" + relationships + "assemblyType=" + assemblyType + "}";
    }
}