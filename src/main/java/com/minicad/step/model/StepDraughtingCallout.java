package com.minicad.step.model;

import java.util.List;

/**
 * Minimal draughting callout containing PMI text and optional leader geometry.
 *
 * @param id STEP instance id
 * @param name callout name
 * @param contents callout contents
 */
public record StepDraughtingCallout(int id, String name, List<StepEntity> contents) implements StepEntity {

    public StepDraughtingCallout {
        contents = List.copyOf(contents);
    }
}
