package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRAINING_RECORD.
 * A training record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param trainee trainee person reference
 * @param trainingType training type (skill, safety, procedure)
 * @param trainingTopic training topic/subject
 * @varianceDate training variance date
 * @varianceDuration training variance duration
 * @varianceProvider training variance provider
 * @varianceStatus training variance status
 */
public final class StepTrainingRecord extends AbstractStepEntity {
    private final StepEntity trainee;
    private final String trainingType;
    private final String trainingTopic;
    private final StepEntity varianceDate;
    private final double varianceDuration;
    private final StepEntity varianceProvider;
    private final String varianceStatus;

    public StepTrainingRecord(int id, String name, StepEntity trainee, String trainingType, String trainingTopic, StepEntity varianceDate, double varianceDuration, StepEntity varianceProvider, String varianceStatus) {
        super(id, name);
        this.trainee = trainee;
        this.trainingType = trainingType;
        this.trainingTopic = trainingTopic;
        this.varianceDate = varianceDate;
        this.varianceDuration = varianceDuration;
        this.varianceProvider = varianceProvider;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getTrainee() {
        return trainee;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public String getTrainingTopic() {
        return trainingTopic;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public double getVarianceDuration() {
        return varianceDuration;
    }

    public StepEntity getVarianceProvider() {
        return varianceProvider;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("trainee", trainee);
        state.put("trainingType", trainingType);
        state.put("trainingTopic", trainingTopic);
        state.put("varianceDate", varianceDate);
        state.put("varianceDuration", varianceDuration);
        state.put("varianceProvider", varianceProvider);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
