package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CANCEL_DEFINITION.
 * A cancel definition entity.
 *
 * @param id STEP instance id
 * @param name cancel name
 * @param cancelType cancel variance type
 * @param cancelCondition cancel variance condition
 * @param cancelGracePeriod cancel variance grace period
 * @param cancelCleanup cancel variance cleanup action
 * @param cancelStatus cancel variance status
 */
/**
 * Resolved CANCEL_DEFINITION.
 * A cancel definition entity.
 *
 * @param id STEP instance id
 * @param name cancel name
 * @param cancelType cancel variance type
 * @param cancelCondition cancel variance condition
 * @param cancelGracePeriod cancel variance grace period
 * @param cancelCleanup cancel variance cleanup action
 * @param cancelStatus cancel variance status
 */
public final class StepCancelDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String cancelType;
    private final String cancelCondition;
    private final int cancelGracePeriod;
    private final StepEntity cancelCleanup;
    private final String cancelStatus;

    public StepCancelDefinition(int id, String name, String cancelType, String cancelCondition, int cancelGracePeriod, StepEntity cancelCleanup, String cancelStatus) {
        this.id = id;
        this.name = name;
        this.cancelType = cancelType;
        this.cancelCondition = cancelCondition;
        this.cancelGracePeriod = cancelGracePeriod;
        this.cancelCleanup = cancelCleanup;
        this.cancelStatus = cancelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCancelType() {
        return cancelType;
    }

    public String getCancelCondition() {
        return cancelCondition;
    }

    public int getCancelGracePeriod() {
        return cancelGracePeriod;
    }

    public StepEntity getCancelCleanup() {
        return cancelCleanup;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCancelDefinition that = (StepCancelDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(cancelType, that.cancelType) && Objects.equals(cancelCondition, that.cancelCondition) && cancelGracePeriod == that.cancelGracePeriod && Objects.equals(cancelCleanup, that.cancelCleanup) && Objects.equals(cancelStatus, that.cancelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cancelType, cancelCondition, cancelGracePeriod, cancelCleanup, cancelStatus);
    }

    @Override
    public String toString() {
        return "StepCancelDefinition{" + "id=" + id + "name=" + name + "cancelType=" + cancelType + "cancelCondition=" + cancelCondition + "cancelGracePeriod=" + cancelGracePeriod + "cancelCleanup=" + cancelCleanup + "cancelStatus=" + cancelStatus + "}";
    }
}