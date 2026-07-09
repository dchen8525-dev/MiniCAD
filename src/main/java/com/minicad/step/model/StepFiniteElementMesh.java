package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFiniteElementMesh implements StepEntity {
    private final int id;
    private final String name;
    private final String meshType;
    private final List<StepEntity> nodes;
    private final List<StepEntity> elements;
    private final List<String> elementTypes;
    private final double meshDensity;

    public StepFiniteElementMesh(int id, String name, String meshType, List<StepEntity> nodes, List<StepEntity> elements, List<String> elementTypes, double meshDensity) {
        this.id = id;
        this.name = name;
        this.meshType = meshType;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.elementTypes = elementTypes == null ? null : java.util.List.copyOf(elementTypes);
        this.meshDensity = meshDensity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFiniteElementMesh that = (StepFiniteElementMesh) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(meshType, that.meshType) && Objects.equals(nodes, that.nodes) && Objects.equals(elements, that.elements) && Objects.equals(elementTypes, that.elementTypes) && meshDensity == that.meshDensity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, meshType, nodes, elements, elementTypes, meshDensity);
    }

    @Override
    public String toString() {
        return "StepFiniteElementMesh{" + "id=" + id + "name=" + name + "meshType=" + meshType + "nodes=" + nodes + "elements=" + elements + "elementTypes=" + elementTypes + "meshDensity=" + meshDensity + "}";
    }
}