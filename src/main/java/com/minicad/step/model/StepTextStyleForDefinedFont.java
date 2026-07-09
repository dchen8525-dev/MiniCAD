package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TEXT_STYLE_FOR_DEFINED_FONT.
 *
 * @param id STEP instance id
 * @param textColour referenced text colour
 */
/**
 * Minimal TEXT_STYLE_FOR_DEFINED_FONT.
 *
 * @param id STEP instance id
 * @param textColour referenced text colour
 */
public final class StepTextStyleForDefinedFont implements StepEntity {
    private final int id;
    private final StepEntity textColour;

    public StepTextStyleForDefinedFont(int id, StepEntity textColour) {
        this.id = id;
        this.textColour = textColour;
    }

    public int getId() {
        return id;
    }

    public StepEntity getTextColour() {
        return textColour;
    }

    public String getName() {
        return "";
    }

    // Record-style accessor
    public StepEntity textColour() {
        return textColour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyleForDefinedFont that = (StepTextStyleForDefinedFont) o;
        return id == that.id && Objects.equals(textColour, that.textColour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textColour);
    }

    @Override
    public String toString() {
        return "StepTextStyleForDefinedFont{" + "id=" + id + "textColour=" + textColour + "}";
    }
}
