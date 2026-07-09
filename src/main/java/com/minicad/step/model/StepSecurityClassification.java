package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SECURITY_CLASSIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name classification name
 * @param purpose classification purpose
 * @param securityLevel classification level
 */
/**
 * Minimal SECURITY_CLASSIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name classification name
 * @param purpose classification purpose
 * @param securityLevel classification level
 */
public final class StepSecurityClassification implements StepEntity {
    private final int id;
    private final String name;
    private final String purpose;
    private final StepSecurityClassificationLevel securityLevel;

    public StepSecurityClassification(int id, String name, String purpose, StepSecurityClassificationLevel securityLevel) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.securityLevel = securityLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public StepSecurityClassificationLevel getSecurityLevel() {
        return securityLevel;
    }

    // Record-style accessor
    public StepSecurityClassificationLevel securityLevel() {
        return securityLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSecurityClassification that = (StepSecurityClassification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(purpose, that.purpose) && Objects.equals(securityLevel, that.securityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, purpose, securityLevel);
    }

    @Override
    public String toString() {
        return "StepSecurityClassification{" + "id=" + id + "name=" + name + "purpose=" + purpose + "securityLevel=" + securityLevel + "}";
    }
}
