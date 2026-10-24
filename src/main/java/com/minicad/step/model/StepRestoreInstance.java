package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "restoreDefinition", restoreDefinition,
                "restoreStartTime", restoreStartTime, "restoreEndTime", restoreEndTime,
                "restoreResult", restoreResult, "restoreValid", restoreValid, "restoreStatus", restoreStatus);
    }
}
