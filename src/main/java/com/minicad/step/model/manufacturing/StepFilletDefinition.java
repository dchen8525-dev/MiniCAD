package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILLET_DEFINITION.
 * A fillet definition entity.
 *
 * @param id STEP instance id
 * @param name fillet name
 * @param edges edges being filleted
 * @param radius fillet radius
 */
/**
 * Resolved FILLET_DEFINITION.
 * A fillet definition entity.
 *
 * @param id STEP instance id
 * @param name fillet name
 * @param edges edges being filleted
 * @param radius fillet radius
 */
public final class StepFilletDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> edges;
    private final Double radius;

    public StepFilletDefinition(int id, String name, List<StepEntity> edges, Double radius) {
        this.id = id;
        this.name = name;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFilletDefinition that = (StepFilletDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges) && Objects.equals(radius, that.radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges, radius);
    }

    @Override
    public String toString() {
        return "StepFilletDefinition{" + "id=" + id + "name=" + name + "edges=" + edges + "radius=" + radius + "}";
    }
}