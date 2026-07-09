package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved KEYWAY_FEATURE.
 * A keyway feature entity.
 *
 * @param id STEP instance id
 * @param name keyway name
 * @param keywayType keyway type classification
 * @param keywayWidth keyway width
 * @param keywayDepth keyway depth
 * @param keywayLength keyway length
 * @param keywayPosition keyway position placement
 * @param shaftDiameter reference shaft diameter
 */
/**
 * Resolved KEYWAY_FEATURE.
 * A keyway feature entity.
 *
 * @param id STEP instance id
 * @param name keyway name
 * @param keywayType keyway type classification
 * @param keywayWidth keyway width
 * @param keywayDepth keyway depth
 * @param keywayLength keyway length
 * @param keywayPosition keyway position placement
 * @param shaftDiameter reference shaft diameter
 */
public final class StepKeywayFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String keywayType;
    private final double keywayWidth;
    private final double keywayDepth;
    private final double keywayLength;
    private final StepEntity keywayPosition;
    private final double shaftDiameter;

    public StepKeywayFeature(int id, String name, String keywayType, double keywayWidth, double keywayDepth, double keywayLength, StepEntity keywayPosition, double shaftDiameter) {
        this.id = id;
        this.name = name;
        this.keywayType = keywayType;
        this.keywayWidth = keywayWidth;
        this.keywayDepth = keywayDepth;
        this.keywayLength = keywayLength;
        this.keywayPosition = keywayPosition;
        this.shaftDiameter = shaftDiameter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKeywayType() {
        return keywayType;
    }

    public double getKeywayWidth() {
        return keywayWidth;
    }

    public double getKeywayDepth() {
        return keywayDepth;
    }

    public double getKeywayLength() {
        return keywayLength;
    }

    public StepEntity getKeywayPosition() {
        return keywayPosition;
    }

    public double getShaftDiameter() {
        return shaftDiameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKeywayFeature that = (StepKeywayFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(keywayType, that.keywayType) && keywayWidth == that.keywayWidth && keywayDepth == that.keywayDepth && keywayLength == that.keywayLength && Objects.equals(keywayPosition, that.keywayPosition) && shaftDiameter == that.shaftDiameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, keywayType, keywayWidth, keywayDepth, keywayLength, keywayPosition, shaftDiameter);
    }

    @Override
    public String toString() {
        return "StepKeywayFeature{" + "id=" + id + "name=" + name + "keywayType=" + keywayType + "keywayWidth=" + keywayWidth + "keywayDepth=" + keywayDepth + "keywayLength=" + keywayLength + "keywayPosition=" + keywayPosition + "shaftDiameter=" + shaftDiameter + "}";
    }
}