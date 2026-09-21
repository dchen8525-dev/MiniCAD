package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BACKUP_RECORD.
 * A backup record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData backed variance up data
 * @varianceLocation backup variance location
 * @varianceDate backup variance date
 * @varianceSize backup variance size
 * @varianceType backup variance type (full, incremental)
 * @varianceStatus record variance status
 */
public final class StepBackupRecord extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final double varianceSize;
    private final String varianceType;
    private final String varianceStatus;

    public StepBackupRecord(int id, String name, StepEntity varianceData, String varianceLocation, StepEntity varianceDate, double varianceSize, String varianceType, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceSize = varianceSize;
        this.varianceType = varianceType;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public double getVarianceSize() {
        return varianceSize;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceData", varianceData);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceDate", varianceDate);
        state.put("varianceSize", varianceSize);
        state.put("varianceType", varianceType);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
