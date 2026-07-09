package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCredentialRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String credentialType;
    private final StepEntity credentialHolder;
    private final boolean credentialValid;
    private final StepEntity credentialExpiry;
    private final String credentialStatus;

    public StepCredentialRecord(int id, String name, String credentialType, StepEntity credentialHolder, boolean credentialValid, StepEntity credentialExpiry, String credentialStatus) {
        this.id = id;
        this.name = name;
        this.credentialType = credentialType;
        this.credentialHolder = credentialHolder;
        this.credentialValid = credentialValid;
        this.credentialExpiry = credentialExpiry;
        this.credentialStatus = credentialStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCredentialRecord that = (StepCredentialRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(credentialType, that.credentialType) && Objects.equals(credentialHolder, that.credentialHolder) && credentialValid == that.credentialValid && Objects.equals(credentialExpiry, that.credentialExpiry) && Objects.equals(credentialStatus, that.credentialStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, credentialType, credentialHolder, credentialValid, credentialExpiry, credentialStatus);
    }

    @Override
    public String toString() {
        return "StepCredentialRecord{" + "id=" + id + "name=" + name + "credentialType=" + credentialType + "credentialHolder=" + credentialHolder + "credentialValid=" + credentialValid + "credentialExpiry=" + credentialExpiry + "credentialStatus=" + credentialStatus + "}";
    }
}