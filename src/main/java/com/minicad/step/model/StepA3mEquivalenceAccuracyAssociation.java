package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepA3mEquivalenceAccuracyAssociation extends AbstractStepEntity {
    private final String description; // OPTIONAL
    private final Object specificAccuracy; // shape_measurement_accuracy reference
    private final Object equivalenceTarget; // accuracy_associated_target_select reference

    public StepA3mEquivalenceAccuracyAssociation(
        int id,
        String name,
        String description,
        Object specificAccuracy,
        Object equivalenceTarget) {
        super(id, name);
        this.description = description;
        this.specificAccuracy = specificAccuracy;
        this.equivalenceTarget = equivalenceTarget;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("specificAccuracy", specificAccuracy);
        state.put("equivalenceTarget", equivalenceTarget);
        return state;
    }
}
