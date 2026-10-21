package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP entity model.
 * Represents relationship between inspected model and equivalence result.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param inspectedModel reference to target annotated 3d model select
 * @param equivalenceResult reference to a3m equivalence inspection result representation
 */
public final class StepA3mInspectedModelAndInspectionResultRelationship extends AbstractStepEntity {
    private final Object inspectedModel; // target_annotated_3d_model_select reference
    private final Object equivalenceResult; // a3m_equivalence_inspection_result_representation reference

    public StepA3mInspectedModelAndInspectionResultRelationship(
        int id,
        String name,
        Object inspectedModel,
        Object equivalenceResult) {
        super(id, name);
        this.inspectedModel = inspectedModel;
        this.equivalenceResult = equivalenceResult;
    }

    public Object getInspectedModel() {
        return inspectedModel;
    }

    public Object getEquivalenceResult() {
        return equivalenceResult;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("inspectedModel", inspectedModel);
        state.put("equivalenceResult", equivalenceResult);
        return state;
    }
}
