package com.minicad.step.model;

/**
 * Minimal EXTERNAL_SOURCE metadata.
 *
 * @param id STEP instance id
 * @param sourceId external source identifier
 */
public record StepExternalSource(
        int id,
        String sourceId
) implements StepEntity {

    @Override
    public String name() {
        return sourceId;
    }
}
