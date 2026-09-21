package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal OVER_RIDING_STYLED_ITEM.
 *
 * @param id step id
 * @param name style label
 * @param styles overriding style assignments
 * @param item styled target
 * @param overRiddenStyle referenced base styled item
 */
public final class StepOverRidingStyledItem extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final StepStyledItem overRiddenStyle;

    public StepOverRidingStyledItem(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, StepStyledItem overRiddenStyle) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.overRiddenStyle = overRiddenStyle;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepEntity getItem() {
        return item;
    }

    public StepStyledItem getOverRiddenStyle() {
        return overRiddenStyle;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepPresentationStyleAssignment> styles() { return styles; }
    public StepEntity item() { return item; }
    public StepStyledItem overRiddenStyle() { return overRiddenStyle; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        state.put("item", item);
        state.put("overRiddenStyle", overRiddenStyle);
        return state;
    }
}
