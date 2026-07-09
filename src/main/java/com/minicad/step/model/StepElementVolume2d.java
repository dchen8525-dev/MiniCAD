package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ELEMENT_VOLUME_2D.
 * A 2D finite element volume (shell/plate element).
 */
/**
 * Resolved ELEMENT_VOLUME_2D.
 * A 2D finite element volume (shell/plate element).
 */
public final class StepElementVolume2d implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> nodes;
    private final String elementType;

    public StepElementVolume2d(int id, String name, List<StepEntity> nodes, String elementType) {
        this.id = id;
        this.name = name;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementType = elementType;
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

    public String getElementType() {
        return elementType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepElementVolume2d that = (StepElementVolume2d) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodes, that.nodes) && Objects.equals(elementType, that.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes, elementType);
    }

    @Override
    public String toString() {
        return "StepElementVolume2d{" + "id=" + id + "name=" + name + "nodes=" + nodes + "elementType=" + elementType + "}";
    }
}
