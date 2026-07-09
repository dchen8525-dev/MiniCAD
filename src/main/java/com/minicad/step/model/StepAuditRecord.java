package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuditRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String auditId;
    private final String auditType;
    private final List<String> varianceFindings;
    private final List<StepEntity> varianceActions;
    private final StepEntity auditDate;
    private final StepEntity auditor;
    private final String varianceStatus;
    private final String auditScope;

    public StepAuditRecord(int id, String name, String auditId, String auditType, List<String> varianceFindings, List<StepEntity> varianceActions, StepEntity auditDate, StepEntity auditor, String varianceStatus, String auditScope) {
        this.id = id;
        this.name = name;
        this.auditId = auditId;
        this.auditType = auditType;
        this.varianceFindings = varianceFindings == null ? null : java.util.List.copyOf(varianceFindings);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.auditDate = auditDate;
        this.auditor = auditor;
        this.varianceStatus = varianceStatus;
        this.auditScope = auditScope;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuditId() {
        return auditId;
    }

    public String getAuditType() {
        return auditType;
    }

    public List<String> getVarianceFindings() {
        return varianceFindings;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public StepEntity getAuditDate() {
        return auditDate;
    }

    public StepEntity getAuditor() {
        return auditor;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public String getAuditScope() {
        return auditScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuditRecord that = (StepAuditRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(auditId, that.auditId) && Objects.equals(auditType, that.auditType) && Objects.equals(varianceFindings, that.varianceFindings) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(auditDate, that.auditDate) && Objects.equals(auditor, that.auditor) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(auditScope, that.auditScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, auditId, auditType, varianceFindings, varianceActions, auditDate, auditor, varianceStatus, auditScope);
    }

    @Override
    public String toString() {
        return "StepAuditRecord{" + "id=" + id + "name=" + name + "auditId=" + auditId + "auditType=" + auditType + "varianceFindings=" + varianceFindings + "varianceActions=" + varianceActions + "auditDate=" + auditDate + "auditor=" + auditor + "varianceStatus=" + varianceStatus + "auditScope=" + auditScope + "}";
    }
}