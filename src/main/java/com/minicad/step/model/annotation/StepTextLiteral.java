package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TEXT_LITERAL.
 * A single text string at a specific placement.
 *
 * @param id STEP instance id
 * @param name text name
 * @param literal the text content
 * @param path text path placement
 */
/**
 * Resolved TEXT_LITERAL.
 * A single text string at a specific placement.
 *
 * @param id STEP instance id
 * @param name text name
 * @param literal the text content
 * @param path text path placement
 */
public final class StepTextLiteral implements StepEntity {
    private final int id;
    private final String name;
    private final String literal;
    private final StepEntity path;

    public StepTextLiteral(int id, String name, String literal, StepEntity path) {
        this.id = id;
        this.name = name;
        this.literal = literal;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLiteral() {
        return literal;
    }

    public StepEntity getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextLiteral that = (StepTextLiteral) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(literal, that.literal) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, literal, path);
    }

    @Override
    public String toString() {
        return "StepTextLiteral{" + "id=" + id + "name=" + name + "literal=" + literal + "path=" + path + "}";
    }
}
