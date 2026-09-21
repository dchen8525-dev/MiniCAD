package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CARTESIAN_TRANSFORMATION_OPERATOR_2D/3D.
 *
 * @param id step id
 * @param name step label
 * @param axis1 optional first axis
 * @param axis2 optional second axis
 * @param localOrigin local origin point
 * @param scale optional scale factor
 * @param axis3 optional third axis for 3D operators
 * @param entityName concrete STEP entity name
 */
public final class StepCartesianTransformationOperator extends AbstractStepEntity {
    private final StepDirection axis1;
    private final StepDirection axis2;
    private final StepCartesianPoint localOrigin;
    private final Double scale;
    private final StepDirection axis3;
    private final String entityName;

    public StepCartesianTransformationOperator(int id, String name, StepDirection axis1, StepDirection axis2, StepCartesianPoint localOrigin, Double scale, StepDirection axis3, String entityName) {
        super(id, name);
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.localOrigin = localOrigin;
        this.scale = scale;
        this.axis3 = axis3;
        this.entityName = entityName;
    }

    public StepDirection getAxis1() {
        return axis1;
    }

    public StepDirection getAxis2() {
        return axis2;
    }

    public StepCartesianPoint getLocalOrigin() {
        return localOrigin;
    }

    public Double getScale() {
        return scale;
    }

    public StepDirection getAxis3() {
        return axis3;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepDirection axis1() { return getAxis1(); }
    public StepDirection axis2() { return getAxis2(); }
    public StepCartesianPoint localOrigin() { return getLocalOrigin(); }
    public Double scale() { return getScale(); }
    public StepDirection axis3() { return getAxis3(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("axis1", axis1);
        state.put("axis2", axis2);
        state.put("localOrigin", localOrigin);
        state.put("scale", scale);
        state.put("axis3", axis3);
        state.put("entityName", entityName);
        return state;
    }
}
