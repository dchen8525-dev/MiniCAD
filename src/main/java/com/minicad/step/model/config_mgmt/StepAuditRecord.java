package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved AUDIT_RECORD.
 * An audit record entity.
 *
 * @param id STEP instance id
 * @param name audit name
 * @param auditId audit identifier
 * @param auditType audit type (quality, process, compliance)
 * @varianceFindings audit variance findings
 * @varianceActions audit variance corrective actions
 * @param auditDate audit date
 * @param auditor auditor reference
 * @varianceStatus audit variance status
 * @param auditScope audit scope description
 */
public record StepAuditRecord(
    int id,
    String name,
    String auditId,
    String auditType,
    List<String> varianceFindings,
    List<StepEntity> varianceActions,
    StepEntity auditDate,
    StepEntity auditor,
    String varianceStatus,
    String auditScope) implements StepEntity {
}