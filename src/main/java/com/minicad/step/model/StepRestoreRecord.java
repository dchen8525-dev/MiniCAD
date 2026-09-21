package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESTORE_RECORD.
 * A restore record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData restored variance data
 * @varianceSource restore variance source/backup
 * @varianceDate restore variance date
 * @varianceVerified verification variance status
 * @varianceStatus record variance status
 */
public final class StepRestoreRecord extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final StepEntity varianceSource;
    private final StepEntity varianceDate;
    private final boolean varianceVerified;
    private final String varianceStatus;

    public StepRestoreRecord(int id, String name, StepEntity varianceData, StepEntity varianceSource, StepEntity varianceDate, boolean varianceVerified, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceSource = varianceSource;
        this.varianceDate = varianceDate;
        this.varianceVerified = varianceVerified;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public StepEntity getVarianceSource() {
        return varianceSource;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public boolean isVarianceVerified() {
        return varianceVerified;
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
        state.put("varianceSource", varianceSource);
        state.put("varianceDate", varianceDate);
        state.put("varianceVerified", varianceVerified);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
