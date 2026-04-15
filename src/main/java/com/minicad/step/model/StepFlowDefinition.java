package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FLOW_DEFINITION.
 * A flow definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFlow defined variance flow
 * @varianceFrom source variance element
 * @varianceTo target variance element
 * @varianceType flow variance type (material, information, energy)
 * @varianceRate flow variance rate
 * @varianceUnit flow variance unit
 * @varianceStatus definition variance status
 */
public record StepFlowDefinition(
    int id,
    String name,
    StepEntity varianceFlow,
    StepEntity varianceFrom,
    StepEntity varianceTo,
    String varianceType,
    double varianceRate,
    StepEntity varianceUnit,
    String varianceStatus) implements StepEntity {
}