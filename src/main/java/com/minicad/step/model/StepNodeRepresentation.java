package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved NODE_REPRESENTATION.
 * Graphical representation of a finite element node.
 */
public final class StepNodeRepresentation extends AbstractStepEntity {
    private final List<StepEntity> representedNodes;

    public StepNodeRepresentation(int id, String name, List<StepEntity> representedNodes) {
        super(id, name);
        this.representedNodes = representedNodes == null ? null : java.util.List.copyOf(representedNodes);
    }

    public List<StepEntity> getRepresentedNodes() {
        return representedNodes;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("representedNodes", representedNodes);
        return state;
    }
}
