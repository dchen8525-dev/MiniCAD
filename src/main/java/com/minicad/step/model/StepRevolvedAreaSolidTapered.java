package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved REVOLVED_AREA_SOLID_TAPERED.
 * A revolved solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to revolve
 * @param axis axis of revolution
 * @param angle revolution angle
 * @param taperAngle taper angle
 */
public final class StepRevolvedAreaSolidTapered extends AbstractStepEntity {
    private final StepEntity sweptArea;
    private final StepAxis1Placement axis;
    private final double angle;
    private final double taperAngle;

    public StepRevolvedAreaSolidTapered(int id, String name, StepEntity sweptArea, StepAxis1Placement axis, double angle, double taperAngle) {
        super(id, name);
        this.sweptArea = sweptArea;
        this.axis = axis;
        this.angle = angle;
        this.taperAngle = taperAngle;
    }

    public StepEntity getSweptArea() {
        return sweptArea;
    }

    public StepAxis1Placement getAxis() {
        return axis;
    }

    public double getAngle() {
        return angle;
    }

    public double getTaperAngle() {
        return taperAngle;
    }

    // Record-style accessors
    public StepEntity sweptArea() { return sweptArea; }
    public StepAxis1Placement axis() { return axis; }
    public double angle() { return angle; }
    public double taperAngle() { return taperAngle; }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sweptArea", sweptArea, "axis", axis, "angle", angle,
                "taperAngle", taperAngle);
    }
}
