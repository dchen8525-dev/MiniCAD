package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved START_WORK.
 * A start work record in AP203 configuration management.
 */
public record StepStartWork(
    int id,
    String name,
    String description) implements StepEntity {
}
