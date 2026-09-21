package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved REPAIR_RECORD.
 * A repair record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment repaired variance equipment
 * @varianceProblem repair variance problem description
 * @varianceCause repair variance root cause
 * @varianceDate repair variance date
 * @varianceActions repair variance actions
 * @varianceStatus record variance status
 */
public final class StepRepairRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final String varianceProblem;
    private final String varianceCause;
    private final StepEntity varianceDate;
    private final List<String> varianceActions;
    private final String varianceStatus;

    public StepRepairRecord(int id, String name, StepEntity varianceEquipment, String varianceProblem, String varianceCause, StepEntity varianceDate, List<String> varianceActions, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceProblem = varianceProblem;
        this.varianceCause = varianceCause;
        this.varianceDate = varianceDate;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceProblem() {
        return varianceProblem;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<String> getVarianceActions() {
        return varianceActions;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEquipment", varianceEquipment);
        state.put("varianceProblem", varianceProblem);
        state.put("varianceCause", varianceCause);
        state.put("varianceDate", varianceDate);
        state.put("varianceActions", varianceActions);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
