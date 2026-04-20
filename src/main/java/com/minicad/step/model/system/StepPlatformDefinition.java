package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PLATFORM_DEFINITION.
 * A platform definition entity.
 *
 * @param id STEP instance id
 * @param name platform name
 * @param platformType platform variance type
 * @param platformDescription platform variance description
 * @param platformComponents platform variance component definitions
 * @param platformCapabilities platform variance capabilities
 * @param platformStatus platform variance status
 */
public record StepPlatformDefinition(
    int id,
    String name,
    String platformType,
    String platformDescription,
    List<StepEntity> platformComponents,
    List<String> platformCapabilities,
    String platformStatus) implements StepEntity {
}