package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CANCEL_INSTANCE.
 * A cancel instance entity.
 *
 * @param id STEP instance id
 * @param name cancel instance name
 * @param cancelDefinition cancel variance definition reference
 * @param cancelState cancel variance state
 * @param cancelTime cancel variance cancellation time
 * @param cancelReason cancel variance reason
 * @param cancelStatus cancel variance status
 */
/**
 * Resolved CANCEL_INSTANCE.
 * A cancel instance entity.
 *
 * @param id STEP instance id
 * @param name cancel instance name
 * @param cancelDefinition cancel variance definition reference
 * @param cancelState cancel variance state
 * @param cancelTime cancel variance cancellation time
 * @param cancelReason cancel variance reason
 * @param cancelStatus cancel variance status
 */
public final class StepCancelInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity cancelDefinition;
    private final String cancelState;
    private final StepEntity cancelTime;
    private final String cancelReason;
    private final String cancelStatus;

    public StepCancelInstance(int id, String name, StepEntity cancelDefinition, String cancelState, StepEntity cancelTime, String cancelReason, String cancelStatus) {
        this.id = id;
        this.name = name;
        this.cancelDefinition = cancelDefinition;
        this.cancelState = cancelState;
        this.cancelTime = cancelTime;
        this.cancelReason = cancelReason;
        this.cancelStatus = cancelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCancelDefinition() {
        return cancelDefinition;
    }

    public String getCancelState() {
        return cancelState;
    }

    public StepEntity getCancelTime() {
        return cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCancelInstance that = (StepCancelInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(cancelDefinition, that.cancelDefinition) && Objects.equals(cancelState, that.cancelState) && Objects.equals(cancelTime, that.cancelTime) && Objects.equals(cancelReason, that.cancelReason) && Objects.equals(cancelStatus, that.cancelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cancelDefinition, cancelState, cancelTime, cancelReason, cancelStatus);
    }

    @Override
    public String toString() {
        return "StepCancelInstance{" + "id=" + id + "name=" + name + "cancelDefinition=" + cancelDefinition + "cancelState=" + cancelState + "cancelTime=" + cancelTime + "cancelReason=" + cancelReason + "cancelStatus=" + cancelStatus + "}";
    }
}