package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ACTUATED_KINEMATIC_PAIR.
 * A kinematic pair with an actuator providing driven motion.
 */
/**
 * Resolved ACTUATED_KINEMATIC_PAIR.
 * A kinematic pair with an actuator providing driven motion.
 */
public final class StepActuatedKinematicPair implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity basePair;
    private final StepEntity actuator;
    private final Double actuationSpeed;

    public StepActuatedKinematicPair(int id, String name, String description, StepEntity basePair, StepEntity actuator, Double actuationSpeed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePair = basePair;
        this.actuator = actuator;
        this.actuationSpeed = actuationSpeed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActuatedKinematicPair that = (StepActuatedKinematicPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(basePair, that.basePair) && Objects.equals(actuator, that.actuator) && Objects.equals(actuationSpeed, that.actuationSpeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, basePair, actuator, actuationSpeed);
    }

    @Override
    public String toString() {
        return "StepActuatedKinematicPair{" + "id=" + id + "name=" + name + "description=" + description + "basePair=" + basePair + "actuator=" + actuator + "actuationSpeed=" + actuationSpeed + "}";
    }
}
