package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal TEXT_STYLE_FOR_DEFINED_FONT.
 *
 * @param id STEP instance id
 * @param textColour referenced text colour
 */
public final class StepTextStyleForDefinedFont extends AbstractStepEntity {
    private final StepEntity textColour;

    public StepTextStyleForDefinedFont(int id, StepEntity textColour) {
        super(id, "");
        this.textColour = textColour;
    }

    public StepEntity getTextColour() {
        return textColour;
    }

    // Record-style accessor
    public StepEntity textColour() {
        return textColour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("textColour", textColour);
        return state;
    }
}
