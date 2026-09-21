package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("components", components);
        return state;
    }
}
