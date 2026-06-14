package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MILESTONE_INSTANCE.
 * A milestone instance entity.
 *
 * @param id STEP instance id
 * @param name milestone instance name
 * @param milestoneDefinition milestone variance definition reference
 * @param milestoneState milestone variance state
 * @param milestoneActual milestone variance actual date
 * @param milestoneStatus milestone variance status
 */
/**
 * Resolved MILESTONE_INSTANCE.
 * A milestone instance entity.
 *
 * @param id STEP instance id
 * @param name milestone instance name
 * @param milestoneDefinition milestone variance definition reference
 * @param milestoneState milestone variance state
 * @param milestoneActual milestone variance actual date
 * @param milestoneStatus milestone variance status
 */
public final class StepMilestoneInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity milestoneDefinition;
    private final String milestoneState;
    private final StepEntity milestoneActual;
    private final String milestoneStatus;

    public StepMilestoneInstance(int id, String name, StepEntity milestoneDefinition, String milestoneState, StepEntity milestoneActual, String milestoneStatus) {
        this.id = id;
        this.name = name;
        this.milestoneDefinition = milestoneDefinition;
        this.milestoneState = milestoneState;
        this.milestoneActual = milestoneActual;
        this.milestoneStatus = milestoneStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMilestoneDefinition() {
        return milestoneDefinition;
    }

    public String getMilestoneState() {
        return milestoneState;
    }

    public StepEntity getMilestoneActual() {
        return milestoneActual;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMilestoneInstance that = (StepMilestoneInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(milestoneDefinition, that.milestoneDefinition) && Objects.equals(milestoneState, that.milestoneState) && Objects.equals(milestoneActual, that.milestoneActual) && Objects.equals(milestoneStatus, that.milestoneStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, milestoneDefinition, milestoneState, milestoneActual, milestoneStatus);
    }

    @Override
    public String toString() {
        return "StepMilestoneInstance{" + "id=" + id + "name=" + name + "milestoneDefinition=" + milestoneDefinition + "milestoneState=" + milestoneState + "milestoneActual=" + milestoneActual + "milestoneStatus=" + milestoneStatus + "}";
    }
}