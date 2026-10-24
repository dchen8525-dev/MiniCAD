package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * A3M_EQUIVALENCE_CRITERION entity model (ABSTRACT SUPERTYPE).
 * Base criterion for A3M equivalence validation.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param assessmentSpecification reference to assessment specification select
 * @param comparingElementTypes list of element type names
 * @param comparedElementTypes list of element type names
 * @param measuredDataType measured data type name
 * @param detectedDifferenceTypes list of detected difference type names (optional, can be empty)
 * @param accuracyTypes list of accuracy type names (optional, can be empty)
 */
public final class StepA3mEquivalenceCriterion extends AbstractStepEntity {
    private final Object assessmentSpecification; // a3m_equivalence_assessment_specification_select reference
    private final List<String> comparingElementTypes; // LIST [1:?] OF a3m_element_type_name
    private final List<String> comparedElementTypes; // LIST [1:?] OF a3m_element_type_name
    private final Object measuredDataType; // a3m_measured_data_type_name
    private final List<String> detectedDifferenceTypes; // LIST [0:?] OF a3m_detected_difference_type_name
    private final List<String> accuracyTypes; // LIST [0:?] OF a3m_accuracy_type_name

    public StepA3mEquivalenceCriterion(
        int id,
        String name,
        Object assessmentSpecification,
        List<String> comparingElementTypes,
        List<String> comparedElementTypes,
        Object measuredDataType,
        List<String> detectedDifferenceTypes,
        List<String> accuracyTypes) {
        super(id, name);
        this.assessmentSpecification = assessmentSpecification;
        this.comparingElementTypes = comparingElementTypes;
        this.comparedElementTypes = comparedElementTypes;
        this.measuredDataType = measuredDataType;
        this.detectedDifferenceTypes = detectedDifferenceTypes;
        this.accuracyTypes = accuracyTypes;
    }

    public Object getAssessmentSpecification() {
        return assessmentSpecification;
    }

    public List<String> getComparingElementTypes() {
        return comparingElementTypes;
    }

    public List<String> getComparedElementTypes() {
        return comparedElementTypes;
    }

    public Object getMeasuredDataType() {
        return measuredDataType;
    }

    public List<String> getDetectedDifferenceTypes() {
        return detectedDifferenceTypes;
    }

    public List<String> getAccuracyTypes() {
        return accuracyTypes;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "assessmentSpecification", assessmentSpecification,
                "comparingElementTypes", comparingElementTypes, "comparedElementTypes", comparedElementTypes,
                "measuredDataType", measuredDataType, "detectedDifferenceTypes", detectedDifferenceTypes,
                "accuracyTypes", accuracyTypes);
    }
}
