package com.minicad.step.model;

import java.util.Map;

public final class StepCameraModelD2 extends AbstractStepEntity {
    private final StepEntity viewPlane;
    private final StepEntity viewReference;

    public StepCameraModelD2(int id, String name, StepEntity viewPlane, StepEntity viewReference) {
        super(id, name);
        this.viewPlane = viewPlane;
        this.viewReference = viewReference;
    }

    public StepEntity getViewPlane() {
        return viewPlane;
    }

    public StepEntity getViewReference() {
        return viewReference;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "viewPlane", viewPlane, "viewReference", viewReference);
    }
}
