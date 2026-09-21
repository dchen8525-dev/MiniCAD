package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param styles presentation style assignments
 * @param item referenced supported annotation content or occurrence
 * @param annotatedCurve referenced annotation curve occurrence
 */
public final class StepTerminatorSymbol extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final StepEntity annotatedCurve;

    public StepTerminatorSymbol(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, StepEntity annotatedCurve) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.annotatedCurve = annotatedCurve;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepEntity getItem() {
        return item;
    }

    public StepEntity getAnnotatedCurve() {
        return annotatedCurve;
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

    public StepEntity annotatedCurve() {
        return annotatedCurve;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        state.put("item", item);
        state.put("annotatedCurve", annotatedCurve);
        return state;
    }
}
