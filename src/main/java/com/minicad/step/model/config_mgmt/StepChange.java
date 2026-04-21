package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CHANGE.
 * A change record in AP203 configuration management.
 */
public record StepChange(
    int id,
    String name,
    String description) implements StepEntity {
}
