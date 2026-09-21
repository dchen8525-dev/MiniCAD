package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FINITE_ELEMENT_MESH.
 * A finite element mesh entity.
 *
 * @param id STEP instance id
 * @param name mesh name
 * @param meshType mesh type (shell, solid, beam)
 * @param nodes mesh nodes
 * @param elements mesh elements
 * @param elementTypes element type specifications
 * @param meshDensity mesh density parameter
 */
public final class StepFiniteElementMesh extends AbstractStepEntity {
    private final String meshType;
    private final List<StepEntity> nodes;
    private final List<StepEntity> elements;
    private final List<String> elementTypes;
    private final double meshDensity;

    public StepFiniteElementMesh(int id, String name, String meshType, List<StepEntity> nodes, List<StepEntity> elements, List<String> elementTypes, double meshDensity) {
        super(id, name);
        this.meshType = meshType;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.elementTypes = elementTypes == null ? null : java.util.List.copyOf(elementTypes);
        this.meshDensity = meshDensity;
    }

    public String getMeshType() {
        return meshType;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    public List<String> getElementTypes() {
        return elementTypes;
    }

    public double getMeshDensity() {
        return meshDensity;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String meshType() { return getMeshType(); }
    public List<StepEntity> nodes() { return getNodes(); }
    public List<StepEntity> elements() { return getElements(); }
    public List<String> elementTypes() { return getElementTypes(); }
    public double meshDensity() { return getMeshDensity(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("meshType", meshType);
        state.put("nodes", nodes);
        state.put("elements", elements);
        state.put("elementTypes", elementTypes);
        state.put("meshDensity", meshDensity);
        return state;
    }
}
