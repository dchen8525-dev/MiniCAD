package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved ELEMENT.
 * A finite element analysis element.
 */
/**
 * Resolved ELEMENT.
 * A finite element analysis element.
 */
public final class StepFeaElement implements StepEntity {
    private final int id;
    private final String name;
    private final String elementType;
    private final List<StepEntity> nodes;
    private final StepEntity elementProperty;

    public StepFeaElement(int id, String name, String elementType, List<StepEntity> nodes, StepEntity elementProperty) {
        this.id = id;
        this.name = name;
        this.elementType = elementType;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementProperty = elementProperty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getElementType() {
        return elementType;
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
        StepFeaElement that = (StepFeaElement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(elementType, that.elementType) && Objects.equals(nodes, that.nodes) && Objects.equals(elementProperty, that.elementProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, elementType, nodes, elementProperty);
    }

    @Override
    public String toString() {
        return "StepFeaElement{" + "id=" + id + "name=" + name + "elementType=" + elementType + "nodes=" + nodes + "elementProperty=" + elementProperty + "}";
    }
}
