package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BACKUP_DEFINITION.
 * A backup definition entity.
 *
 * @param id STEP instance id
 * @param name backup name
 * @param backupType backup variance type
 * @param backupSource backup variance source reference
 * @param backupTarget backup variance target reference
 * @param backupSchedule backup variance schedule
 * @param backupRetention backup variance retention period
 * @param backupStatus backup variance status
 */
public final class StepBackupDefinition extends AbstractStepEntity {
    private final String backupType;
    private final StepEntity backupSource;
    private final StepEntity backupTarget;
    private final String backupSchedule;
    private final int backupRetention;
    private final String backupStatus;

    public StepBackupDefinition(int id, String name, String backupType, StepEntity backupSource, StepEntity backupTarget, String backupSchedule, int backupRetention, String backupStatus) {
        super(id, name);
        this.backupType = backupType;
        this.backupSource = backupSource;
        this.backupTarget = backupTarget;
        this.backupSchedule = backupSchedule;
        this.backupRetention = backupRetention;
        this.backupStatus = backupStatus;
    }

    public String getBackupType() {
        return backupType;
    }

    public StepEntity getBackupSource() {
        return backupSource;
    }

    public StepEntity getBackupTarget() {
        return backupTarget;
    }

    public String getBackupSchedule() {
        return backupSchedule;
    }

    public int getBackupRetention() {
        return backupRetention;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("backupType", backupType);
        state.put("backupSource", backupSource);
        state.put("backupTarget", backupTarget);
        state.put("backupSchedule", backupSchedule);
        state.put("backupRetention", backupRetention);
        state.put("backupStatus", backupStatus);
        return state;
    }
}
