package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved TEXT_FILE_REPRESENTATION.
 */
/**
 * Resolved TEXT_FILE_REPRESENTATION.
 */
public final class StepTextFileRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final String textContent;

    public StepTextFileRepresentation(int id, String name, String textContent) {
        this.id = id;
        this.name = name;
        this.textContent = textContent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextFileRepresentation that = (StepTextFileRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(textContent, that.textContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, textContent);
    }

    @Override
    public String toString() {
        return "StepTextFileRepresentation{" + "id=" + id + "name=" + name + "textContent=" + textContent + "}";
    }
}
