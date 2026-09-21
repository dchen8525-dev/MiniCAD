package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESOURCE_UTILIZATION.
 * A resource utilization entity.
 *
 * @param id STEP instance id
 * @param name utilization name
 * @varianceResource resource variance reference
 * @varianceUtilization utilization variance percentage
 * @variancePeriod utilization variance period
 * @variancePeak peak variance utilization
 * @varianceAverage average variance utilization
 * @varianceStatus utilization variance status
 */
public final class StepResourceUtilization extends AbstractStepEntity {
    private final StepEntity varianceResource;
    private final double varianceUtilization;
    private final String variancePeriod;
    private final double variancePeak;
    private final double varianceAverage;
    private final String varianceStatus;

    public StepResourceUtilization(int id, String name, StepEntity varianceResource, double varianceUtilization, String variancePeriod, double variancePeak, double varianceAverage, String varianceStatus) {
        super(id, name);
        this.varianceResource = varianceResource;
        this.varianceUtilization = varianceUtilization;
        this.variancePeriod = variancePeriod;
        this.variancePeak = variancePeak;
        this.varianceAverage = varianceAverage;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceResource() {
        return varianceResource;
    }

    public double getVarianceUtilization() {
        return varianceUtilization;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public double getVariancePeak() {
        return variancePeak;
    }

    public double getVarianceAverage() {
        return varianceAverage;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceResource", varianceResource);
        state.put("varianceUtilization", varianceUtilization);
        state.put("variancePeriod", variancePeriod);
        state.put("variancePeak", variancePeak);
        state.put("varianceAverage", varianceAverage);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
