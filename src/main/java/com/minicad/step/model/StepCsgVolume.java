package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CSG_VOLUME.
 * A CSG solid represented as a volume.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param treeRoot root of the CSG tree
 */
/**
 * Resolved CSG_VOLUME.
 * A CSG solid represented as a volume.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param treeRoot root of the CSG tree
 */
public final class StepCsgVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity treeRoot;

    public StepCsgVolume(int id, String name, StepEntity treeRoot) {
        this.id = id;
        this.name = name;
        this.treeRoot = treeRoot;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTreeRoot() {
        return treeRoot;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity treeRoot() { return getTreeRoot(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCsgVolume that = (StepCsgVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(treeRoot, that.treeRoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, treeRoot);
    }

    @Override
    public String toString() {
        return "StepCsgVolume{" + "id=" + id + "name=" + name + "treeRoot=" + treeRoot + "}";
    }
}
