package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuditDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String auditType;
    private final String auditDescription;
    private final List<String> auditCriteria;
    private final String auditScope;
    private final String auditFrequency;
    private final String auditStatus;

    public StepAuditDefinition(int id, String name, String auditType, String auditDescription, List<String> auditCriteria, String auditScope, String auditFrequency, String auditStatus) {
        this.id = id;
        this.name = name;
        this.auditType = auditType;
        this.auditDescription = auditDescription;
        this.auditCriteria = auditCriteria == null ? null : java.util.List.copyOf(auditCriteria);
        this.auditScope = auditScope;
        this.auditFrequency = auditFrequency;
        this.auditStatus = auditStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuditDefinition that = (StepAuditDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(auditType, that.auditType) && Objects.equals(auditDescription, that.auditDescription) && Objects.equals(auditCriteria, that.auditCriteria) && Objects.equals(auditScope, that.auditScope) && Objects.equals(auditFrequency, that.auditFrequency) && Objects.equals(auditStatus, that.auditStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, auditType, auditDescription, auditCriteria, auditScope, auditFrequency, auditStatus);
    }

    @Override
    public String toString() {
        return "StepAuditDefinition{" + "id=" + id + "name=" + name + "auditType=" + auditType + "auditDescription=" + auditDescription + "auditCriteria=" + auditCriteria + "auditScope=" + auditScope + "auditFrequency=" + auditFrequency + "auditStatus=" + auditStatus + "}";
    }
}