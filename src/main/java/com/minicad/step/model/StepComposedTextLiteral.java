package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSED_TEXT_LITERAL.
 */
/**
 * Resolved COMPOSED_TEXT_LITERAL.
 */
public final class StepComposedTextLiteral implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> components;

    public StepComposedTextLiteral(int id, String name, List<StepEntity> components) {
        this.id = id;
        this.name = name;
        this.components = components == null ? null : java.util.List.copyOf(components);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComposedTextLiteral that = (StepComposedTextLiteral) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, components);
    }

    @Override
    public String toString() {
        return "StepComposedTextLiteral{" + "id=" + id + "name=" + name + "components=" + components + "}";
    }
}
