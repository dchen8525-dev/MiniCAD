package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DATUM_REFERENCE_ELEMENT.
 * A datum reference element used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name element name
 * @param description element description
 * @param ofShape product definition shape
 * @param compartments datum reference compartments
 */
public final class StepDatumReferenceElement extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;
    private final List<StepEntity> compartments;

    public StepDatumReferenceElement(int id, String name, String description, StepEntity ofShape, List<StepEntity> compartments) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.compartments = compartments == null ? null : java.util.List.copyOf(compartments);
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    public List<StepEntity> getCompartments() {
        return compartments;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        state.put("compartments", compartments);
        return state;
    }
}
