package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved POINT_ON_FACE.
 * A point located on a face.
 */
public final class StepPointOnFace extends AbstractStepEntity {
    private final StepEntity face;
    private final double uParameter;
    private final double vParameter;

    public StepPointOnFace(int id, String name, StepEntity face, double uParameter, double vParameter) {
        super(id, name);
        this.face = face;
        this.uParameter = uParameter;
        this.vParameter = vParameter;
    }

    public StepEntity getFace() {
        return face;
    }

    public double getUParameter() {
        return uParameter;
    }

    public double getVParameter() {
        return vParameter;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "face", face, "uParameter", uParameter,
                "vParameter", vParameter);
    }
}
