package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal LANGUAGE metadata.
 *
 * @param id STEP instance id
 * @param name language name
 */
/**
 * Minimal LANGUAGE metadata.
 *
 * @param id STEP instance id
 * @param name language name
 */
public final class StepLanguage implements StepEntity {
    private final int id;
    private final String name;

    public StepLanguage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLanguage that = (StepLanguage) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepLanguage{" + "id=" + id + "name=" + name + "}";
    }
}
