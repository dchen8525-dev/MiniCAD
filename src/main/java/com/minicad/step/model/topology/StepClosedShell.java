package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;

/**
 * Resolved CLOSED_SHELL.
 *
 * @param id step id
 * @param name step label
 * @param faces shell faces
 */
public record StepClosedShell(int id, String name, List<StepFaceEntity> faces) implements StepEntity {

    /**
     * Creates an immutable shell record.
     */
    public StepClosedShell {
        faces = List.copyOf(faces);
    }
}
