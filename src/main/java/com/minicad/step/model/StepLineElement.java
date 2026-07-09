package com.minicad.step.model.core.element;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved LINE_ELEMENT.
 * A 1D line finite element.
 */
/**
 * Resolved LINE_ELEMENT.
 * A 1D line finite element.
 */
public final class StepLineElement implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> nodes;
    private final StepEntity elementProperty;

    public StepLineElement(int id, String name, List<StepEntity> nodes, StepEntity elementProperty) {
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
        StepLineElement that = (StepLineElement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodes, that.nodes) && Objects.equals(elementProperty, that.elementProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes, elementProperty);
    }

    @Override
    public String toString() {
        return "StepLineElement{" + "id=" + id + "name=" + name + "nodes=" + nodes + "elementProperty=" + elementProperty + "}";
    }
}
