package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DATUM_TARGET.
 * A datum target used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetId target identifier
 * @param targetShape target shape reference
 */
public final class StepDatumTarget extends AbstractStepEntity {
    private final String targetId;
    private final StepEntity targetShape;

    public StepDatumTarget(int id, String name, String targetId, StepEntity targetShape) {
        super(id, name);
        this.targetId = targetId;
        this.targetShape = targetShape;
    }

    public String getTargetId() {
        return targetId;
    }

    public StepEntity getTargetShape() {
        return targetShape;
    }

    // Record-style accessor
    public StepEntity targetShape() {
        return targetShape;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "targetId", targetId, "targetShape", targetShape);
    }
}
