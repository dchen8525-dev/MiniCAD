package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REGULATORY_COMPLIANCE.
 * A regulatory compliance entity.
 *
 * @param id STEP instance id
 * @param name compliance name
 * @param regulationType regulation type (CE, UL, FCC, RoHS)
 * @param regulationDescription regulation description
 * @varianceStatus compliance variance status
 * @param certificationReference certification reference number
 * @varianceDate certification variance date
 * @varianceRequirements compliance variance requirements
 */
/**
 * Resolved REGULATORY_COMPLIANCE.
 * A regulatory compliance entity.
 *
 * @param id STEP instance id
 * @param name compliance name
 * @param regulationType regulation type (CE, UL, FCC, RoHS)
 * @param regulationDescription regulation description
 * @varianceStatus compliance variance status
 * @param certificationReference certification reference number
 * @varianceDate certification variance date
 * @varianceRequirements compliance variance requirements
 */
public final class StepRegulatoryCompliance implements StepEntity {
    private final int id;
    private final String name;
    private final String regulationType;
    private final String regulationDescription;
    private final String varianceStatus;
    private final String certificationReference;
    private final StepEntity varianceDate;
    private final List<StepEntity> varianceRequirements;

    public StepRegulatoryCompliance(int id, String name, String regulationType, String regulationDescription, String varianceStatus, String certificationReference, StepEntity varianceDate, List<StepEntity> varianceRequirements) {
        this.id = id;
        this.name = name;
        this.regulationType = regulationType;
        this.regulationDescription = regulationDescription;
        this.varianceStatus = varianceStatus;
        this.certificationReference = certificationReference;
        this.varianceDate = varianceDate;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegulationType() {
        return regulationType;
    }

    public String getRegulationDescription() {
        return regulationDescription;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public String getCertificationReference() {
        return certificationReference;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<StepEntity> getVarianceRequirements() {
        return varianceRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRegulatoryCompliance that = (StepRegulatoryCompliance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(regulationType, that.regulationType) && Objects.equals(regulationDescription, that.regulationDescription) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(certificationReference, that.certificationReference) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceRequirements, that.varianceRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regulationType, regulationDescription, varianceStatus, certificationReference, varianceDate, varianceRequirements);
    }

    @Override
    public String toString() {
        return "StepRegulatoryCompliance{" + "id=" + id + "name=" + name + "regulationType=" + regulationType + "regulationDescription=" + regulationDescription + "varianceStatus=" + varianceStatus + "certificationReference=" + certificationReference + "varianceDate=" + varianceDate + "varianceRequirements=" + varianceRequirements + "}";
    }
}