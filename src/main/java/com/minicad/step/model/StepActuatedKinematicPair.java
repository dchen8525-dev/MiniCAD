package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ACTUATED_KINEMATIC_PAIR.
 * A kinematic pair with an actuator providing driven motion.
 */
public final class StepActuatedKinematicPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity basePair;
    private final StepEntity actuator;
    private final Double actuationSpeed;

    public StepActuatedKinematicPair(int id, String name, String description, StepEntity basePair, StepEntity actuator, Double actuationSpeed) {
        super(id, name);
        this.description = description;
        this.basePair = basePair;
        this.actuator = actuator;
        this.actuationSpeed = actuationSpeed;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getBasePair() {
        return basePair;
    }

    public StepEntity getActuator() {
        return actuator;
    }

    public Double getActuationSpeed() {
        return actuationSpeed;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("basePair", basePair);
        state.put("actuator", actuator);
        state.put("actuationSpeed", actuationSpeed);
        return state;
    }
}
