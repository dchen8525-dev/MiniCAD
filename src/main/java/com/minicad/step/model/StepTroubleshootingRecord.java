package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTroubleshootingRecord extends AbstractStepEntity {
    private final String varianceProblem;
    private final List<String> varianceSymptoms;
    private final List<String> varianceSteps;
    private final String varianceSolution;
    private final double varianceTime;
    private final String varianceStatus;

    public StepTroubleshootingRecord(int id, String name, String varianceProblem, List<String> varianceSymptoms, List<String> varianceSteps, String varianceSolution, double varianceTime, String varianceStatus) {
        super(id, name);
        this.varianceProblem = varianceProblem;
        this.varianceSymptoms = varianceSymptoms == null ? null : java.util.List.copyOf(varianceSymptoms);
        this.varianceSteps = varianceSteps == null ? null : java.util.List.copyOf(varianceSteps);
        this.varianceSolution = varianceSolution;
        this.varianceTime = varianceTime;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceProblem", varianceProblem);
        state.put("varianceSymptoms", varianceSymptoms);
        state.put("varianceSteps", varianceSteps);
        state.put("varianceSolution", varianceSolution);
        state.put("varianceTime", varianceTime);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
