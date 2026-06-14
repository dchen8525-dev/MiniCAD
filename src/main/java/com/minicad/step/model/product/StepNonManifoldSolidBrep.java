package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved NON_MANIFOLD_SOLID_BREP.
 * A B-rep solid whose boundary may be a non-manifold shell.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param outer the surface (open or closed shell) forming the boundary
 */
/**
 * Resolved NON_MANIFOLD_SOLID_BREP.
 * A B-rep solid whose boundary may be a non-manifold shell.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param outer the surface (open or closed shell) forming the boundary
 */
public final class StepNonManifoldSolidBrep implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;

    public StepNonManifoldSolidBrep(int id, String name, StepEntity outer) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNonManifoldSolidBrep that = (StepNonManifoldSolidBrep) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outer, that.outer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer);
    }

    @Override
    public String toString() {
        return "StepNonManifoldSolidBrep{" + "id=" + id + "name=" + name + "outer=" + outer + "}";
    }
}
