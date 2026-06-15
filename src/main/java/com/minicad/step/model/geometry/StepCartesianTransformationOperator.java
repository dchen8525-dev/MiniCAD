package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepCartesianTransformationOperator implements StepEntity {
    private final int id;
    private final String name;
    private final StepDirection axis1;
    private final StepDirection axis2;
    private final StepCartesianPoint localOrigin;
    private final Double scale;
    private final StepDirection axis3;
    private final String entityName;

    public StepCartesianTransformationOperator(int id, String name, StepDirection axis1, StepDirection axis2, StepCartesianPoint localOrigin, Double scale, StepDirection axis3, String entityName) {
        this.id = id;
        this.name = name;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.localOrigin = localOrigin;
        this.scale = scale;
        this.axis3 = axis3;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCartesianTransformationOperator that = (StepCartesianTransformationOperator) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(axis1, that.axis1) && Objects.equals(axis2, that.axis2) && Objects.equals(localOrigin, that.localOrigin) && Objects.equals(scale, that.scale) && Objects.equals(axis3, that.axis3) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, axis1, axis2, localOrigin, scale, axis3, entityName);
    }

    @Override
    public String toString() {
        return "StepCartesianTransformationOperator{" + "id=" + id + "name=" + name + "axis1=" + axis1 + "axis2=" + axis2 + "localOrigin=" + localOrigin + "scale=" + scale + "axis3=" + axis3 + "entityName=" + entityName + "}";
    }
}
