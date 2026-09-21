package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MIGRATION_RECORD.
 * A migration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData migrated variance data
 * @varianceFrom migration variance source
 * @varianceTo migration variance destination
 * @varianceDate migration variance date
 * @varianceFormat migration variance format conversion
 * @varianceStatus record variance status
 */
public final class StepMigrationRecord extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final String varianceFormat;
    private final String varianceStatus;

    public StepMigrationRecord(int id, String name, StepEntity varianceData, String varianceFrom, String varianceTo, StepEntity varianceDate, String varianceFormat, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceFormat = varianceFormat;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public String getVarianceFrom() {
        return varianceFrom;
    }

    public String getVarianceTo() {
        return varianceTo;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceFormat() {
        return varianceFormat;
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
        state.put("varianceFrom", varianceFrom);
        state.put("varianceTo", varianceTo);
        state.put("varianceDate", varianceDate);
        state.put("varianceFormat", varianceFormat);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
