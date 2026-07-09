package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HISTORY_RECORD.
 * A history record entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @param historyType history variance type
 * @param historyAction history variance action description
 * @param historyTarget history variance target reference
 * @param historyActor history variance actor reference
 * @param historyTimestamp history variance timestamp
 * @param historyStatus history variance status
 */
/**
 * Resolved HISTORY_RECORD.
 * A history record entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @param historyType history variance type
 * @param historyAction history variance action description
 * @param historyTarget history variance target reference
 * @param historyActor history variance actor reference
 * @param historyTimestamp history variance timestamp
 * @param historyStatus history variance status
 */
public final class StepHistoryRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String historyType;
    private final String historyAction;
    private final StepEntity historyTarget;
    private final StepEntity historyActor;
    private final StepEntity historyTimestamp;
    private final String historyStatus;

    public StepHistoryRecord(int id, String name, String historyType, String historyAction, StepEntity historyTarget, StepEntity historyActor, StepEntity historyTimestamp, String historyStatus) {
        this.id = id;
        this.name = name;
        this.historyType = historyType;
        this.historyAction = historyAction;
        this.historyTarget = historyTarget;
        this.historyActor = historyActor;
        this.historyTimestamp = historyTimestamp;
        this.historyStatus = historyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHistoryType() {
        return historyType;
    }

    public String getHistoryAction() {
        return historyAction;
    }

    public StepEntity getHistoryTarget() {
        return historyTarget;
    }

    public StepEntity getHistoryActor() {
        return historyActor;
    }

    public StepEntity getHistoryTimestamp() {
        return historyTimestamp;
    }

    public String getHistoryStatus() {
        return historyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHistoryRecord that = (StepHistoryRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(historyType, that.historyType) && Objects.equals(historyAction, that.historyAction) && Objects.equals(historyTarget, that.historyTarget) && Objects.equals(historyActor, that.historyActor) && Objects.equals(historyTimestamp, that.historyTimestamp) && Objects.equals(historyStatus, that.historyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, historyType, historyAction, historyTarget, historyActor, historyTimestamp, historyStatus);
    }

    @Override
    public String toString() {
        return "StepHistoryRecord{" + "id=" + id + "name=" + name + "historyType=" + historyType + "historyAction=" + historyAction + "historyTarget=" + historyTarget + "historyActor=" + historyActor + "historyTimestamp=" + historyTimestamp + "historyStatus=" + historyStatus + "}";
    }
}