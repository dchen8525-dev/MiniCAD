package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

/**
 * FACETED_BREP_AND_BREP_WITH_VOIDS (single-T alias variant of FACETTED_BREP_AND_BREP_WITH_VOIDS).
 *
 * <p>A faceted boundary representation with an outer closed shell and
 * optional void (inner) closed shells.</p>
 *
 * @param id   STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 * @param voids referenced void closed shells
 */
public final class StepFacetedBrepAndBrepWithVoids implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepFacetedBrepAndBrepWithVoids(int id, String name, StepEntity outer, List<StepEntity> voids) {
        this.id = id;
        this.name = name;
        this.outer = outer;
        this.voids = voids == null ? null : List.copyOf(voids);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOuter() {
        return outer;
    }

    public List<StepEntity> getVoids() {
        return voids;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity outer() { return getOuter(); }
    public List<StepEntity> voids() { return getVoids(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFacetedBrepAndBrepWithVoids that = (StepFacetedBrepAndBrepWithVoids) o;
        return id == that.id && Objects.equals(name, that.name)
                && Objects.equals(outer, that.outer) && Objects.equals(voids, that.voids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer, voids);
    }

    @Override
    public String toString() {
        return "StepFacetedBrepAndBrepWithVoids{id=" + id + ", name=" + name + ", outer=" + outer + ", voids=" + voids + "}";
    }
}
