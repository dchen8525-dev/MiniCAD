package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAuditRecord extends AbstractStepEntity {
    private final String auditId;
    private final String auditType;
    private final List<String> varianceFindings;
    private final List<StepEntity> varianceActions;
    private final StepEntity auditDate;
    private final StepEntity auditor;
    private final String varianceStatus;
    private final String auditScope;

    public StepAuditRecord(int id, String name, String auditId, String auditType, List<String> varianceFindings, List<StepEntity> varianceActions, StepEntity auditDate, StepEntity auditor, String varianceStatus, String auditScope) {
        super(id, name);
        this.auditId = auditId;
        this.auditType = auditType;
        this.varianceFindings = varianceFindings == null ? null : java.util.List.copyOf(varianceFindings);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.auditDate = auditDate;
        this.auditor = auditor;
        this.varianceStatus = varianceStatus;
        this.auditScope = auditScope;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("auditId", auditId);
        state.put("auditType", auditType);
        state.put("varianceFindings", varianceFindings);
        state.put("varianceActions", varianceActions);
        state.put("auditDate", auditDate);
        state.put("auditor", auditor);
        state.put("varianceStatus", varianceStatus);
        state.put("auditScope", auditScope);
        return state;
    }
}
