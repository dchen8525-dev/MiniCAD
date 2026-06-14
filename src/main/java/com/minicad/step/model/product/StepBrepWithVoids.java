package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal BREP_WITH_VOIDS.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 * @param voids referenced void closed shells
 */
/**
 * Minimal BREP_WITH_VOIDS.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 * @param voids referenced void closed shells
 */
public final class StepBrepWithVoids implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepBrepWithVoids(int id, String name, StepEntity outer, List<StepEntity> voids) {
        this.id = id;
        this.name = name;
        this.outer = outer;
        this.voids = voids == null ? null : java.util.List.copyOf(voids);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBrepWithVoids that = (StepBrepWithVoids) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outer, that.outer) && Objects.equals(voids, that.voids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer, voids);
    }

    @Override
    public String toString() {
        return "StepBrepWithVoids{" + "id=" + id + "name=" + name + "outer=" + outer + "voids=" + voids + "}";
    }
}
