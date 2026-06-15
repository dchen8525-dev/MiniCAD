package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DEGENERATE_CURVE.
 * A curve that has degenerated to a point or line.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve the original curve before degeneration
 */
/**
 * Resolved DEGENERATE_CURVE.
 * A curve that has degenerated to a point or line.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve the original curve before degeneration
 */
public final class StepDegenerateCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisCurve;

    public StepDegenerateCurve(int id, String name, StepEntity basisCurve) {
        this.id = id;
        this.name = name;
        this.basisCurve = basisCurve;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisCurve() {
        return basisCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisCurve() { return getBasisCurve(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDegenerateCurve that = (StepDegenerateCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisCurve, that.basisCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisCurve);
    }

    @Override
    public String toString() {
        return "StepDegenerateCurve{" + "id=" + id + "name=" + name + "basisCurve=" + basisCurve + "}";
    }
}
