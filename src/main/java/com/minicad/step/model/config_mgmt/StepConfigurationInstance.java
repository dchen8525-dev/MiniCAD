package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONFIGURATION_INSTANCE.
 * A configuration instance entity.
 *
 * @param id STEP instance id
 * @param name configuration instance name
 * @param configurationDefinition configuration variance definition reference
 * @param configurationState configuration variance state
 * @param configurationValues configuration variance current values
 * @param configurationApplied configuration variance applied flag
 * @param configurationStatus configuration variance status
 */
public record StepConfigurationInstance(
    int id,
    String name,
    StepEntity configurationDefinition,
    String configurationState,
    List<String> configurationValues,
    boolean configurationApplied,
    String configurationStatus) implements StepEntity {
}