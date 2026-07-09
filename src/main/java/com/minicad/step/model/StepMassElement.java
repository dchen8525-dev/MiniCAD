package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved MASS_ELEMENT.
 * A mass finite element.
 */
/**
 * Resolved MASS_ELEMENT.
 * A mass finite element.
 */
public final class StepMassElement implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> nodes;
    private final double mass;

    public StepMassElement(int id, String name, List<StepEntity> nodes, double mass) {
        this.id = id;
        this.name = name;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.mass = mass;
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

    public double getMass() {
        return mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMassElement that = (StepMassElement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodes, that.nodes) && mass == that.mass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes, mass);
    }

    @Override
    public String toString() {
        return "StepMassElement{" + "id=" + id + "name=" + name + "nodes=" + nodes + "mass=" + mass + "}";
    }
}
