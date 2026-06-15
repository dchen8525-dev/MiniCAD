package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import java.util.Objects;
/**
 * Minimal SOLID_REPLICA parse-only solid model.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parentSolid source solid
 * @param transformation placement transformation
 */
/**
 * Minimal SOLID_REPLICA parse-only solid model.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parentSolid source solid
 * @param transformation placement transformation
 */
public final class StepSolidReplica implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parentSolid;
    private final StepCartesianTransformationOperator transformation;

    public StepSolidReplica(int id, String name, StepEntity parentSolid, StepCartesianTransformationOperator transformation) {
        this.id = id;
        this.name = name;
        this.parentSolid = parentSolid;
        this.transformation = transformation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSolidReplica that = (StepSolidReplica) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parentSolid, that.parentSolid) && Objects.equals(transformation, that.transformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentSolid, transformation);
    }

    @Override
    public String toString() {
        return "StepSolidReplica{" + "id=" + id + "name=" + name + "parentSolid=" + parentSolid + "transformation=" + transformation + "}";
    }
}
