package com.minicad.step.model;

/**
 * Resolved LINK_INSTANCE.
 * A link instance entity.
 *
 * @param id STEP instance id
 * @param name link instance name
 * @param linkDefinition link variance definition reference
 * @param linkState link variance state
 * @param linkUtilization link variance utilization
 * @param linkStatus link variance status
 */
public record StepLinkInstance(
    int id,
    String name,
    StepEntity linkDefinition,
    String linkState,
    double linkUtilization,
    String linkStatus) implements StepEntity {
}