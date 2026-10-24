package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved BACKUP_INSTANCE.
 * A backup instance entity.
 *
 * @param id STEP instance id
 * @param name backup instance name
 * @param backupDefinition backup variance definition reference
 * @param backupStartTime backup variance start time
 * @param backupEndTime backup variance end time
 * @param backupSize backup variance size
 * @param backupValid backup variance valid flag
 * @param backupStatus backup variance status
 */
public final class StepBackupInstance extends AbstractStepEntity {
    private final StepEntity backupDefinition;
    private final StepEntity backupStartTime;
    private final StepEntity backupEndTime;
    private final long backupSize;
    private final boolean backupValid;
    private final String backupStatus;

    public StepBackupInstance(int id, String name, StepEntity backupDefinition, StepEntity backupStartTime, StepEntity backupEndTime, long backupSize, boolean backupValid, String backupStatus) {
        super(id, name);
        this.backupDefinition = backupDefinition;
        this.backupStartTime = backupStartTime;
        this.backupEndTime = backupEndTime;
        this.backupSize = backupSize;
        this.backupValid = backupValid;
        this.backupStatus = backupStatus;
    }

    public StepEntity getBackupDefinition() {
        return backupDefinition;
    }

    public StepEntity getBackupStartTime() {
        return backupStartTime;
    }

    public StepEntity getBackupEndTime() {
        return backupEndTime;
    }

    public long getBackupSize() {
        return backupSize;
    }

    public boolean isBackupValid() {
        return backupValid;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "backupDefinition", backupDefinition,
                "backupStartTime", backupStartTime, "backupEndTime", backupEndTime, "backupSize", backupSize,
                "backupValid", backupValid, "backupStatus", backupStatus);
    }
}
