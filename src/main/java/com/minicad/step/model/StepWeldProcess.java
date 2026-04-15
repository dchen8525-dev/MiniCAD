package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WELD_PROCESS.
 * A weld process entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processParameters process variance parameters
 * @param processEquipment process variance equipment reference
 * @param processStatus process variance status
 */
public record StepWeldProcess(
    int id,
    String name,
    String processType,
    List<String> processParameters,
    StepEntity processEquipment,
    String processStatus) implements StepEntity {
}