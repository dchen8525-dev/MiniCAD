package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved EXTRUDED_AREA_SOLID_TAPERED.
 * An extruded solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to extrude
 * @param direction extrusion direction
 * @param depth extrusion depth
 * @param taperAngle taper angle
 */
public final class StepExtrudedAreaSolidTapered extends AbstractStepEntity {
    private final StepEntity sweptArea;
    private final StepDirection direction;
    private final double depth;
    private final double taperAngle;

    public StepExtrudedAreaSolidTapered(int id, String name, StepEntity sweptArea, StepDirection direction, double depth, double taperAngle) {
        super(id, name);
        this.sweptArea = sweptArea;
        this.direction = direction;
        this.depth = depth;
        this.taperAngle = taperAngle;
    }

    public StepEntity getSweptArea() {
        return sweptArea;
    }

    public StepDirection getDirection() {
        return direction;
    }

    public double getDepth() {
        return depth;
    }

    public double getTaperAngle() {
        return taperAngle;
    }

    // Record-style accessors
    public StepEntity sweptArea() { return sweptArea; }
    public StepDirection direction() { return direction; }
    public double depth() { return depth; }
    public double taperAngle() { return taperAngle; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sweptArea", sweptArea);
        state.put("direction", direction);
        state.put("depth", depth);
        state.put("taperAngle", taperAngle);
        return state;
    }
}
