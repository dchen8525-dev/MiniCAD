package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("viewPlane", viewPlane);
        state.put("viewReference", viewReference);
        state.put("fieldOfView", fieldOfView);
        return state;
    }
}
