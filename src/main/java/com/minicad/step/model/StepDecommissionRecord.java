package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DECOMMISSION_RECORD.
 * A decommission record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment decommissioned variance equipment
 * @varianceReason decommission variance reason
 * @varianceDate decommission variance date
 * @varianceDisposition disposition variance action
 * @varianceDocumentation documentation variance reference
 * @varianceStatus record variance status
 */
public final class StepDecommissionRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final String varianceReason;
    private final StepEntity varianceDate;
    private final String varianceDisposition;
    private final StepEntity varianceDocumentation;
    private final String varianceStatus;

    public StepDecommissionRecord(int id, String name, StepEntity varianceEquipment, String varianceReason, StepEntity varianceDate, String varianceDisposition, StepEntity varianceDocumentation, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceReason = varianceReason;
        this.varianceDate = varianceDate;
        this.varianceDisposition = varianceDisposition;
        this.varianceDocumentation = varianceDocumentation;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceDisposition() {
        return varianceDisposition;
    }

    public StepEntity getVarianceDocumentation() {
        return varianceDocumentation;
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
        state.put("varianceReason", varianceReason);
        state.put("varianceDate", varianceDate);
        state.put("varianceDisposition", varianceDisposition);
        state.put("varianceDocumentation", varianceDocumentation);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
