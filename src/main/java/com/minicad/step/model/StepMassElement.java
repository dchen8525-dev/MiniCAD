package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved MASS_ELEMENT.
 * A mass finite element.
 */
public final class StepMassElement extends AbstractStepEntity {
    private final List<StepEntity> nodes;
    private final double mass;

    public StepMassElement(int id, String name, List<StepEntity> nodes, double mass) {
        super(id, name);
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.mass = mass;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    public double getMass() {
        return mass;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "nodes", nodes, "mass", mass);
    }
}
