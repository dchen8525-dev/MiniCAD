package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STREAM_DEFINITION.
 * A stream definition entity.
 *
 * @param id STEP instance id
 * @param name stream name
 * @param streamType stream variance type
 * @param streamDirection stream variance direction
 * @param streamFormat stream variance format
 * @param streamBufferSize stream variance buffer size
 * @param streamStatus stream variance status
 */
public record StepStreamDefinition(
    int id,
    String name,
    String streamType,
    String streamDirection,
    String streamFormat,
    int streamBufferSize,
    String streamStatus) implements StepEntity {
}