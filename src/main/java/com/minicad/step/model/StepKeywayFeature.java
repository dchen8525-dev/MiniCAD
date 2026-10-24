package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepKeywayFeature extends AbstractStepEntity {
    private final String keywayType;
    private final double keywayWidth;
    private final double keywayDepth;
    private final double keywayLength;
    private final StepEntity keywayPosition;
    private final double shaftDiameter;

    public StepKeywayFeature(int id, String name, String keywayType, double keywayWidth, double keywayDepth, double keywayLength, StepEntity keywayPosition, double shaftDiameter) {
        super(id, name);
        this.keywayType = keywayType;
        this.keywayWidth = keywayWidth;
        this.keywayDepth = keywayDepth;
        this.keywayLength = keywayLength;
        this.keywayPosition = keywayPosition;
        this.shaftDiameter = shaftDiameter;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "keywayType", keywayType, "keywayWidth", keywayWidth,
                "keywayDepth", keywayDepth, "keywayLength", keywayLength, "keywayPosition", keywayPosition,
                "shaftDiameter", shaftDiameter);
    }
}
