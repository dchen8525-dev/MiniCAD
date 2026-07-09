package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal SYMBOL_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styleOfSymbol symbol style payload
 */
/**
 * Minimal SYMBOL_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styleOfSymbol symbol style payload
 */
public final class StepSymbolStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity styleOfSymbol;

    public StepSymbolStyle(int id, String name, StepEntity styleOfSymbol) {
        this.id = id;
        this.name = name;
        this.styleOfSymbol = styleOfSymbol;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStyleOfSymbol() {
        return styleOfSymbol;
    }

    // Record-style accessor
    public StepEntity styleOfSymbol() {
        return styleOfSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSymbolStyle that = (StepSymbolStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styleOfSymbol, that.styleOfSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styleOfSymbol);
    }

    @Override
    public String toString() {
        return "StepSymbolStyle{" + "id=" + id + "name=" + name + "styleOfSymbol=" + styleOfSymbol + "}";
    }
}
