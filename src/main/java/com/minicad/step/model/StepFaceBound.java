package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved FACE_BOUND or FACE_OUTER_BOUND.
 *
 * @param id step id
 * @param name step label
 * @param loop referenced loop
 * @param orientation orientation flag
 * @param outer whether this is the outer bound
 */
/**
 * Resolved FACE_BOUND or FACE_OUTER_BOUND.
 *
 * @param id step id
 * @param name step label
 * @param loop referenced loop
 * @param orientation orientation flag
 * @param outer whether this is the outer bound
 */
public final class StepFaceBound implements StepEntity {
    private final int id;
    private final String name;
    private final StepLoop loop;
    private final boolean orientation;
    private final boolean outer;

    public StepFaceBound(int id, String name, StepLoop loop, boolean orientation, boolean outer) {
        this.id = id;
        this.name = name;
        this.loop = loop;
        this.orientation = orientation;
        this.outer = outer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepLoop getLoop() {
        return loop;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public boolean isOuter() {
        return outer;
    }

    // Record-style accessors
    public StepLoop loop() { return getLoop(); }
    public boolean orientation() { return isOrientation(); }
    public boolean outer() { return isOuter(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFaceBound that = (StepFaceBound) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loop, that.loop) && orientation == that.orientation && outer == that.outer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loop, orientation, outer);
    }

    @Override
    public String toString() {
        return "StepFaceBound{" + "id=" + id + "name=" + name + "loop=" + loop + "orientation=" + orientation + "outer=" + outer + "}";
    }
}
