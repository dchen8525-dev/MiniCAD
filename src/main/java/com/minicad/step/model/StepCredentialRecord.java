package com.minicad.step.model;

import java.util.List;

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
public record StepCredentialRecord(
    int id,
    String name,
    String credentialType,
    StepEntity credentialHolder,
    boolean credentialValid,
    StepEntity credentialExpiry,
    String credentialStatus) implements StepEntity {
}