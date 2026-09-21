package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved AUDIT_INSTANCE.
 * An audit instance entity.
 *
 * @param id STEP instance id
 * @param name audit instance name
 * @param auditDefinition audit variance definition reference
 * @param auditStartTime audit variance start time
 * @param auditEndTime audit variance end time
 * @param auditFindings audit variance findings count
 * @param auditPassed audit variance passed flag
 * @param auditStatus audit variance status
 */
public final class StepAuditInstance extends AbstractStepEntity {
    private final StepEntity auditDefinition;
    private final StepEntity auditStartTime;
    private final StepEntity auditEndTime;
    private final int auditFindings;
    private final boolean auditPassed;
    private final String auditStatus;

    public StepAuditInstance(int id, String name, StepEntity auditDefinition, StepEntity auditStartTime, StepEntity auditEndTime, int auditFindings, boolean auditPassed, String auditStatus) {
        super(id, name);
        this.auditDefinition = auditDefinition;
        this.auditStartTime = auditStartTime;
        this.auditEndTime = auditEndTime;
        this.auditFindings = auditFindings;
        this.auditPassed = auditPassed;
        this.auditStatus = auditStatus;
    }

    public StepEntity getAuditDefinition() {
        return auditDefinition;
    }

    public StepEntity getAuditStartTime() {
        return auditStartTime;
    }

    public StepEntity getAuditEndTime() {
        return auditEndTime;
    }

    public int getAuditFindings() {
        return auditFindings;
    }

    public boolean isAuditPassed() {
        return auditPassed;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("auditDefinition", auditDefinition);
        state.put("auditStartTime", auditStartTime);
        state.put("auditEndTime", auditEndTime);
        state.put("auditFindings", auditFindings);
        state.put("auditPassed", auditPassed);
        state.put("auditStatus", auditStatus);
        return state;
    }
}
