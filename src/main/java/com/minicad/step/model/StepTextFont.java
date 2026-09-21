package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TEXT_FONT.
 */
public final class StepTextFont extends AbstractStepEntity {
    private final String fontName;

    public StepTextFont(int id, String name, String fontName) {
        super(id, name);
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("fontName", fontName);
        return state;
    }
}
