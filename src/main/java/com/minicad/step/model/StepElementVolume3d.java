package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ELEMENT_VOLUME_3D.
 * A 3D finite element volume (solid/tetrahedral/hexahedral element).
 */
public final class StepElementVolume3d extends AbstractStepEntity {
    private final List<StepEntity> nodes;
    private final String elementType;

    public StepElementVolume3d(int id, String name, List<StepEntity> nodes, String elementType) {
        super(id, name);
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementType = elementType;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    public String getElementType() {
        return elementType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nodes", nodes);
        state.put("elementType", elementType);
        return state;
    }
}
