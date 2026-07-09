package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROCESS_RECORD.
 * A process record entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processTarget process variance target reference
 * @param processStartTime process variance start time
 * @param processEndTime process variance end time
 * @param processResult process variance result
 * @param processDetails process variance details
 * @param processStatus process variance status
 */
/**
 * Resolved PROCESS_RECORD.
 * A process record entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processTarget process variance target reference
 * @param processStartTime process variance start time
 * @param processEndTime process variance end time
 * @param processResult process variance result
 * @param processDetails process variance details
 * @param processStatus process variance status
 */
public final class StepProcessRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String processType;
    private final StepEntity processTarget;
    private final StepEntity processStartTime;
    private final StepEntity processEndTime;
    private final String processResult;
    private final List<String> processDetails;
    private final String processStatus;

    public StepProcessRecord(int id, String name, String processType, StepEntity processTarget, StepEntity processStartTime, StepEntity processEndTime, String processResult, List<String> processDetails, String processStatus) {
        this.id = id;
        this.name = name;
        this.processType = processType;
        this.processTarget = processTarget;
        this.processStartTime = processStartTime;
        this.processEndTime = processEndTime;
        this.processResult = processResult;
        this.processDetails = processDetails == null ? null : java.util.List.copyOf(processDetails);
        this.processStatus = processStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProcessType() {
        return processType;
    }

    public StepEntity getProcessTarget() {
        return processTarget;
    }

    public StepEntity getProcessStartTime() {
        return processStartTime;
    }

    public StepEntity getProcessEndTime() {
        return processEndTime;
    }

    public String getProcessResult() {
        return processResult;
    }

    public List<String> getProcessDetails() {
        return processDetails;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcessRecord that = (StepProcessRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(processType, that.processType) && Objects.equals(processTarget, that.processTarget) && Objects.equals(processStartTime, that.processStartTime) && Objects.equals(processEndTime, that.processEndTime) && Objects.equals(processResult, that.processResult) && Objects.equals(processDetails, that.processDetails) && Objects.equals(processStatus, that.processStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, processType, processTarget, processStartTime, processEndTime, processResult, processDetails, processStatus);
    }

    @Override
    public String toString() {
        return "StepProcessRecord{" + "id=" + id + "name=" + name + "processType=" + processType + "processTarget=" + processTarget + "processStartTime=" + processStartTime + "processEndTime=" + processEndTime + "processResult=" + processResult + "processDetails=" + processDetails + "processStatus=" + processStatus + "}";
    }
}