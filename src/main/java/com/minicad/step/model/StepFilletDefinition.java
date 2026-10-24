package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FILLET_DEFINITION.
 * A fillet definition entity.
 *
 * @param id STEP instance id
 * @param name fillet name
 * @param edges edges being filleted
 * @param radius fillet radius
 */
public final class StepFilletDefinition extends AbstractStepEntity {
    private final List<StepEntity> edges;
    private final Double radius;

    public StepFilletDefinition(int id, String name, List<StepEntity> edges, Double radius) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.radius = radius;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "edges", edges, "radius", radius);
    }
}
