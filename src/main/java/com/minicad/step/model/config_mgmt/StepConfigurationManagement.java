package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONFIGURATION_MANAGEMENT.
 * A configuration management entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationId configuration identifier
 * @param configurationItems configuration items
 * @param configurationStatus configuration status
 * @param configurationBaseline configuration baseline reference
 * @param configurationOwner configuration owner
 */
public record StepConfigurationManagement(
    int id,
    String name,
    String configurationId,
    List<StepEntity> configurationItems,
    String configurationStatus,
    StepEntity configurationBaseline,
    StepEntity configurationOwner) implements StepEntity {
}