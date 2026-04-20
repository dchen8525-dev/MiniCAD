package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved AUTHORIZATION_ENTRY.
 * An authorization entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryAuthorization entry variance authorization reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public record StepAuthorizationEntry(
    int id,
    String name,
    String entryType,
    StepEntity entryAuthorization,
    StepEntity entryHolder,
    boolean entryGranted,
    StepEntity entryTimestamp,
    String entryStatus) implements StepEntity {
}