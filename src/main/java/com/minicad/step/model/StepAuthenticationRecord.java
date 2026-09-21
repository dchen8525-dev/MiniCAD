package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved AUTHENTICATION_RECORD.
 * An authentication record entity.
 *
 * @param id STEP instance id
 * @param name authentication name
 * @param authType authentication variance type
 * @param authResult authentication variance result (success/failure)
 * @param authHolder authentication variance holder reference
 * @param authTimestamp authentication variance timestamp
 * @param authDetails authentication variance details
 * @param authStatus authentication variance status
 */
public final class StepAuthenticationRecord extends AbstractStepEntity {
    private final String authType;
    private final String authResult;
    private final StepEntity authHolder;
    private final StepEntity authTimestamp;
    private final List<String> authDetails;
    private final String authStatus;

    public StepAuthenticationRecord(int id, String name, String authType, String authResult, StepEntity authHolder, StepEntity authTimestamp, List<String> authDetails, String authStatus) {
        super(id, name);
        this.authType = authType;
        this.authResult = authResult;
        this.authHolder = authHolder;
        this.authTimestamp = authTimestamp;
        this.authDetails = authDetails == null ? null : java.util.List.copyOf(authDetails);
        this.authStatus = authStatus;
    }

    public String getAuthType() {
        return authType;
    }

    public String getAuthResult() {
        return authResult;
    }

    public StepEntity getAuthHolder() {
        return authHolder;
    }

    public StepEntity getAuthTimestamp() {
        return authTimestamp;
    }

    public List<String> getAuthDetails() {
        return authDetails;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("authType", authType);
        state.put("authResult", authResult);
        state.put("authHolder", authHolder);
        state.put("authTimestamp", authTimestamp);
        state.put("authDetails", authDetails);
        state.put("authStatus", authStatus);
        return state;
    }
}
