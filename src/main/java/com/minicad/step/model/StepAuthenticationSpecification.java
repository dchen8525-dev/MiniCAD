package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved AUTHENTICATION_SPECIFICATION.
 * An authentication specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceMethod authentication variance method (password, token, certificate)
 * @varianceProvider authentication variance provider
 * @varianceSession session variance management specification
 * @varianceMultiFactor multi-factor variance authentication flag
 * @varianceStatus specification variance status
 */
public final class StepAuthenticationSpecification extends AbstractStepEntity {
    private final String varianceMethod;
    private final StepEntity varianceProvider;
    private final StepEntity varianceSession;
    private final boolean varianceMultiFactor;
    private final String varianceStatus;

    public StepAuthenticationSpecification(int id, String name, String varianceMethod, StepEntity varianceProvider, StepEntity varianceSession, boolean varianceMultiFactor, String varianceStatus) {
        super(id, name);
        this.varianceMethod = varianceMethod;
        this.varianceProvider = varianceProvider;
        this.varianceSession = varianceSession;
        this.varianceMultiFactor = varianceMultiFactor;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceProvider() {
        return varianceProvider;
    }

    public StepEntity getVarianceSession() {
        return varianceSession;
    }

    public boolean isVarianceMultiFactor() {
        return varianceMultiFactor;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceMethod", varianceMethod);
        state.put("varianceProvider", varianceProvider);
        state.put("varianceSession", varianceSession);
        state.put("varianceMultiFactor", varianceMultiFactor);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
