package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuthenticationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String authType;
    private final String authResult;
    private final StepEntity authHolder;
    private final StepEntity authTimestamp;
    private final List<String> authDetails;
    private final String authStatus;

    public StepAuthenticationRecord(int id, String name, String authType, String authResult, StepEntity authHolder, StepEntity authTimestamp, List<String> authDetails, String authStatus) {
        this.id = id;
        this.name = name;
        this.authType = authType;
        this.authResult = authResult;
        this.authHolder = authHolder;
        this.authTimestamp = authTimestamp;
        this.authDetails = authDetails == null ? null : java.util.List.copyOf(authDetails);
        this.authStatus = authStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuthenticationRecord that = (StepAuthenticationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(authType, that.authType) && Objects.equals(authResult, that.authResult) && Objects.equals(authHolder, that.authHolder) && Objects.equals(authTimestamp, that.authTimestamp) && Objects.equals(authDetails, that.authDetails) && Objects.equals(authStatus, that.authStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authType, authResult, authHolder, authTimestamp, authDetails, authStatus);
    }

    @Override
    public String toString() {
        return "StepAuthenticationRecord{" + "id=" + id + "name=" + name + "authType=" + authType + "authResult=" + authResult + "authHolder=" + authHolder + "authTimestamp=" + authTimestamp + "authDetails=" + authDetails + "authStatus=" + authStatus + "}";
    }
}