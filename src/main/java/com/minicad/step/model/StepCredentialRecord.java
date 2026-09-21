package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CREDENTIAL_RECORD.
 * A credential record entity.
 *
 * @param id STEP instance id
 * @param name credential name
 * @param credentialType credential variance type
 * @param credentialHolder credential variance holder reference
 * @param credentialValid credential variance valid flag
 * @param credentialExpiry credential variance expiry time
 * @param credentialStatus credential variance status
 */
public final class StepCredentialRecord extends AbstractStepEntity {
    private final String credentialType;
    private final StepEntity credentialHolder;
    private final boolean credentialValid;
    private final StepEntity credentialExpiry;
    private final String credentialStatus;

    public StepCredentialRecord(int id, String name, String credentialType, StepEntity credentialHolder, boolean credentialValid, StepEntity credentialExpiry, String credentialStatus) {
        super(id, name);
        this.credentialType = credentialType;
        this.credentialHolder = credentialHolder;
        this.credentialValid = credentialValid;
        this.credentialExpiry = credentialExpiry;
        this.credentialStatus = credentialStatus;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public StepEntity getCredentialHolder() {
        return credentialHolder;
    }

    public boolean isCredentialValid() {
        return credentialValid;
    }

    public StepEntity getCredentialExpiry() {
        return credentialExpiry;
    }

    public String getCredentialStatus() {
        return credentialStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("credentialType", credentialType);
        state.put("credentialHolder", credentialHolder);
        state.put("credentialValid", credentialValid);
        state.put("credentialExpiry", credentialExpiry);
        state.put("credentialStatus", credentialStatus);
        return state;
    }
}
