package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal product definition root.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name product name
 * @param description optional description
 * @param frameOfReference product contexts
 */
/**
 * Minimal product definition root.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name product name
 * @param description optional description
 * @param frameOfReference product contexts
 */
public final class StepProduct implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final List<StepProductContext> frameOfReference;

    public StepProduct(int id, String identifier, String name, String description, List<StepProductContext> frameOfReference) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.frameOfReference = frameOfReference == null ? null : java.util.List.copyOf(frameOfReference);
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<StepProductContext> getFrameOfReference() {
        return frameOfReference;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String identifier() { return identifier; }
    public String description() { return description; }
    public List<StepProductContext> frameOfReference() { return frameOfReference; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProduct that = (StepProduct) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(frameOfReference, that.frameOfReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, frameOfReference);
    }

    @Override
    public String toString() {
        return "StepProduct{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "frameOfReference=" + frameOfReference + "}";
    }
}
