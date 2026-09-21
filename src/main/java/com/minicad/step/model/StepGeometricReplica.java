package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal parse-only POINT_REPLICA, CURVE_REPLICA or SURFACE_REPLICA.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parent replicated geometric item
 * @param transformation transformation operator
 * @param entityName concrete STEP entity name
 */
public final class StepGeometricReplica extends AbstractStepEntity {
    private final StepEntity parent;
    private final StepCartesianTransformationOperator transformation;
    private final String entityName;

    public StepGeometricReplica(int id, String name, StepEntity parent, StepCartesianTransformationOperator transformation, String entityName) {
        super(id, name);
        this.parent = parent;
        this.transformation = transformation;
        this.entityName = entityName;
    }

    public StepEntity getParent() {
        return parent;
    }

    public StepCartesianTransformationOperator getTransformation() {
        return transformation;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity parent() { return getParent(); }
    public StepCartesianTransformationOperator transformation() { return getTransformation(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parent", parent);
        state.put("transformation", transformation);
        state.put("entityName", entityName);
        return state;
    }
}
