package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved KPI_MEASUREMENT.
 * A KPI measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @varianceKpi KPI variance reference
 * @varianceValue measured variance value
 * @varianceDate measurement variance date
 * @variancePeriod measurement variance period
 * @varianceStatus measurement variance status
 * @varianceComment measurement variance comment
 */
public final class StepKpiMeasurement extends AbstractStepEntity {
    private final StepEntity varianceKpi;
    private final double varianceValue;
    private final StepEntity varianceDate;
    private final String variancePeriod;
    private final String varianceStatus;
    private final String varianceComment;

    public StepKpiMeasurement(int id, String name, StepEntity varianceKpi, double varianceValue, StepEntity varianceDate, String variancePeriod, String varianceStatus, String varianceComment) {
        super(id, name);
        this.varianceKpi = varianceKpi;
        this.varianceValue = varianceValue;
        this.varianceDate = varianceDate;
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
        this.varianceComment = varianceComment;
    }

    public StepEntity getVarianceKpi() {
        return varianceKpi;
    }

    public double getVarianceValue() {
        return varianceValue;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public String getVarianceComment() {
        return varianceComment;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceKpi", varianceKpi);
        state.put("varianceValue", varianceValue);
        state.put("varianceDate", varianceDate);
        state.put("variancePeriod", variancePeriod);
        state.put("varianceStatus", varianceStatus);
        state.put("varianceComment", varianceComment);
        return state;
    }
}
