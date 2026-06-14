package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepOperatorQualification implements StepEntity {
    private final int id;
    private final String name;
    private final String operatorId;
    private final String qualificationType;
    private final int varianceLevel;
    private final StepEntity qualificationDate;
    private final StepEntity expirationDate;
    private final List<StepEntity> varianceOperations;
    private final String qualificationStatus;

    public StepOperatorQualification(int id, String name, String operatorId, String qualificationType, int varianceLevel, StepEntity qualificationDate, StepEntity expirationDate, List<StepEntity> varianceOperations, String qualificationStatus) {
        this.id = id;
        this.name = name;
        this.operatorId = operatorId;
        this.qualificationType = qualificationType;
        this.varianceLevel = varianceLevel;
        this.qualificationDate = qualificationDate;
        this.expirationDate = expirationDate;
        this.varianceOperations = varianceOperations == null ? null : java.util.List.copyOf(varianceOperations);
        this.qualificationStatus = qualificationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOperatorQualification that = (StepOperatorQualification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(operatorId, that.operatorId) && Objects.equals(qualificationType, that.qualificationType) && varianceLevel == that.varianceLevel && Objects.equals(qualificationDate, that.qualificationDate) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(varianceOperations, that.varianceOperations) && Objects.equals(qualificationStatus, that.qualificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, operatorId, qualificationType, varianceLevel, qualificationDate, expirationDate, varianceOperations, qualificationStatus);
    }

    @Override
    public String toString() {
        return "StepOperatorQualification{" + "id=" + id + "name=" + name + "operatorId=" + operatorId + "qualificationType=" + qualificationType + "varianceLevel=" + varianceLevel + "qualificationDate=" + qualificationDate + "expirationDate=" + expirationDate + "varianceOperations=" + varianceOperations + "qualificationStatus=" + qualificationStatus + "}";
    }
}