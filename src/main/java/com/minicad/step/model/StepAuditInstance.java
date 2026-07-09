package com.minicad.step.model.management.config;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuditInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity auditDefinition;
    private final StepEntity auditStartTime;
    private final StepEntity auditEndTime;
    private final int auditFindings;
    private final boolean auditPassed;
    private final String auditStatus;

    public StepAuditInstance(int id, String name, StepEntity auditDefinition, StepEntity auditStartTime, StepEntity auditEndTime, int auditFindings, boolean auditPassed, String auditStatus) {
        this.id = id;
        this.name = name;
        this.auditDefinition = auditDefinition;
        this.auditStartTime = auditStartTime;
        this.auditEndTime = auditEndTime;
        this.auditFindings = auditFindings;
        this.auditPassed = auditPassed;
        this.auditStatus = auditStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuditInstance that = (StepAuditInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(auditDefinition, that.auditDefinition) && Objects.equals(auditStartTime, that.auditStartTime) && Objects.equals(auditEndTime, that.auditEndTime) && auditFindings == that.auditFindings && auditPassed == that.auditPassed && Objects.equals(auditStatus, that.auditStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, auditDefinition, auditStartTime, auditEndTime, auditFindings, auditPassed, auditStatus);
    }

    @Override
    public String toString() {
        return "StepAuditInstance{" + "id=" + id + "name=" + name + "auditDefinition=" + auditDefinition + "auditStartTime=" + auditStartTime + "auditEndTime=" + auditEndTime + "auditFindings=" + auditFindings + "auditPassed=" + auditPassed + "auditStatus=" + auditStatus + "}";
    }
}