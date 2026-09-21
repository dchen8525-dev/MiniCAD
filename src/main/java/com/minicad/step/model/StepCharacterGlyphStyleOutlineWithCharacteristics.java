package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 * @param characteristics referenced fill area style
 */
public final class StepCharacterGlyphStyleOutlineWithCharacteristics extends AbstractStepEntity {
    private final StepCurveStyle outlineStyle;
    private final StepFillAreaStyle characteristics;

    public StepCharacterGlyphStyleOutlineWithCharacteristics(int id, StepCurveStyle outlineStyle, StepFillAreaStyle characteristics) {
        super(id, "");
        this.outlineStyle = outlineStyle;
        this.characteristics = characteristics;
    }

    public StepCurveStyle getOutlineStyle() {
        return outlineStyle;
    }

    public StepFillAreaStyle getCharacteristics() {
        return characteristics;
    }

    // Record-style accessors
    public StepCurveStyle outlineStyle() {
        return outlineStyle;
    }

    public StepFillAreaStyle characteristics() {
        return characteristics;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("outlineStyle", outlineStyle);
        state.put("characteristics", characteristics);
        return state;
    }
}
