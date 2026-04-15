package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STANDARD_DEFINITION.
 * A standard definition entity.
 *
 * @param id STEP instance id
 * @param name standard name
 * @param standardType standard variance type
 * @param standardCode standard variance code/identifier
 * @param standardVersion standard variance version
 * @param standardRequirements standard variance requirements
 * @param standardStatus standard variance status
 */
public record StepStandardDefinition(
    int id,
    String name,
    String standardType,
    String standardCode,
    String standardVersion,
    List<String> standardRequirements,
    String standardStatus) implements StepEntity {
}