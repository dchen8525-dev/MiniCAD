package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved TEXT_FONT.
 */
/**
 * Resolved TEXT_FONT.
 */
public final class StepTextFont implements StepEntity {
    private final int id;
    private final String name;
    private final String fontName;

    public StepTextFont(int id, String name, String fontName) {
        this.id = id;
        this.name = name;
        this.fontName = fontName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFontName() {
        return fontName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextFont that = (StepTextFont) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(fontName, that.fontName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fontName);
    }

    @Override
    public String toString() {
        return "StepTextFont{" + "id=" + id + "name=" + name + "fontName=" + fontName + "}";
    }
}
