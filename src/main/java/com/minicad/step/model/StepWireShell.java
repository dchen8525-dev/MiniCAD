package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WIRE_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param loops defining loops
 */
/**
 * Resolved WIRE_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param loops defining loops
 */
public final class StepWireShell implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepLoop> loops;

    public StepWireShell(int id, String name, List<StepLoop> loops) {
        this.id = id;
        this.name = name;
        this.loops = loops == null ? null : java.util.List.copyOf(loops);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepLoop> getLoops() {
        return loops;
    }

    // Record-style accessor
    public List<StepLoop> loops() {
        return loops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWireShell that = (StepWireShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loops, that.loops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loops);
    }

    @Override
    public String toString() {
        return "StepWireShell{" + "id=" + id + "name=" + name + "loops=" + loops + "}";
    }
}
