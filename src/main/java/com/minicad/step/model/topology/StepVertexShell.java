package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved VERTEX_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param extent defining vertex loop
 */
/**
 * Resolved VERTEX_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param extent defining vertex loop
 */
public final class StepVertexShell implements StepEntity {
    private final int id;
    private final String name;
    private final StepVertexLoop extent;

    public StepVertexShell(int id, String name, StepVertexLoop extent) {
        this.id = id;
        this.name = name;
        this.extent = extent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepVertexLoop getExtent() {
        return extent;
    }

    // Record-style accessor
    public StepVertexLoop extent() {
        return extent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVertexShell that = (StepVertexShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(extent, that.extent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, extent);
    }

    @Override
    public String toString() {
        return "StepVertexShell{" + "id=" + id + "name=" + name + "extent=" + extent + "}";
    }
}
