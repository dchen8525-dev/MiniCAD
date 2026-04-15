package com.minicad.step.model;

import java.util.List;

/**
 * Resolved METADATA_RECORD.
 * A metadata record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param metadataType metadata variance type
 * @param metadataKey metadata variance key
 * @param metadataValue metadata variance value
 * @param metadataSource metadata variance source reference
 * @param metadataTimestamp metadata variance timestamp
 * @param metadataStatus metadata variance status
 */
public record StepMetadataRecord(
    int id,
    String name,
    String metadataType,
    String metadataKey,
    String metadataValue,
    StepEntity metadataSource,
    StepEntity metadataTimestamp,
    String metadataStatus) implements StepEntity {
}