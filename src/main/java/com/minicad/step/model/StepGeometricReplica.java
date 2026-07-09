package com.minicad.step.model;

import com.minicad.step.model.StepEntity;

import com.minicad.step.model.StepCartesianTransformationOperator;
import java.util.Objects;
/**
 * Minimal parse-only POINT_REPLICA, CURVE_REPLICA or SURFACE_REPLICA.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parent replicated geometric item
 * @param transformation transformation operator
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal parse-only POINT_REPLICA, CURVE_REPLICA or SURFACE_REPLICA.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parent replicated geometric item
 * @param transformation transformation operator
 * @param entityName concrete STEP entity name
 */
public final class StepGeometricReplica implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parent;
    private final StepCartesianTransformationOperator transformation;
    private final String entityName;

    public StepGeometricReplica(int id, String name, StepEntity parent, StepCartesianTransformationOperator transformation, String entityName) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.transformation = transformation;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricReplica that = (StepGeometricReplica) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parent, that.parent) && Objects.equals(transformation, that.transformation) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parent, transformation, entityName);
    }

    @Override
    public String toString() {
        return "StepGeometricReplica{" + "id=" + id + "name=" + name + "parent=" + parent + "transformation=" + transformation + "entityName=" + entityName + "}";
    }
}
