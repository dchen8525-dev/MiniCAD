package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAnnotationPlaceholderOccurrence extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final String role;
    private final double lineSpacing;

    public StepAnnotationPlaceholderOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, String role, double lineSpacing) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.role = role;
        this.lineSpacing = lineSpacing;
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
        return getName();
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "styles", styles, "item", item, "role", role,
                "lineSpacing", lineSpacing);
    }
}
