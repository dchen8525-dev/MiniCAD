package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DATA_FLOW_INSTANCE.
 * A data flow instance entity.
 *
 * @param id STEP instance id
 * @param name data flow instance name
 * @param flowDefinition flow variance definition reference
 * @param flowState flow variance state
 * @param flowRate flow variance current rate
 * @param flowData flow variance data content
 * @param flowStatus flow variance status
 */
public record StepDataFlowInstance(
    int id,
    String name,
    StepEntity flowDefinition,
    String flowState,
    double flowRate,
    List<String> flowData,
    String flowStatus) implements StepEntity {
}