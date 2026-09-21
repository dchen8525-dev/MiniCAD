package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DESIGN_MAKE_FROM.
 * Design-to-manufacturing mapping.
 */
public final class StepDesignMakeFrom extends AbstractStepEntity {
    private final String description;
    private final StepEntity design;
    private final StepEntity manufacturing;

    public StepDesignMakeFrom(int id, String name, String description, StepEntity design, StepEntity manufacturing) {
        super(id, name);
        this.description = description;
        this.design = design;
        this.manufacturing = manufacturing;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDesign() {
        return design;
    }

    public StepEntity getManufacturing() {
        return manufacturing;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("design", design);
        state.put("manufacturing", manufacturing);
        return state;
    }
}
