package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCheckpointDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String checkpointType;
    private final StepEntity checkpointLocation;
    private final int checkpointFrequency;
    private final int checkpointRetention;
    private final String checkpointStatus;

    public StepCheckpointDefinition(int id, String name, String checkpointType, StepEntity checkpointLocation, int checkpointFrequency, int checkpointRetention, String checkpointStatus) {
        this.id = id;
        this.name = name;
        this.checkpointType = checkpointType;
        this.checkpointLocation = checkpointLocation;
        this.checkpointFrequency = checkpointFrequency;
        this.checkpointRetention = checkpointRetention;
        this.checkpointStatus = checkpointStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCheckpointDefinition that = (StepCheckpointDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(checkpointType, that.checkpointType) && Objects.equals(checkpointLocation, that.checkpointLocation) && checkpointFrequency == that.checkpointFrequency && checkpointRetention == that.checkpointRetention && Objects.equals(checkpointStatus, that.checkpointStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, checkpointType, checkpointLocation, checkpointFrequency, checkpointRetention, checkpointStatus);
    }

    @Override
    public String toString() {
        return "StepCheckpointDefinition{" + "id=" + id + "name=" + name + "checkpointType=" + checkpointType + "checkpointLocation=" + checkpointLocation + "checkpointFrequency=" + checkpointFrequency + "checkpointRetention=" + checkpointRetention + "checkpointStatus=" + checkpointStatus + "}";
    }
}