package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "varianceData", varianceData,
                "varianceSource", varianceSource, "varianceDate", varianceDate,
                "varianceVerified", varianceVerified, "varianceStatus", varianceStatus);
    }
}
