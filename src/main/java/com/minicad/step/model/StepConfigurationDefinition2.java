package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONFIGURATION_DEFINITION.
 * A configuration definition entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationType configuration variance type
 * @param configurationDescription configuration variance description
 * @param configurationParameters configuration variance parameters
 * @param configurationDefaults configuration variance defaults
 * @param configurationStatus configuration variance status
 */
public record StepConfigurationDefinition2(
    int id,
    String name,
    String configurationType,
    String configurationDescription,
    List<String> configurationParameters,
    List<String> configurationDefaults,
    String configurationStatus) implements StepEntity {
}