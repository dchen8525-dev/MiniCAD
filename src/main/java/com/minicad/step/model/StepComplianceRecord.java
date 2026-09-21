package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPLIANCE_RECORD.
 * A compliance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem compliance variance item
 * @varianceStandard compliance variance standard
 * @varianceRequirements compliance variance requirements
 * @varianceEvidence compliance variance evidence
 * @varianceDate compliance variance date
 * @varianceStatus record variance status
 */
public final class StepComplianceRecord extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceStandard;
    private final List<String> varianceRequirements;
    private final List<StepEntity> varianceEvidence;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepComplianceRecord(int id, String name, StepEntity varianceItem, String varianceStandard, List<String> varianceRequirements, List<StepEntity> varianceEvidence, StepEntity varianceDate, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceStandard = varianceStandard;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
        this.varianceEvidence = varianceEvidence == null ? null : java.util.List.copyOf(varianceEvidence);
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceStandard() {
        return varianceStandard;
    }

    public List<String> getVarianceRequirements() {
        return varianceRequirements;
    }

    public List<StepEntity> getVarianceEvidence() {
        return varianceEvidence;
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
        state.put("varianceStandard", varianceStandard);
        state.put("varianceRequirements", varianceRequirements);
        state.put("varianceEvidence", varianceEvidence);
        state.put("varianceDate", varianceDate);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
