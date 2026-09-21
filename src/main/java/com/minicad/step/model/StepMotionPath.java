package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MOTION_PATH.
 * A motion path entity.
 *
 * @param id STEP instance id
 * @param name path name
 * @param pathGeometry path geometry curve
 * @param motionType motion type (linear, circular, spline)
 * @param motionSpeed motion speed profile
 * @param motionAcceleration motion acceleration profile
 * @param startPosition start position point
 * @param endPosition end position point
 */
public final class StepMotionPath extends AbstractStepEntity {
    private final StepEntity pathGeometry;
    private final String motionType;
    private final StepEntity motionSpeed;
    private final StepEntity motionAcceleration;
    private final StepEntity startPosition;
    private final StepEntity endPosition;

    public StepMotionPath(int id, String name, StepEntity pathGeometry, String motionType, StepEntity motionSpeed, StepEntity motionAcceleration, StepEntity startPosition, StepEntity endPosition) {
        super(id, name);
        this.pathGeometry = pathGeometry;
        this.motionType = motionType;
        this.motionSpeed = motionSpeed;
        this.motionAcceleration = motionAcceleration;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public StepEntity getPathGeometry() {
        return pathGeometry;
    }

    public String getMotionType() {
        return motionType;
    }

    public StepEntity getMotionSpeed() {
        return motionSpeed;
    }

    public StepEntity getMotionAcceleration() {
        return motionAcceleration;
    }

    public StepEntity getStartPosition() {
        return startPosition;
    }

    public StepEntity getEndPosition() {
        return endPosition;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pathGeometry", pathGeometry);
        state.put("motionType", motionType);
        state.put("motionSpeed", motionSpeed);
        state.put("motionAcceleration", motionAcceleration);
        state.put("startPosition", startPosition);
        state.put("endPosition", endPosition);
        return state;
    }
}
