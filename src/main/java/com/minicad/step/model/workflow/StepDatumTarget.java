package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DATUM_TARGET.
 * A datum target used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetId target identifier
 * @param targetShape target shape reference
 */
/**
 * Resolved DATUM_TARGET.
 * A datum target used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetId target identifier
 * @param targetShape target shape reference
 */
public final class StepDatumTarget implements StepEntity {
    private final int id;
    private final String name;
    private final String targetId;
    private final StepEntity targetShape;

    public StepDatumTarget(int id, String name, String targetId, StepEntity targetShape) {
        this.id = id;
        this.name = name;
        this.targetId = targetId;
        this.targetShape = targetShape;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDatumTarget that = (StepDatumTarget) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(targetId, that.targetId) && Objects.equals(targetShape, that.targetShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, targetId, targetShape);
    }

    @Override
    public String toString() {
        return "StepDatumTarget{" + "id=" + id + "name=" + name + "targetId=" + targetId + "targetShape=" + targetShape + "}";
    }
}
