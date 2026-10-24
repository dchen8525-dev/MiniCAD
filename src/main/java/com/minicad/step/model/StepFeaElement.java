package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved ELEMENT.
 * A finite element analysis element.
 */
public final class StepFeaElement extends AbstractStepEntity {
    private final String elementType;
    private final List<StepEntity> nodes;
    private final StepEntity elementProperty;

    public StepFeaElement(int id, String name, String elementType, List<StepEntity> nodes, StepEntity elementProperty) {
        super(id, name);
        this.elementType = elementType;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
        this.elementProperty = elementProperty;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "elementType", elementType, "nodes", nodes,
                "elementProperty", elementProperty);
    }
}
