package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE.
 * A low-order kinematic pair with specified range limits.
 */
public final class StepLowOrderKinematicPairWithRange extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity direction;
    private final Double lowerRange;
    private final Double upperRange;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepLowOrderKinematicPairWithRange(int id, String name, String description, StepEntity position, StepEntity direction, Double lowerRange, Double upperRange, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.direction = direction;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getLowerRange() {
        return lowerRange;
    }

    public Double getUpperRange() {
        return upperRange;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("position", position);
        state.put("direction", direction);
        state.put("lowerRange", lowerRange);
        state.put("upperRange", upperRange);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
