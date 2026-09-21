package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved AUDIT_DEFINITION.
 * An audit definition entity.
 *
 * @param id STEP instance id
 * @param name audit name
 * @param auditType audit variance type
 * @param auditDescription audit variance description
 * @param auditCriteria audit variance criteria
 * @param auditScope audit variance scope
 * @param auditFrequency audit variance frequency
 * @param auditStatus audit variance status
 */
public final class StepAuditDefinition extends AbstractStepEntity {
    private final String auditType;
    private final String auditDescription;
    private final List<String> auditCriteria;
    private final String auditScope;
    private final String auditFrequency;
    private final String auditStatus;

    public StepAuditDefinition(int id, String name, String auditType, String auditDescription, List<String> auditCriteria, String auditScope, String auditFrequency, String auditStatus) {
        super(id, name);
        this.auditType = auditType;
        this.auditDescription = auditDescription;
        this.auditCriteria = auditCriteria == null ? null : java.util.List.copyOf(auditCriteria);
        this.auditScope = auditScope;
        this.auditFrequency = auditFrequency;
        this.auditStatus = auditStatus;
    }

    public String getAuditType() {
        return auditType;
    }

    public String getAuditDescription() {
        return auditDescription;
    }

    public List<String> getAuditCriteria() {
        return auditCriteria;
    }

    public String getAuditScope() {
        return auditScope;
    }

    public String getAuditFrequency() {
        return auditFrequency;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("auditType", auditType);
        state.put("auditDescription", auditDescription);
        state.put("auditCriteria", auditCriteria);
        state.put("auditScope", auditScope);
        state.put("auditFrequency", auditFrequency);
        state.put("auditStatus", auditStatus);
        return state;
    }
}
