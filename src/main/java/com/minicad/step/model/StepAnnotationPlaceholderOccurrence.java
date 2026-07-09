package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal annotation placeholder occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced point-like carrier
 * @param role placeholder role enum
 * @param lineSpacing positive line spacing
 */
/**
 * Minimal annotation placeholder occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced point-like carrier
 * @param role placeholder role enum
 * @param lineSpacing positive line spacing
 */
public final class StepAnnotationPlaceholderOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final String role;
    private final double lineSpacing;

    public StepAnnotationPlaceholderOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, String role, double lineSpacing) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.role = role;
        this.lineSpacing = lineSpacing;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepEntity getItem() {
        return item;
    }

    public String getRole() {
        return role;
    }

    public double getLineSpacing() {
        return lineSpacing;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepEntity item() {
        return item;
    }

    public String role() {
        return role;
    }

    public double lineSpacing() {
        return lineSpacing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationPlaceholderOccurrence that = (StepAnnotationPlaceholderOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item) && Objects.equals(role, that.role) && lineSpacing == that.lineSpacing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item, role, lineSpacing);
    }

    @Override
    public String toString() {
        return "StepAnnotationPlaceholderOccurrence{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "role=" + role + "lineSpacing=" + lineSpacing + "}";
    }
}
