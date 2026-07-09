package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VALIDATION_DEFINITION.
 * A validation definition entity.
 *
 * @param id STEP instance id
 * @param name validation name
 * @param validationType validation variance type
 * @param validationCriteria validation variance criteria
 * @param validationRules validation variance rules
 * @param validationScope validation variance scope
 * @param validationStatus validation variance status
 */
/**
 * Resolved VALIDATION_DEFINITION.
 * A validation definition entity.
 *
 * @param id STEP instance id
 * @param name validation name
 * @param validationType validation variance type
 * @param validationCriteria validation variance criteria
 * @param validationRules validation variance rules
 * @param validationScope validation variance scope
 * @param validationStatus validation variance status
 */
public final class StepValidationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String validationType;
    private final List<String> validationCriteria;
    private final List<StepEntity> validationRules;
    private final String validationScope;
    private final String validationStatus;

    public StepValidationDefinition(int id, String name, String validationType, List<String> validationCriteria, List<StepEntity> validationRules, String validationScope, String validationStatus) {
        this.id = id;
        this.name = name;
        this.validationType = validationType;
        this.validationCriteria = validationCriteria == null ? null : java.util.List.copyOf(validationCriteria);
        this.validationRules = validationRules == null ? null : java.util.List.copyOf(validationRules);
        this.validationScope = validationScope;
        this.validationStatus = validationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValidationType() {
        return validationType;
    }

    public List<String> getValidationCriteria() {
        return validationCriteria;
    }

    public List<StepEntity> getValidationRules() {
        return validationRules;
    }

    public String getValidationScope() {
        return validationScope;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValidationDefinition that = (StepValidationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(validationType, that.validationType) && Objects.equals(validationCriteria, that.validationCriteria) && Objects.equals(validationRules, that.validationRules) && Objects.equals(validationScope, that.validationScope) && Objects.equals(validationStatus, that.validationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, validationType, validationCriteria, validationRules, validationScope, validationStatus);
    }

    @Override
    public String toString() {
        return "StepValidationDefinition{" + "id=" + id + "name=" + name + "validationType=" + validationType + "validationCriteria=" + validationCriteria + "validationRules=" + validationRules + "validationScope=" + validationScope + "validationStatus=" + validationStatus + "}";
    }
}