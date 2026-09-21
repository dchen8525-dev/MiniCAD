package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ROUND_HOLE_DEFINITION.
 * A round hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param diameter hole diameter
 * @param depth hole depth
 * @param bottomType bottom type (through, blind, flat, etc)
 */
public final class StepRoundHoleDefinition extends AbstractStepEntity {
    private final Double diameter;
    private final Double depth;
    private final String bottomType;

    public StepRoundHoleDefinition(int id, String name, Double diameter, Double depth, String bottomType) {
        super(id, name);
        this.diameter = diameter;
        this.depth = depth;
        this.bottomType = bottomType;
    }

    public Double getDiameter() {
        return diameter;
    }

    public Double getDepth() {
        return depth;
    }

    public String getBottomType() {
        return bottomType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("diameter", diameter);
        state.put("depth", depth);
        state.put("bottomType", bottomType);
        return state;
    }
}
