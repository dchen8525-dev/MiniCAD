package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CONFIGURATION_ITEM.
 * A configuration-managed product definition.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description item description
 * @param itemConceived product definition being configured
 * @param purpose configuration purpose
 */
public record StepConfigurationItem(
    int id,
    String name,
    String description,
    StepEntity itemConceived,
    String purpose) implements StepEntity {
}
