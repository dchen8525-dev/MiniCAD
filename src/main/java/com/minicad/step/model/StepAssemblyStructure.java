package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAssemblyStructure extends AbstractStepEntity {
    private final StepEntity rootComponent;
    private final List<StepEntity> components;
    private final List<StepEntity> relationships;
    private final String assemblyType;

    public StepAssemblyStructure(int id, String name, StepEntity rootComponent, List<StepEntity> components, List<StepEntity> relationships, String assemblyType) {
        super(id, name);
        this.rootComponent = rootComponent;
        this.components = components == null ? null : java.util.List.copyOf(components);
        this.relationships = relationships == null ? null : java.util.List.copyOf(relationships);
        this.assemblyType = assemblyType;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("rootComponent", rootComponent);
        state.put("components", components);
        state.put("relationships", relationships);
        state.put("assemblyType", assemblyType);
        return state;
    }
}
