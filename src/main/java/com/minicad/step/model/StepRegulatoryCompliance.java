package com.minicad.step.model;

import java.util.List;

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
public record StepRegulatoryCompliance(
    int id,
    String name,
    String regulationType,
    String regulationDescription,
    String varianceStatus,
    String certificationReference,
    StepEntity varianceDate,
    List<StepEntity> varianceRequirements) implements StepEntity {
}