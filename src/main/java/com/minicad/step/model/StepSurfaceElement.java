package com.minicad.step.model.core.element;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved SURFACE_ELEMENT.
 * A 2D surface finite element.
 */
/**
 * Resolved SURFACE_ELEMENT.
 * A 2D surface finite element.
 */
public final class StepSurfaceElement implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> nodes;
    private final StepEntity elementProperty;

    public StepSurfaceElement(int id, String name, List<StepEntity> nodes, StepEntity elementProperty) {
        this.id = id;
        this.name = name;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementProperty = elementProperty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    public StepEntity getElementProperty() {
        return elementProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceElement that = (StepSurfaceElement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodes, that.nodes) && Objects.equals(elementProperty, that.elementProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes, elementProperty);
    }

    @Override
    public String toString() {
        return "StepSurfaceElement{" + "id=" + id + "name=" + name + "nodes=" + nodes + "elementProperty=" + elementProperty + "}";
    }
}
