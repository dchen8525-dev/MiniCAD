package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved EXTERNALLY_DEFINED_TILE_STYLE.
 */
/**
 * Resolved EXTERNALLY_DEFINED_TILE_STYLE.
 */
public final class StepExternallyDefinedTileStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity externalSource;

    public StepExternallyDefinedTileStyle(int id, String name, StepEntity externalSource) {
        this.id = id;
        this.name = name;
        this.externalSource = externalSource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getExternalSource() {
        return externalSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternallyDefinedTileStyle that = (StepExternallyDefinedTileStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(externalSource, that.externalSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, externalSource);
    }

    @Override
    public String toString() {
        return "StepExternallyDefinedTileStyle{" + "id=" + id + "name=" + name + "externalSource=" + externalSource + "}";
    }
}
