package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION entity model.
 * Base specification for data equivalence assessment.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description text
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceAssessmentSpecification extends AbstractStepEntity {
    private final String description; // OPTIONAL
    private final String entityName;

    public StepDataEquivalenceAssessmentSpecification(
        int id,
        String name,
        String description,
        String entityName) {
        super(id, name);
        this.description = description;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("entityName", entityName);
        return state;
    }
}
