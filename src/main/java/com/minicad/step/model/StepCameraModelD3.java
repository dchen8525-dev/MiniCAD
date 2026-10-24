package com.minicad.step.model;

import java.util.Map;

public final class StepCameraModelD3 extends AbstractStepEntity {
    private final StepEntity viewPlane;
    private final StepEntity viewReference;
    private final double fieldOfView;

    public StepCameraModelD3(int id, String name, StepEntity viewPlane, StepEntity viewReference, double fieldOfView) {
        super(id, name);
        this.viewPlane = viewPlane;
        this.viewReference = viewReference;
        this.fieldOfView = fieldOfView;
    }

    public StepEntity getViewPlane() {
        return viewPlane;
    }

    public StepEntity getViewReference() {
        return viewReference;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "viewPlane", viewPlane, "viewReference", viewReference,
                "fieldOfView", fieldOfView);
    }
}
