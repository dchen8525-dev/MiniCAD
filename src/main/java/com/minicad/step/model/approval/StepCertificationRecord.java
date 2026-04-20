package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CERTIFICATION_RECORD.
 * A certification record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson certified variance person
 * @param certificationType certification type (skill, quality, safety)
 * @varianceLevel certification variance level
 * @varianceDate certification variance date
 * @varianceExpiration expiration variance date
 * @varianceAuthority certification variance authority
 * @varianceStatus certification variance status
 */
public record StepCertificationRecord(
    int id,
    String name,
    StepEntity variancePerson,
    String certificationType,
    int varianceLevel,
    StepEntity varianceDate,
    StepEntity varianceExpiration,
    StepEntity varianceAuthority,
    String varianceStatus) implements StepEntity {
}