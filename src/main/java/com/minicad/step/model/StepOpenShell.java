package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OPEN_SHELL.
 *
 * @param id step id
 * @param name step label
 * @param faces shell faces
 */
public record StepOpenShell(int id, String name, List<StepFaceEntity> faces) implements StepEntity {

    /**
     * Creates an immutable shell record.
     */
    public StepOpenShell {
        faces = List.copyOf(faces);
    }
}
