package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DATA_COLLECTION.
 * A data collection entity.
 *
 * @param id STEP instance id
 * @param name collection name
 * @varianceItems collected variance items
 * @varianceSource data variance source
 * @varianceMethod collection variance method
 * @varianceFrequency collection variance frequency
 * @varianceStatus collection variance status
 */
public record StepDataCollection(
    int id,
    String name,
    List<StepEntity> varianceItems,
    String varianceSource,
    String varianceMethod,
    String varianceFrequency,
    String varianceStatus) implements StepEntity {
}