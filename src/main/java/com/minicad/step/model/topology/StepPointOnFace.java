package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved POINT_ON_FACE.
 * A point located on a face.
 */
/**
 * Resolved POINT_ON_FACE.
 * A point located on a face.
 */
public final class StepPointOnFace implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity face;
    private final double uParameter;
    private final double vParameter;

    public StepPointOnFace(int id, String name, StepEntity face, double uParameter, double vParameter) {
        this.id = id;
        this.name = name;
        this.face = face;
        this.uParameter = uParameter;
        this.vParameter = vParameter;
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

    public double getUParameter() {
        return uParameter;
    }

    public double getVParameter() {
        return vParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPointOnFace that = (StepPointOnFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(face, that.face) && uParameter == that.uParameter && vParameter == that.vParameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, face, uParameter, vParameter);
    }

    @Override
    public String toString() {
        return "StepPointOnFace{" + "id=" + id + "name=" + name + "face=" + face + "uParameter=" + uParameter + "vParameter=" + vParameter + "}";
    }
}
