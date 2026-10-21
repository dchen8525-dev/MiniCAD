package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A3MA_EQUIVALENCE_INSPECTION_RESULT entity model.
 * SUBTYPE OF data_equivalence_inspection_result with criterion_inspected = a3m_equivalence_criterion_for_assembly.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description
 * @param result result value
 * @param criterionInspected reference to a3m_equivalence_criterion_for_assembly
 */
public final class StepA3maEquivalenceInspectionResult extends AbstractStepEntity {
    private final String description; // OPTIONAL from supertype
    private final Object result; // from supertype data_equivalence_inspection_result
    private final Object criterionInspected; // a3m_equivalence_criterion_for_assembly reference

    public StepA3maEquivalenceInspectionResult(
        int id,
        String name,
        String description,
        Object result,
        Object criterionInspected) {
        super(id, name);
        this.description = description;
        this.result = result;
        this.criterionInspected = criterionInspected;
    }

    public String getDescription() {
        return description;
    }

    public Object getResult() {
        return result;
    }

    public Object getCriterionInspected() {
        return criterionInspected;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("result", result);
        state.put("criterionInspected", criterionInspected);
        return state;
    }
}
