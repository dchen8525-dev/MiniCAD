package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MACHINED_SURFACE.
 * Represents a surface that has been machined.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param face the face that was machined
 */
/**
 * Resolved MACHINED_SURFACE.
 * Represents a surface that has been machined.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param face the face that was machined
 */
public final class StepMachinedSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity face;

    public StepMachinedSurface(int id, String name, StepEntity face) {
        this.id = id;
        this.name = name;
        this.face = face;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFace() {
        return face;
    }

    // Record-style accessor
    public StepEntity face() { return getFace(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachinedSurface that = (StepMachinedSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(face, that.face);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, face);
    }

    @Override
    public String toString() {
        return "StepMachinedSurface{" + "id=" + id + "name=" + name + "face=" + face + "}";
    }
}