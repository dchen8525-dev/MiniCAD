package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepProcessRecord extends AbstractStepEntity {
    private final String processType;
    private final StepEntity processTarget;
    private final StepEntity processStartTime;
    private final StepEntity processEndTime;
    private final String processResult;
    private final List<String> processDetails;
    private final String processStatus;

    public StepProcessRecord(int id, String name, String processType, StepEntity processTarget, StepEntity processStartTime, StepEntity processEndTime, String processResult, List<String> processDetails, String processStatus) {
        super(id, name);
        this.processType = processType;
        this.processTarget = processTarget;
        this.processStartTime = processStartTime;
        this.processEndTime = processEndTime;
        this.processResult = processResult;
        this.processDetails = processDetails == null ? null : java.util.List.copyOf(processDetails);
        this.processStatus = processStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("processType", processType);
        state.put("processTarget", processTarget);
        state.put("processStartTime", processStartTime);
        state.put("processEndTime", processEndTime);
        state.put("processResult", processResult);
        state.put("processDetails", processDetails);
        state.put("processStatus", processStatus);
        return state;
    }
}
