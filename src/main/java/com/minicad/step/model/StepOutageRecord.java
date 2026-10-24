package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved OUTAGE_RECORD.
 * An outage record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceCause outage variance cause
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceDuration outage variance duration
 * @varianceImpact impact variance description
 * @varianceStatus record variance status
 */
public final class StepOutageRecord extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final String varianceCause;
    private final StepEntity varianceStartTime;
    private final StepEntity varianceEndTime;
    private final double varianceDuration;
    private final String varianceImpact;
    private final String varianceStatus;

    public StepOutageRecord(int id, String name, StepEntity varianceSystem, String varianceCause, StepEntity varianceStartTime, StepEntity varianceEndTime, double varianceDuration, String varianceImpact, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceCause = varianceCause;
        this.varianceStartTime = varianceStartTime;
        this.varianceEndTime = varianceEndTime;
        this.varianceDuration = varianceDuration;
        this.varianceImpact = varianceImpact;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public StepEntity getVarianceStartTime() {
        return varianceStartTime;
    }

    public StepEntity getVarianceEndTime() {
        return varianceEndTime;
    }

    public double getVarianceDuration() {
        return varianceDuration;
    }

    public String getVarianceImpact() {
        return varianceImpact;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceSystem", varianceSystem,
                "varianceCause", varianceCause, "varianceStartTime", varianceStartTime,
                "varianceEndTime", varianceEndTime, "varianceDuration", varianceDuration,
                "varianceImpact", varianceImpact, "varianceStatus", varianceStatus);
    }
}
