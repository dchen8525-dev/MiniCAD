package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DATA_FLOW_DEFINITION.
 * A data flow definition entity.
 *
 * @param id STEP instance id
 * @param name data flow name
 * @param flowType flow variance type
 * @param flowDirection flow variance direction
 * @param flowSource flow variance source reference
 * @param flowTarget flow variance target reference
 * @param flowProtocol flow variance protocol
 * @param flowStatus flow variance status
 */
public record StepDataFlowDefinition(
    int id,
    String name,
    String flowType,
    String flowDirection,
    StepEntity flowSource,
    StepEntity flowTarget,
    String flowProtocol,
    String flowStatus) implements StepEntity {
}