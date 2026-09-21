package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHECKPOINT_INSTANCE.
 * A checkpoint instance entity.
 *
 * @param id STEP instance id
 * @param name checkpoint instance name
 * @param checkpointDefinition checkpoint variance definition reference
 * @param checkpointTime checkpoint variance creation time
 * @param checkpointSize checkpoint variance size
 * @param checkpointValid checkpoint variance valid flag
 * @param checkpointStatus checkpoint variance status
 */
public final class StepCheckpointInstance extends AbstractStepEntity {
    private final StepEntity checkpointDefinition;
    private final StepEntity checkpointTime;
    private final long checkpointSize;
    private final boolean checkpointValid;
    private final String checkpointStatus;

    public StepCheckpointInstance(int id, String name, StepEntity checkpointDefinition, StepEntity checkpointTime, long checkpointSize, boolean checkpointValid, String checkpointStatus) {
        super(id, name);
        this.checkpointDefinition = checkpointDefinition;
        this.checkpointTime = checkpointTime;
        this.checkpointSize = checkpointSize;
        this.checkpointValid = checkpointValid;
        this.checkpointStatus = checkpointStatus;
    }

    public StepEntity getCheckpointDefinition() {
        return checkpointDefinition;
    }

    public StepEntity getCheckpointTime() {
        return checkpointTime;
    }

    public long getCheckpointSize() {
        return checkpointSize;
    }

    public boolean isCheckpointValid() {
        return checkpointValid;
    }

    public String getCheckpointStatus() {
        return checkpointStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("checkpointDefinition", checkpointDefinition);
        state.put("checkpointTime", checkpointTime);
        state.put("checkpointSize", checkpointSize);
        state.put("checkpointValid", checkpointValid);
        state.put("checkpointStatus", checkpointStatus);
        return state;
    }
}
