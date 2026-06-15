package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 */
/**
 * Minimal LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 */
public final class StepLanguageAssignment implements StepEntity {
    private final int id;
    private final StepLanguage assignedLanguage;

    public StepLanguageAssignment(int id, StepLanguage assignedLanguage) {
        this.id = id;
        this.assignedLanguage = assignedLanguage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepLanguage getAssignedLanguage() {
        return assignedLanguage;
    }

    // Record-style accessor
    public StepLanguage assignedLanguage() {
        return assignedLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLanguageAssignment that = (StepLanguageAssignment) o;
        return id == that.id && Objects.equals(assignedLanguage, that.assignedLanguage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedLanguage);
    }

    @Override
    public String toString() {
        return "StepLanguageAssignment{" + "id=" + id + "assignedLanguage=" + assignedLanguage + "}";
    }
}
