package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepGeneralizedDatum extends AbstractStepEntity {
    private final String description;
    private final StepEntity datumTarget;

    public StepGeneralizedDatum(int id, String name, String description, StepEntity datumTarget) {
        super(id, name);
        this.description = description;
        this.datumTarget = datumTarget;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDatumTarget() {
        return datumTarget;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("datumTarget", datumTarget);
        return state;
    }
}
