package com.minicad.step.model.base;

/**
 * Minimal geometric representation item marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public record StepGeometricRepresentationItem(int id, String name) implements StepEntity {
}
