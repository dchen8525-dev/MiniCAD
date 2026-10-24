package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepRegulatoryCompliance extends AbstractStepEntity {
    private final String regulationType;
    private final String regulationDescription;
    private final String varianceStatus;
    private final String certificationReference;
    private final StepEntity varianceDate;
    private final List<StepEntity> varianceRequirements;

    public StepRegulatoryCompliance(int id, String name, String regulationType, String regulationDescription, String varianceStatus, String certificationReference, StepEntity varianceDate, List<StepEntity> varianceRequirements) {
        super(id, name);
        this.regulationType = regulationType;
        this.regulationDescription = regulationDescription;
        this.varianceStatus = varianceStatus;
        this.certificationReference = certificationReference;
        this.varianceDate = varianceDate;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "regulationType", regulationType,
                "regulationDescription", regulationDescription, "varianceStatus", varianceStatus,
                "certificationReference", certificationReference, "varianceDate", varianceDate,
                "varianceRequirements", varianceRequirements);
    }
}
