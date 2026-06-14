package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * A3M_EQUIVALENCE_ACCURACY_ASSOCIATION entity model.
 * Represents an association between accuracy and equivalence target in A3M validation.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description text
 * @param specificAccuracy reference to shape measurement accuracy
 * @param equivalenceTarget reference to accuracy associated target select
 */
public final class StepA3mEquivalenceAccuracyAssociation implements StepEntity {
    private final int id;
    private final String name;
    private final String description; // OPTIONAL
    private final Object specificAccuracy; // shape_measurement_accuracy reference
    private final Object equivalenceTarget; // accuracy_associated_target_select reference

    public StepA3mEquivalenceAccuracyAssociation(
        int id,
        String name,
        String description,
        Object specificAccuracy,
        Object equivalenceTarget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.specificAccuracy = specificAccuracy;
        this.equivalenceTarget = equivalenceTarget;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Object getSpecificAccuracy() {
        return specificAccuracy;
    }

    public Object getEquivalenceTarget() {
        return equivalenceTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepA3mEquivalenceAccuracyAssociation that = (StepA3mEquivalenceAccuracyAssociation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepA3mEquivalenceAccuracyAssociation{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", specificAccuracy=" + specificAccuracy +
            ", equivalenceTarget=" + equivalenceTarget +
            '}';
    }
}