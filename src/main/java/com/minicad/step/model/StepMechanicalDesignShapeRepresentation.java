package com.minicad.step.model;

import java.util.Map;

public final class StepMechanicalDesignShapeRepresentation extends AbstractStepEntity {
    private final StepEntity context;

    public StepMechanicalDesignShapeRepresentation(int id, String name, StepEntity context) {
        super(id, name);
        this.context = context;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "context", context);
    }
}
