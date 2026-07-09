package com.minicad.step.model.topology;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ORIENTED_SUBFACE.
 * An oriented reference to a sub-face.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying subface entity
 * @param orientation orientation flag
 */
/**
 * Resolved ORIENTED_SUBFACE.
 * An oriented reference to a sub-face.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying subface entity
 * @param orientation orientation flag
 */
public final class StepOrientedSubface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity faceElement;
    private final boolean orientation;

    public StepOrientedSubface(int id, String name, StepEntity faceElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.faceElement = faceElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFaceElement() {
        return faceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public StepEntity faceElement() {
        return faceElement;
    }

    public boolean orientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedSubface that = (StepOrientedSubface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faceElement, that.faceElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faceElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedSubface{" + "id=" + id + "name=" + name + "faceElement=" + faceElement + "orientation=" + orientation + "}";
    }
}
