package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SESSION_DEFINITION.
 * A session definition entity.
 *
 * @param id STEP instance id
 * @param name session name
 * @param sessionType session variance type
 * @param sessionTimeout session variance timeout in seconds
 * @param sessionMaxInactive session variance max inactive time
 * @param sessionFeatures session variance features
 * @param sessionStatus session variance status
 */
public final class StepSessionDefinition extends AbstractStepEntity {
    private final String sessionType;
    private final int sessionTimeout;
    private final int sessionMaxInactive;
    private final List<String> sessionFeatures;
    private final String sessionStatus;

    public StepSessionDefinition(int id, String name, String sessionType, int sessionTimeout, int sessionMaxInactive, List<String> sessionFeatures, String sessionStatus) {
        super(id, name);
        this.sessionType = sessionType;
        this.sessionTimeout = sessionTimeout;
        this.sessionMaxInactive = sessionMaxInactive;
        this.sessionFeatures = sessionFeatures == null ? null : java.util.List.copyOf(sessionFeatures);
        this.sessionStatus = sessionStatus;
    }

    public String getSessionType() {
        return sessionType;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public int getSessionMaxInactive() {
        return sessionMaxInactive;
    }

    public List<String> getSessionFeatures() {
        return sessionFeatures;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sessionType", sessionType);
        state.put("sessionTimeout", sessionTimeout);
        state.put("sessionMaxInactive", sessionMaxInactive);
        state.put("sessionFeatures", sessionFeatures);
        state.put("sessionStatus", sessionStatus);
        return state;
    }
}
