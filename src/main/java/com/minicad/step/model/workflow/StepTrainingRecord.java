package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTrainingRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity trainee;
    private final String trainingType;
    private final String trainingTopic;
    private final StepEntity varianceDate;
    private final double varianceDuration;
    private final StepEntity varianceProvider;
    private final String varianceStatus;

    public StepTrainingRecord(int id, String name, StepEntity trainee, String trainingType, String trainingTopic, StepEntity varianceDate, double varianceDuration, StepEntity varianceProvider, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.trainee = trainee;
        this.trainingType = trainingType;
        this.trainingTopic = trainingTopic;
        this.varianceDate = varianceDate;
        this.varianceDuration = varianceDuration;
        this.varianceProvider = varianceProvider;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrainingRecord that = (StepTrainingRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(trainee, that.trainee) && Objects.equals(trainingType, that.trainingType) && Objects.equals(trainingTopic, that.trainingTopic) && Objects.equals(varianceDate, that.varianceDate) && varianceDuration == that.varianceDuration && Objects.equals(varianceProvider, that.varianceProvider) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, trainee, trainingType, trainingTopic, varianceDate, varianceDuration, varianceProvider, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTrainingRecord{" + "id=" + id + "name=" + name + "trainee=" + trainee + "trainingType=" + trainingType + "trainingTopic=" + trainingTopic + "varianceDate=" + varianceDate + "varianceDuration=" + varianceDuration + "varianceProvider=" + varianceProvider + "varianceStatus=" + varianceStatus + "}";
    }
}