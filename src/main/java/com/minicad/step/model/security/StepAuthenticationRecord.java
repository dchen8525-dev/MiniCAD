package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAuthenticationRecord(
    int id,
    String name,
    String authType,
    String authResult,
    StepEntity authHolder,
    StepEntity authTimestamp,
    List<String> authDetails,
    String authStatus) implements StepEntity {
}