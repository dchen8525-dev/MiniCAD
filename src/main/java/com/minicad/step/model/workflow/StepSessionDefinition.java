package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepSessionDefinition(
    int id,
    String name,
    String sessionType,
    int sessionTimeout,
    int sessionMaxInactive,
    List<String> sessionFeatures,
    String sessionStatus) implements StepEntity {
}