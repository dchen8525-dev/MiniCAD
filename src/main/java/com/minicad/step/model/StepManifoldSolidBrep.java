package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MANIFOLD_SOLID_BREP.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 */
/**
 * Resolved MANIFOLD_SOLID_BREP.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 */
public final class StepManifoldSolidBrep implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;

    public StepManifoldSolidBrep(int id, String name, StepEntity outer) {
        this.id = id;
        this.name = name;
        this.outer = outer;
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

    // Java Bean style alias for outer
    public StepEntity isOuter() {
        return outer;
    }

    // Record-style accessors for compatibility
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity outer() { return getOuter(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepManifoldSolidBrep that = (StepManifoldSolidBrep) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outer, that.outer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer);
    }

    @Override
    public String toString() {
        return "StepManifoldSolidBrep{" + "id=" + id + "name=" + name + "outer=" + outer + "}";
    }
}
