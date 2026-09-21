package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved OPERATOR_QUALIFICATION.
 * An operator qualification entity.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param operatorId operator identifier
 * @param qualificationType qualification type (skill, certification, training)
 * @varianceLevel qualification variance level
 * @param qualificationDate qualification date
 * @param expirationDate expiration date
 * @varianceOperations qualified variance operations
 * @param qualificationStatus qualification status
 */
public final class StepOperatorQualification extends AbstractStepEntity {
    private final String operatorId;
    private final String qualificationType;
    private final int varianceLevel;
    private final StepEntity qualificationDate;
    private final StepEntity expirationDate;
    private final List<StepEntity> varianceOperations;
    private final String qualificationStatus;

    public StepOperatorQualification(int id, String name, String operatorId, String qualificationType, int varianceLevel, StepEntity qualificationDate, StepEntity expirationDate, List<StepEntity> varianceOperations, String qualificationStatus) {
        super(id, name);
        this.operatorId = operatorId;
        this.qualificationType = qualificationType;
        this.varianceLevel = varianceLevel;
        this.qualificationDate = qualificationDate;
        this.expirationDate = expirationDate;
        this.varianceOperations = varianceOperations == null ? null : java.util.List.copyOf(varianceOperations);
        this.qualificationStatus = qualificationStatus;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getQualificationType() {
        return qualificationType;
    }

    public int getVarianceLevel() {
        return varianceLevel;
    }

    public StepEntity getQualificationDate() {
        return qualificationDate;
    }

    public StepEntity getExpirationDate() {
        return expirationDate;
    }

    public List<StepEntity> getVarianceOperations() {
        return varianceOperations;
    }

    public String getQualificationStatus() {
        return qualificationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("operatorId", operatorId);
        state.put("qualificationType", qualificationType);
        state.put("varianceLevel", varianceLevel);
        state.put("qualificationDate", qualificationDate);
        state.put("expirationDate", expirationDate);
        state.put("varianceOperations", varianceOperations);
        state.put("qualificationStatus", qualificationStatus);
        return state;
    }
}
