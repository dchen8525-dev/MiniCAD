package com.minicad.step.model.management.config;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHANGE_RECORD.
 * A change record entity.
 *
 * @param id STEP instance id
 * @param name change name
 * @param changeType change variance type
 * @param changeDescription change variance description
 * @param changeTarget change variance target reference
 * @param changeReason change variance reason
 * @param changeTimestamp change variance timestamp
 * @param changeStatus change variance status
 */
/**
 * Resolved CHANGE_RECORD.
 * A change record entity.
 *
 * @param id STEP instance id
 * @param name change name
 * @param changeType change variance type
 * @param changeDescription change variance description
 * @param changeTarget change variance target reference
 * @param changeReason change variance reason
 * @param changeTimestamp change variance timestamp
 * @param changeStatus change variance status
 */
public final class StepChangeRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String changeType;
    private final String changeDescription;
    private final StepEntity changeTarget;
    private final String changeReason;
    private final StepEntity changeTimestamp;
    private final String changeStatus;

    public StepChangeRecord(int id, String name, String changeType, String changeDescription, StepEntity changeTarget, String changeReason, StepEntity changeTimestamp, String changeStatus) {
        this.id = id;
        this.name = name;
        this.changeType = changeType;
        this.changeDescription = changeDescription;
        this.changeTarget = changeTarget;
        this.changeReason = changeReason;
        this.changeTimestamp = changeTimestamp;
        this.changeStatus = changeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public StepEntity getChangeTarget() {
        return changeTarget;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public StepEntity getChangeTimestamp() {
        return changeTimestamp;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChangeRecord that = (StepChangeRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(changeType, that.changeType) && Objects.equals(changeDescription, that.changeDescription) && Objects.equals(changeTarget, that.changeTarget) && Objects.equals(changeReason, that.changeReason) && Objects.equals(changeTimestamp, that.changeTimestamp) && Objects.equals(changeStatus, that.changeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, changeType, changeDescription, changeTarget, changeReason, changeTimestamp, changeStatus);
    }

    @Override
    public String toString() {
        return "StepChangeRecord{" + "id=" + id + "name=" + name + "changeType=" + changeType + "changeDescription=" + changeDescription + "changeTarget=" + changeTarget + "changeReason=" + changeReason + "changeTimestamp=" + changeTimestamp + "changeStatus=" + changeStatus + "}";
    }
}