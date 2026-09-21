package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ANOMALY_RECORD.
 * An anomaly record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType anomaly variance type
 * @varianceDetection detection variance method
 * @varianceDate anomaly variance date
 * @varianceInvestigation investigation variance result
 * @varianceAction action variance taken
 * @varianceStatus record variance status
 */
public final class StepAnomalyRecord extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final String varianceType;
    private final String varianceDetection;
    private final StepEntity varianceDate;
    private final String varianceInvestigation;
    private final String varianceAction;
    private final String varianceStatus;

    public StepAnomalyRecord(int id, String name, StepEntity varianceSystem, String varianceType, String varianceDetection, StepEntity varianceDate, String varianceInvestigation, String varianceAction, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceType = varianceType;
        this.varianceDetection = varianceDetection;
        this.varianceDate = varianceDate;
        this.varianceInvestigation = varianceInvestigation;
        this.varianceAction = varianceAction;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceDetection() {
        return varianceDetection;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceInvestigation() {
        return varianceInvestigation;
    }

    public String getVarianceAction() {
        return varianceAction;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceType", varianceType);
        state.put("varianceDetection", varianceDetection);
        state.put("varianceDate", varianceDate);
        state.put("varianceInvestigation", varianceInvestigation);
        state.put("varianceAction", varianceAction);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
