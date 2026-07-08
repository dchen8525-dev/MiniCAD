package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SECURITY_SPECIFICATION.
 * A security specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceLevel security variance level (low, medium, high)
 * @varianceRequirements security variance requirements
 * @varianceAccess access variance control specification
 * @varianceEncryption encryption variance specification
 * @varianceAuthentication authentication variance specification
 * @varianceStatus specification variance status
 */
/**
 * Resolved SECURITY_SPECIFICATION.
 * A security specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceLevel security variance level (low, medium, high)
 * @varianceRequirements security variance requirements
 * @varianceAccess access variance control specification
 * @varianceEncryption encryption variance specification
 * @varianceAuthentication authentication variance specification
 * @varianceStatus specification variance status
 */
public final class StepSecuritySpecification implements StepEntity {
    private final int id;
    private final String name;
    private final int varianceLevel;
    private final List<String> varianceRequirements;
    private final StepEntity varianceAccess;
    private final StepEntity varianceEncryption;
    private final StepEntity varianceAuthentication;
    private final String varianceStatus;

    public StepSecuritySpecification(int id, String name, int varianceLevel, List<String> varianceRequirements, StepEntity varianceAccess, StepEntity varianceEncryption, StepEntity varianceAuthentication, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceLevel = varianceLevel;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
        this.varianceAccess = varianceAccess;
        this.varianceEncryption = varianceEncryption;
        this.varianceAuthentication = varianceAuthentication;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVarianceLevel() {
        return varianceLevel;
    }

    public List<String> getVarianceRequirements() {
        return varianceRequirements;
    }

    public StepEntity getVarianceAccess() {
        return varianceAccess;
    }

    public StepEntity getVarianceEncryption() {
        return varianceEncryption;
    }

    public StepEntity getVarianceAuthentication() {
        return varianceAuthentication;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSecuritySpecification that = (StepSecuritySpecification) o;
        return id == that.id && Objects.equals(name, that.name) && varianceLevel == that.varianceLevel && Objects.equals(varianceRequirements, that.varianceRequirements) && Objects.equals(varianceAccess, that.varianceAccess) && Objects.equals(varianceEncryption, that.varianceEncryption) && Objects.equals(varianceAuthentication, that.varianceAuthentication) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceLevel, varianceRequirements, varianceAccess, varianceEncryption, varianceAuthentication, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSecuritySpecification{" + "id=" + id + "name=" + name + "varianceLevel=" + varianceLevel + "varianceRequirements=" + varianceRequirements + "varianceAccess=" + varianceAccess + "varianceEncryption=" + varianceEncryption + "varianceAuthentication=" + varianceAuthentication + "varianceStatus=" + varianceStatus + "}";
    }
}