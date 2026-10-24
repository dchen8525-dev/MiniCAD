package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "basePair", basePair,
                "actuator", actuator, "actuationSpeed", actuationSpeed);
    }
}
