package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TOOLPATH_SPEED_PROFILE.
 * A toolpath speed profile representation entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param speedValues speed values along the toolpath
 * @param feedValues feed values along the toolpath
 * @param positionPoints position points for profile values
 */
public final class StepToolpathSpeedProfile extends AbstractStepEntity {
    private final List<Double> speedValues;
    private final List<Double> feedValues;
    private final List<StepEntity> positionPoints;

    public StepToolpathSpeedProfile(int id, String name, List<Double> speedValues, List<Double> feedValues, List<StepEntity> positionPoints) {
        super(id, name);
        this.speedValues = speedValues == null ? null : java.util.List.copyOf(speedValues);
        this.feedValues = feedValues == null ? null : java.util.List.copyOf(feedValues);
        this.positionPoints = positionPoints == null ? null : java.util.List.copyOf(positionPoints);
    }

    public List<Double> getSpeedValues() {
        return speedValues;
    }

    public List<Double> getFeedValues() {
        return feedValues;
    }

    public List<StepEntity> getPositionPoints() {
        return positionPoints;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("speedValues", speedValues);
        state.put("feedValues", feedValues);
        state.put("positionPoints", positionPoints);
        return state;
    }
}
