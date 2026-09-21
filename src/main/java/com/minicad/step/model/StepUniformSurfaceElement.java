package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved UNIFORM_SURFACE_ELEMENT.
 * A uniform surface finite element.
 */
public final class StepUniformSurfaceElement extends AbstractStepEntity {
    private final List<StepEntity> nodes;
    private final StepEntity elementProperty;

    public StepUniformSurfaceElement(int id, String name, List<StepEntity> nodes, StepEntity elementProperty) {
        super(id, name);
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementProperty = elementProperty;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    public StepEntity getElementProperty() {
        return elementProperty;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nodes", nodes);
        state.put("elementProperty", elementProperty);
        return state;
    }
}
