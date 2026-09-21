package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAccessControl extends AbstractStepEntity {
    private final List<StepEntity> varianceRoles;
    private final List<String> variancePermissions;
    private final List<StepEntity> varianceResources;
    private final String variancePolicy;
    private final String varianceStatus;

    public StepAccessControl(int id, String name, List<StepEntity> varianceRoles, List<String> variancePermissions, List<StepEntity> varianceResources, String variancePolicy, String varianceStatus) {
        super(id, name);
        this.varianceRoles = varianceRoles == null ? null : java.util.List.copyOf(varianceRoles);
        this.variancePermissions = variancePermissions == null ? null : java.util.List.copyOf(variancePermissions);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.variancePolicy = variancePolicy;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceRoles", varianceRoles);
        state.put("variancePermissions", variancePermissions);
        state.put("varianceResources", varianceResources);
        state.put("variancePolicy", variancePolicy);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
