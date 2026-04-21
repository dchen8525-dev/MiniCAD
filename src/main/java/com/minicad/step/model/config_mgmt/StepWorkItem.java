package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved WORK_ITEM.
 * A work item in AP203 configuration management.
 */
public record StepWorkItem(
    int id,
    String name,
    String description) implements StepEntity {
}
