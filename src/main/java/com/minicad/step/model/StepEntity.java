package com.minicad.step.model;

/**
 * Marker interface for resolved STEP semantic entities.
 */
public interface StepEntity {

    /**
     * Returns the original STEP instance id.
     *
     * @return instance id
     */
    int getId();

    /**
     * Returns the optional STEP label/name field.
     *
     * @return label or empty string
     */
    String getName();

    // Record-style accessors for compatibility
    default int id() { return getId(); }
    default String name() { return getName(); }
}
