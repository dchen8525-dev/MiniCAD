package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ACCESS_CONTROL.
 * An access control entity.
 *
 * @param id STEP instance id
 * @param name control name
 * @varianceRoles access variance roles
 * @variancePermissions access variance permissions
 * @varianceResources protected variance resources
 * @variancePolicy access variance policy
 * @varianceStatus control variance status
 */
/**
 * Resolved ACCESS_CONTROL.
 * An access control entity.
 *
 * @param id STEP instance id
 * @param name control name
 * @varianceRoles access variance roles
 * @variancePermissions access variance permissions
 * @varianceResources protected variance resources
 * @variancePolicy access variance policy
 * @varianceStatus control variance status
 */
public final class StepAccessControl implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceRoles;
    private final List<String> variancePermissions;
    private final List<StepEntity> varianceResources;
    private final String variancePolicy;
    private final String varianceStatus;

    public StepAccessControl(int id, String name, List<StepEntity> varianceRoles, List<String> variancePermissions, List<StepEntity> varianceResources, String variancePolicy, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceRoles = varianceRoles == null ? null : java.util.List.copyOf(varianceRoles);
        this.variancePermissions = variancePermissions == null ? null : java.util.List.copyOf(variancePermissions);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.variancePolicy = variancePolicy;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getVarianceRoles() {
        return varianceRoles;
    }

    public List<String> getVariancePermissions() {
        return variancePermissions;
    }

    public List<StepEntity> getVarianceResources() {
        return varianceResources;
    }

    public String getVariancePolicy() {
        return variancePolicy;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAccessControl that = (StepAccessControl) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceRoles, that.varianceRoles) && Objects.equals(variancePermissions, that.variancePermissions) && Objects.equals(varianceResources, that.varianceResources) && Objects.equals(variancePolicy, that.variancePolicy) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceRoles, variancePermissions, varianceResources, variancePolicy, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAccessControl{" + "id=" + id + "name=" + name + "varianceRoles=" + varianceRoles + "variancePermissions=" + variancePermissions + "varianceResources=" + varianceResources + "variancePolicy=" + variancePolicy + "varianceStatus=" + varianceStatus + "}";
    }
}