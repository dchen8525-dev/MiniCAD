package com.minicad.step.model;

import java.util.List;

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
public record StepFiniteElementMesh(
    int id,
    String name,
    String meshType,
    List<StepEntity> nodes,
    List<StepEntity> elements,
    List<String> elementTypes,
    double meshDensity) implements StepEntity {
}