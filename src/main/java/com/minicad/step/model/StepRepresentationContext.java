package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal semantic representation context.
 *
 * @param id STEP instance id
 * @param contextIdentifier context identifier
 * @param contextType context type
 */
/**
 * Minimal semantic representation context.
 *
 * @param id STEP instance id
 * @param contextIdentifier context identifier
 * @param contextType context type
 */
public final class StepRepresentationContext implements StepEntity {
    private final int id;
    private final String contextIdentifier;
    private final String contextType;

    public StepRepresentationContext(int id, String contextIdentifier, String contextType) {
        this.id = id;
        this.contextIdentifier = contextIdentifier;
        this.contextType = contextType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public String getContextIdentifier() {
        return contextIdentifier;
    }

    public String getContextType() {
        return contextType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationContext that = (StepRepresentationContext) o;
        return id == that.id && Objects.equals(contextIdentifier, that.contextIdentifier) && Objects.equals(contextType, that.contextType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contextIdentifier, contextType);
    }

    @Override
    public String toString() {
        return "StepRepresentationContext{" + "id=" + id + "contextIdentifier=" + contextIdentifier + "contextType=" + contextType + "}";
    }
}
