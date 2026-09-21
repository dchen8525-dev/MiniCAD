package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("backupDefinition", backupDefinition);
        state.put("backupStartTime", backupStartTime);
        state.put("backupEndTime", backupEndTime);
        state.put("backupSize", backupSize);
        state.put("backupValid", backupValid);
        state.put("backupStatus", backupStatus);
        return state;
    }
}
