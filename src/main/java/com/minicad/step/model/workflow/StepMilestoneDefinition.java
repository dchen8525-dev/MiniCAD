package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MILESTONE_DEFINITION.
 * A milestone definition entity.
 *
 * @param id STEP instance id
 * @param name milestone name
 * @param milestoneType milestone variance type
 * @param milestoneDescription milestone variance description
 * @param milestoneTarget milestone variance target date
 * @param milestoneStatus milestone variance status
 */
/**
 * Resolved MILESTONE_DEFINITION.
 * A milestone definition entity.
 *
 * @param id STEP instance id
 * @param name milestone name
 * @param milestoneType milestone variance type
 * @param milestoneDescription milestone variance description
 * @param milestoneTarget milestone variance target date
 * @param milestoneStatus milestone variance status
 */
public final class StepMilestoneDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String milestoneType;
    private final String milestoneDescription;
    private final StepEntity milestoneTarget;
    private final String milestoneStatus;

    public StepMilestoneDefinition(int id, String name, String milestoneType, String milestoneDescription, StepEntity milestoneTarget, String milestoneStatus) {
        this.id = id;
        this.name = name;
        this.milestoneType = milestoneType;
        this.milestoneDescription = milestoneDescription;
        this.milestoneTarget = milestoneTarget;
        this.milestoneStatus = milestoneStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public StepEntity getMilestoneTarget() {
        return milestoneTarget;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMilestoneDefinition that = (StepMilestoneDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(milestoneType, that.milestoneType) && Objects.equals(milestoneDescription, that.milestoneDescription) && Objects.equals(milestoneTarget, that.milestoneTarget) && Objects.equals(milestoneStatus, that.milestoneStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, milestoneType, milestoneDescription, milestoneTarget, milestoneStatus);
    }

    @Override
    public String toString() {
        return "StepMilestoneDefinition{" + "id=" + id + "name=" + name + "milestoneType=" + milestoneType + "milestoneDescription=" + milestoneDescription + "milestoneTarget=" + milestoneTarget + "milestoneStatus=" + milestoneStatus + "}";
    }
}