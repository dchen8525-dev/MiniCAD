package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOSED_TEXT_LITERAL.
 */
public final class StepComposedTextLiteral extends AbstractStepEntity {
    private final List<StepEntity> components;

    public StepComposedTextLiteral(int id, String name, List<StepEntity> components) {
        super(id, name);
        this.components = components == null ? null : java.util.List.copyOf(components);
    }

    public List<StepEntity> getComponents() {
        return components;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "components", components);
    }
}
