package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved CHECKPOINT_DEFINITION.
 * A checkpoint definition entity.
 *
 * @param id STEP instance id
 * @param name checkpoint name
 * @param checkpointType checkpoint variance type
 * @param checkpointLocation checkpoint variance location reference
 * @param checkpointFrequency checkpoint variance frequency
 * @param checkpointRetention checkpoint variance retention count
 * @param checkpointStatus checkpoint variance status
 */
public final class StepCheckpointDefinition extends AbstractStepEntity {
    private final String checkpointType;
    private final StepEntity checkpointLocation;
    private final int checkpointFrequency;
    private final int checkpointRetention;
    private final String checkpointStatus;

    public StepCheckpointDefinition(int id, String name, String checkpointType, StepEntity checkpointLocation, int checkpointFrequency, int checkpointRetention, String checkpointStatus) {
        super(id, name);
        this.checkpointType = checkpointType;
        this.checkpointLocation = checkpointLocation;
        this.checkpointFrequency = checkpointFrequency;
        this.checkpointRetention = checkpointRetention;
        this.checkpointStatus = checkpointStatus;
    }

    public String getCheckpointType() {
        return checkpointType;
    }

    public StepEntity getCheckpointLocation() {
        return checkpointLocation;
    }

    public int getCheckpointFrequency() {
        return checkpointFrequency;
    }

    public int getCheckpointRetention() {
        return checkpointRetention;
    }

    public String getCheckpointStatus() {
        return checkpointStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "checkpointType", checkpointType,
                "checkpointLocation", checkpointLocation, "checkpointFrequency", checkpointFrequency,
                "checkpointRetention", checkpointRetention, "checkpointStatus", checkpointStatus);
    }
}
