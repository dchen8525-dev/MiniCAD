package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP entity model.
 * Represents relationship between inspected model and equivalence result.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param inspectedModel reference to target annotated 3d model select
 * @param equivalenceResult reference to a3m equivalence inspection result representation
 */
public final class StepA3mInspectedModelAndInspectionResultRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final Object inspectedModel; // target_annotated_3d_model_select reference
    private final Object equivalenceResult; // a3m_equivalence_inspection_result_representation reference

    public StepA3mInspectedModelAndInspectionResultRelationship(
        int id,
        String name,
        Object inspectedModel,
        Object equivalenceResult) {
        this.id = id;
        this.name = name;
        this.inspectedModel = inspectedModel;
        this.equivalenceResult = equivalenceResult;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getInspectedModel() {
        return inspectedModel;
    }

    public Object getEquivalenceResult() {
        return equivalenceResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepA3mInspectedModelAndInspectionResultRelationship that = (StepA3mInspectedModelAndInspectionResultRelationship) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepA3mInspectedModelAndInspectionResultRelationship{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", inspectedModel=" + inspectedModel +
            ", equivalenceResult=" + equivalenceResult +
            '}';
    }
}