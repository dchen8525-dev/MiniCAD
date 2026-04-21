package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved START_REQUEST.
 * A start request in AP203 configuration management.
 */
public record StepStartRequest(
    int id,
    String name,
    String description) implements StepEntity {
}
