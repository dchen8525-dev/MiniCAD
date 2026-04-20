package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CERTIFICATION_RECORD_2.
 * A certification record entity for products/systems.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem certified variance item
 * @varianceType certification variance type
 * @varianceNumber certification variance number
 * @varianceAuthority certification variance authority
 * @varianceValid validity variance period
 * @varianceStatus record variance status
 */
public record StepCertificationRecord2(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceType,
    String varianceNumber,
    StepEntity varianceAuthority,
    String varianceValid,
    String varianceStatus) implements StepEntity {
}