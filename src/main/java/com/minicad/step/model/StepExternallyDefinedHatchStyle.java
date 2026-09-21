package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved EXTERNALLY_DEFINED_HATCH_STYLE.
 * A hatch style defined by an external source.
 */
public final class StepExternallyDefinedHatchStyle extends AbstractStepEntity {
    private final StepEntity externalSource;

    public StepExternallyDefinedHatchStyle(int id, String name, StepEntity externalSource) {
        super(id, name);
        this.externalSource = externalSource;
    }

    public StepEntity getExternalSource() {
        return externalSource;
    }

    public String entityName() {
        return "EXTERNALLY_DEFINED_HATCH_STYLE";
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("externalSource", externalSource);
        return state;
    }
}
