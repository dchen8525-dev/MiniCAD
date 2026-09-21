package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved NODE_SET.
 * A named set of finite element nodes.
 */
public final class StepNodeSet extends AbstractStepEntity {
    private final List<StepEntity> nodes;

    public StepNodeSet(int id, String name, List<StepEntity> nodes) {
        super(id, name);
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nodes", nodes);
        return state;
    }
}
