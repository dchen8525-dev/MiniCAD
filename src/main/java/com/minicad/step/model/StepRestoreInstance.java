package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESTORE_INSTANCE.
 * A restore instance entity.
 *
 * @param id STEP instance id
 * @param name restore instance name
 * @param restoreDefinition restore variance definition reference
 * @param restoreStartTime restore variance start time
 * @param restoreEndTime restore variance end time
 * @param restoreResult restore variance result
 * @param restoreValid restore variance valid flag
 * @param restoreStatus restore variance status
 */
public final class StepRestoreInstance extends AbstractStepEntity {
    private final StepEntity restoreDefinition;
    private final StepEntity restoreStartTime;
    private final StepEntity restoreEndTime;
    private final String restoreResult;
    private final boolean restoreValid;
    private final String restoreStatus;

    public StepRestoreInstance(int id, String name, StepEntity restoreDefinition, StepEntity restoreStartTime, StepEntity restoreEndTime, String restoreResult, boolean restoreValid, String restoreStatus) {
        super(id, name);
        this.restoreDefinition = restoreDefinition;
        this.restoreStartTime = restoreStartTime;
        this.restoreEndTime = restoreEndTime;
        this.restoreResult = restoreResult;
        this.restoreValid = restoreValid;
        this.restoreStatus = restoreStatus;
    }

    public StepEntity getRestoreDefinition() {
        return restoreDefinition;
    }

    public StepEntity getRestoreStartTime() {
        return restoreStartTime;
    }

    public StepEntity getRestoreEndTime() {
        return restoreEndTime;
    }

    public String getRestoreResult() {
        return restoreResult;
    }

    public boolean isRestoreValid() {
        return restoreValid;
    }

    public String getRestoreStatus() {
        return restoreStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("restoreDefinition", restoreDefinition);
        state.put("restoreStartTime", restoreStartTime);
        state.put("restoreEndTime", restoreEndTime);
        state.put("restoreResult", restoreResult);
        state.put("restoreValid", restoreValid);
        state.put("restoreStatus", restoreStatus);
        return state;
    }
}
