package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DESIGNED_PART_DESIGN_VERSION.
 * A design version associated with a designed part.
 *
 * @param id STEP instance id
 * @param name part name
 * @param description part description
 * @param frameOfReference product context
 */
/**
 * Resolved DESIGNED_PART_DESIGN_VERSION.
 * A design version associated with a designed part.
 *
 * @param id STEP instance id
 * @param name part name
 * @param description part description
 * @param frameOfReference product context
 */
public final class StepDesignedPartDesignVersion implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity frameOfReference;

    public StepDesignedPartDesignVersion(int id, String name, String description, StepEntity frameOfReference) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frameOfReference = frameOfReference;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getFrameOfReference() {
        return frameOfReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDesignedPartDesignVersion that = (StepDesignedPartDesignVersion) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(frameOfReference, that.frameOfReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, frameOfReference);
    }

    @Override
    public String toString() {
        return "StepDesignedPartDesignVersion{" + "id=" + id + "name=" + name + "description=" + description + "frameOfReference=" + frameOfReference + "}";
    }
}
