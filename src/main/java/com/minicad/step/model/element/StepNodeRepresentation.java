package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved NODE_REPRESENTATION.
 * Graphical representation of a finite element node.
 */
/**
 * Resolved NODE_REPRESENTATION.
 * Graphical representation of a finite element node.
 */
public final class StepNodeRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> representedNodes;

    public StepNodeRepresentation(int id, String name, List<StepEntity> representedNodes) {
        this.id = id;
        this.name = name;
        this.representedNodes = representedNodes == null ? null : java.util.List.copyOf(representedNodes);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getRepresentedNodes() {
        return representedNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNodeRepresentation that = (StepNodeRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(representedNodes, that.representedNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, representedNodes);
    }

    @Override
    public String toString() {
        return "StepNodeRepresentation{" + "id=" + id + "name=" + name + "representedNodes=" + representedNodes + "}";
    }
}
