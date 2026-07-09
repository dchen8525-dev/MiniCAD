package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TROUBLESHOOTING_RECORD.
 * A troubleshooting record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceProblem problem variance description
 * @varianceSymptoms symptoms variance observed
 * @varianceSteps troubleshooting variance steps taken
 * @varianceSolution solution variance found
 * @varianceTime time variance to resolve
 * @varianceStatus record variance status
 */
/**
 * Resolved TROUBLESHOOTING_RECORD.
 * A troubleshooting record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceProblem problem variance description
 * @varianceSymptoms symptoms variance observed
 * @varianceSteps troubleshooting variance steps taken
 * @varianceSolution solution variance found
 * @varianceTime time variance to resolve
 * @varianceStatus record variance status
 */
public final class StepTroubleshootingRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceProblem;
    private final List<String> varianceSymptoms;
    private final List<String> varianceSteps;
    private final String varianceSolution;
    private final double varianceTime;
    private final String varianceStatus;

    public StepTroubleshootingRecord(int id, String name, String varianceProblem, List<String> varianceSymptoms, List<String> varianceSteps, String varianceSolution, double varianceTime, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProblem = varianceProblem;
        this.varianceSymptoms = varianceSymptoms == null ? null : java.util.List.copyOf(varianceSymptoms);
        this.varianceSteps = varianceSteps == null ? null : java.util.List.copyOf(varianceSteps);
        this.varianceSolution = varianceSolution;
        this.varianceTime = varianceTime;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceProblem() {
        return varianceProblem;
    }

    public List<String> getVarianceSymptoms() {
        return varianceSymptoms;
    }

    public List<String> getVarianceSteps() {
        return varianceSteps;
    }

    public String getVarianceSolution() {
        return varianceSolution;
    }

    public double getVarianceTime() {
        return varianceTime;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTroubleshootingRecord that = (StepTroubleshootingRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProblem, that.varianceProblem) && Objects.equals(varianceSymptoms, that.varianceSymptoms) && Objects.equals(varianceSteps, that.varianceSteps) && Objects.equals(varianceSolution, that.varianceSolution) && varianceTime == that.varianceTime && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProblem, varianceSymptoms, varianceSteps, varianceSolution, varianceTime, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTroubleshootingRecord{" + "id=" + id + "name=" + name + "varianceProblem=" + varianceProblem + "varianceSymptoms=" + varianceSymptoms + "varianceSteps=" + varianceSteps + "varianceSolution=" + varianceSolution + "varianceTime=" + varianceTime + "varianceStatus=" + varianceStatus + "}";
    }
}