package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved SUBFACE.
 * A sub-face of a connected face set.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying face entity
 */
/**
 * Resolved SUBFACE.
 * A sub-face of a connected face set.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying face entity
 */
public final class StepSubface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity faceElement;

    public StepSubface(int id, String name, StepEntity faceElement) {
        this.id = id;
        this.name = name;
        this.faceElement = faceElement;
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

    // Record-style accessor
    public StepEntity faceElement() {
        return faceElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSubface that = (StepSubface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faceElement, that.faceElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faceElement);
    }

    @Override
    public String toString() {
        return "StepSubface{" + "id=" + id + "name=" + name + "faceElement=" + faceElement + "}";
    }
}
