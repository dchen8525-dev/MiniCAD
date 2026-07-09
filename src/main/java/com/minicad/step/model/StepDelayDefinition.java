package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DELAY_DEFINITION.
 * A delay definition entity.
 *
 * @param id STEP instance id
 * @param name delay name
 * @param delayType delay variance type
 * @param delayDuration delay variance duration
 * @param delayCondition delay variance condition
 * @param delayAction delay variance action after delay
 * @param delayStatus delay variance status
 */
/**
 * Resolved DELAY_DEFINITION.
 * A delay definition entity.
 *
 * @param id STEP instance id
 * @param name delay name
 * @param delayType delay variance type
 * @param delayDuration delay variance duration
 * @param delayCondition delay variance condition
 * @param delayAction delay variance action after delay
 * @param delayStatus delay variance status
 */
public final class StepDelayDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String delayType;
    private final int delayDuration;
    private final String delayCondition;
    private final StepEntity delayAction;
    private final String delayStatus;

    public StepDelayDefinition(int id, String name, String delayType, int delayDuration, String delayCondition, StepEntity delayAction, String delayStatus) {
        this.id = id;
        this.name = name;
        this.delayType = delayType;
        this.delayDuration = delayDuration;
        this.delayCondition = delayCondition;
        this.delayAction = delayAction;
        this.delayStatus = delayStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDelayType() {
        return delayType;
    }

    public int getDelayDuration() {
        return delayDuration;
    }

    public String getDelayCondition() {
        return delayCondition;
    }

    public StepEntity getDelayAction() {
        return delayAction;
    }

    public String getDelayStatus() {
        return delayStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDelayDefinition that = (StepDelayDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(delayType, that.delayType) && delayDuration == that.delayDuration && Objects.equals(delayCondition, that.delayCondition) && Objects.equals(delayAction, that.delayAction) && Objects.equals(delayStatus, that.delayStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, delayType, delayDuration, delayCondition, delayAction, delayStatus);
    }

    @Override
    public String toString() {
        return "StepDelayDefinition{" + "id=" + id + "name=" + name + "delayType=" + delayType + "delayDuration=" + delayDuration + "delayCondition=" + delayCondition + "delayAction=" + delayAction + "delayStatus=" + delayStatus + "}";
    }
}