package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal resolved DEGENERATE_PCURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param basisSurface basis surface
 * @param referenceToCurve referenced definitional representation
 */
/**
 * Minimal resolved DEGENERATE_PCURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param basisSurface basis surface
 * @param referenceToCurve referenced definitional representation
 */
public final class StepDegeneratePcurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final StepRepresentation referenceToCurve;

    public StepDegeneratePcurve(int id, String name, StepEntity basisSurface, StepRepresentation referenceToCurve) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.referenceToCurve = referenceToCurve;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public StepRepresentation getReferenceToCurve() {
        return referenceToCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public StepRepresentation referenceToCurve() { return getReferenceToCurve(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDegeneratePcurve that = (StepDegeneratePcurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && Objects.equals(referenceToCurve, that.referenceToCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, referenceToCurve);
    }

    @Override
    public String toString() {
        return "StepDegeneratePcurve{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "referenceToCurve=" + referenceToCurve + "}";
    }
}
