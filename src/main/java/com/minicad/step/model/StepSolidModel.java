package com.minicad.step.model;

/**
 * Minimal solid-model marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public record StepSolidModel(int id, String name) implements StepEntity {
}
