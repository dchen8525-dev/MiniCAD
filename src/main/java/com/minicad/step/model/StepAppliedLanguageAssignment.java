package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 * @param items assigned target items
 */
public final class StepAppliedLanguageAssignment implements StepEntity {
    private final int id;
    private final StepLanguage assignedLanguage;
    private final List<StepEntity> items;

    public StepAppliedLanguageAssignment(int id, StepLanguage assignedLanguage, List<StepEntity> items) {
        this.id = id;
        this.assignedLanguage = assignedLanguage;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public StepLanguage getAssignedLanguage() {
        return assignedLanguage;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getName() {
        return "";
    }

    // Record-style accessors
    public StepLanguage assignedLanguage() {
        return assignedLanguage;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedLanguageAssignment that = (StepAppliedLanguageAssignment) o;
        return id == that.id && Objects.equals(assignedLanguage, that.assignedLanguage) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedLanguage, items);
    }

    @Override
    public String toString() {
        return "StepAppliedLanguageAssignment{" + "id=" + id + "assignedLanguage=" + assignedLanguage + "items=" + items + "}";
    }
}
