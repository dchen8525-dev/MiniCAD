package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved NODE_SET.
 * A named set of finite element nodes.
 */
/**
 * Resolved NODE_SET.
 * A named set of finite element nodes.
 */
public final class StepNodeSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> nodes;

    public StepNodeSet(int id, String name, List<StepEntity> nodes) {
        this.id = id;
        this.name = name;
        this.nodes = nodes == null ? null : java.util.List.copyOf(nodes);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getNodes() {
        return nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNodeSet that = (StepNodeSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes);
    }

    @Override
    public String toString() {
        return "StepNodeSet{" + "id=" + id + "name=" + name + "nodes=" + nodes + "}";
    }
}
