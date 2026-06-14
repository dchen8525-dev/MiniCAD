package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal annotation text occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param text annotation text
 * @param position anchor point
 */
/**
 * Minimal annotation text occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param text annotation text
 * @param position anchor point
 */
public final class StepAnnotationTextOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final String text;
    private final StepEntity position;

    public StepAnnotationTextOccurrence(int id, String name, String text, StepEntity position) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public StepEntity getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationTextOccurrence that = (StepAnnotationTextOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(text, that.text) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, position);
    }

    @Override
    public String toString() {
        return "StepAnnotationTextOccurrence{" + "id=" + id + "name=" + name + "text=" + text + "position=" + position + "}";
    }
}
