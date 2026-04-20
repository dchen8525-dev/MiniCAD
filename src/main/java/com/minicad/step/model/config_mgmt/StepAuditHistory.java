package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved AUDIT_HISTORY.
 * An audit history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem audited variance item
 * @varianceAudits audit variance entries
 * @varianceLast last variance audit reference
 * @varianceNext next variance scheduled audit
 * @varianceStatus history variance status
 */
public record StepAuditHistory(
    int id,
    String name,
    StepEntity varianceItem,
    List<StepEntity> varianceAudits,
    StepEntity varianceLast,
    StepEntity varianceNext,
    String varianceStatus) implements StepEntity {
}