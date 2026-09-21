package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRACEABILITY_RECORD.
 * A traceability record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem traced variance item
 * @varianceOrigin origin variance source
 * @variancePath trace variance path/chain
 * @varianceDestination destination variance reference
 * @varianceDate trace variance date
 * @varianceStatus record variance status
 */
public final class StepTraceabilityRecord extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final StepEntity varianceOrigin;
    private final List<StepEntity> variancePath;
    private final StepEntity varianceDestination;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepTraceabilityRecord(int id, String name, StepEntity varianceItem, StepEntity varianceOrigin, List<StepEntity> variancePath, StepEntity varianceDestination, StepEntity varianceDate, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceOrigin = varianceOrigin;
        this.variancePath = variancePath == null ? null : java.util.List.copyOf(variancePath);
        this.varianceDestination = varianceDestination;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public StepEntity getVarianceOrigin() {
        return varianceOrigin;
    }

    public List<StepEntity> getVariancePath() {
        return variancePath;
    }

    public StepEntity getVarianceDestination() {
        return varianceDestination;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceOrigin", varianceOrigin);
        state.put("variancePath", variancePath);
        state.put("varianceDestination", varianceDestination);
        state.put("varianceDate", varianceDate);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
