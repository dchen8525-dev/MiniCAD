package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal SOLID_REPLICA parse-only solid model.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parentSolid source solid
 * @param transformation placement transformation
 */
public final class StepSolidReplica extends AbstractStepEntity {
    private final StepEntity parentSolid;
    private final StepCartesianTransformationOperator transformation;

    public StepSolidReplica(int id, String name, StepEntity parentSolid, StepCartesianTransformationOperator transformation) {
        super(id, name);
        this.parentSolid = parentSolid;
        this.transformation = transformation;
    }

    public StepEntity getParentSolid() {
        return parentSolid;
    }

    public StepCartesianTransformationOperator getTransformation() {
        return transformation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity parentSolid() { return getParentSolid(); }
    public StepCartesianTransformationOperator transformation() { return getTransformation(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "parentSolid", parentSolid, "transformation", transformation);
    }
}
