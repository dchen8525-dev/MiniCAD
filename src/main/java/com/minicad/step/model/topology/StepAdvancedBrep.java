package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ADVANCED_BREP.
 * An advanced boundary representation with voids.
 *
 * @param id STEP instance id
 * @param name B-rep name
 * @param outer outer shell
 * @param voids list of void shells
 */
/**
 * Resolved ADVANCED_BREP.
 * An advanced boundary representation with voids.
 *
 * @param id STEP instance id
 * @param name B-rep name
 * @param outer outer shell
 * @param voids list of void shells
 */
public final class StepAdvancedBrep implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity outer;
    private final List<StepEntity> voids;

    public StepAdvancedBrep(int id, String name, StepEntity outer, List<StepEntity> voids) {
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

    // Java Bean style alias for outer
    public StepEntity isOuter() {
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
        StepAdvancedBrep that = (StepAdvancedBrep) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outer, that.outer) && Objects.equals(voids, that.voids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outer, voids);
    }

    @Override
    public String toString() {
        return "StepAdvancedBrep{" + "id=" + id + "name=" + name + "outer=" + outer + "voids=" + voids + "}";
    }
}
