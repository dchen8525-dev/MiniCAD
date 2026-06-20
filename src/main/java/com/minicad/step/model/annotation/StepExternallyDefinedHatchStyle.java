package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved EXTERNALLY_DEFINED_HATCH_STYLE.
 * A hatch style defined by an external source.
 */
/**
 * Resolved EXTERNALLY_DEFINED_HATCH_STYLE.
 * A hatch style defined by an external source.
 */
public final class StepExternallyDefinedHatchStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity externalSource;

    public StepExternallyDefinedHatchStyle(int id, String name, StepEntity externalSource) {
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

    public String entityName() {
        return "EXTERNALLY_DEFINED_HATCH_STYLE";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternallyDefinedHatchStyle that = (StepExternallyDefinedHatchStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(externalSource, that.externalSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, externalSource);
    }

    @Override
    public String toString() {
        return "StepExternallyDefinedHatchStyle{" + "id=" + id + "name=" + name + "externalSource=" + externalSource + "}";
    }
}
