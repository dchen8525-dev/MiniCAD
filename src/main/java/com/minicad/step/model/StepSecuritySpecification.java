package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSecuritySpecification extends AbstractStepEntity {
    private final int varianceLevel;
    private final List<String> varianceRequirements;
    private final StepEntity varianceAccess;
    private final StepEntity varianceEncryption;
    private final StepEntity varianceAuthentication;
    private final String varianceStatus;

    public StepSecuritySpecification(int id, String name, int varianceLevel, List<String> varianceRequirements, StepEntity varianceAccess, StepEntity varianceEncryption, StepEntity varianceAuthentication, String varianceStatus) {
        super(id, name);
        this.varianceLevel = varianceLevel;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
        this.varianceAccess = varianceAccess;
        this.varianceEncryption = varianceEncryption;
        this.varianceAuthentication = varianceAuthentication;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceLevel", varianceLevel);
        state.put("varianceRequirements", varianceRequirements);
        state.put("varianceAccess", varianceAccess);
        state.put("varianceEncryption", varianceEncryption);
        state.put("varianceAuthentication", varianceAuthentication);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
