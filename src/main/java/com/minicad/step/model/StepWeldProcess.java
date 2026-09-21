package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WELD_PROCESS.
 * A weld process entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processParameters process variance parameters
 * @param processEquipment process variance equipment reference
 * @param processStatus process variance status
 */
public final class StepWeldProcess extends AbstractStepEntity {
    private final String processType;
    private final List<String> processParameters;
    private final StepEntity processEquipment;
    private final String processStatus;

    public StepWeldProcess(int id, String name, String processType, List<String> processParameters, StepEntity processEquipment, String processStatus) {
        super(id, name);
        this.processType = processType;
        this.processParameters = processParameters == null ? null : java.util.List.copyOf(processParameters);
        this.processEquipment = processEquipment;
        this.processStatus = processStatus;
    }

    public String getProcessType() {
        return processType;
    }

    public List<String> getProcessParameters() {
        return processParameters;
    }

    public StepEntity getProcessEquipment() {
        return processEquipment;
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
        state.put("processParameters", processParameters);
        state.put("processEquipment", processEquipment);
        state.put("processStatus", processStatus);
        return state;
    }
}
