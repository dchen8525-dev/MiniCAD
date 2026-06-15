package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved CLOSED_SHELL.
 *
 * @param id step id
 * @param name step label
 * @param faces shell faces
 */
/**
 * Resolved CLOSED_SHELL.
 *
 * @param id step id
 * @param name step label
 * @param faces shell faces
 */
public final class StepClosedShell implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepFaceEntity> faces;

    public StepClosedShell(int id, String name, List<StepFaceEntity> faces) {
        this.id = id;
        this.name = name;
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepFaceEntity> getFaces() {
        return faces;
    }

    // Record-style accessor
    public List<StepFaceEntity> faces() {
        return faces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClosedShell that = (StepClosedShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faces, that.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faces);
    }

    @Override
    public String toString() {
        return "StepClosedShell{" + "id=" + id + "name=" + name + "faces=" + faces + "}";
    }
}
