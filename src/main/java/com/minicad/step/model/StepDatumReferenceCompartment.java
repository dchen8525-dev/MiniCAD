package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DATUM_REFERENCE_COMPARTMENT.
 * A compartment of a datum reference in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name compartment name
 * @param description compartment description
 * @param ofShape product definition shape
 * @param precedence datum precedence
 * @param referencedDatum referenced datum
 */
public final class StepDatumReferenceCompartment extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;
    private final int precedence;
    private final StepEntity referencedDatum;

    public StepDatumReferenceCompartment(int id, String name, String description, StepEntity ofShape, int precedence, StepEntity referencedDatum) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.precedence = precedence;
        this.referencedDatum = referencedDatum;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    public int getPrecedence() {
        return precedence;
    }

    public StepEntity getReferencedDatum() {
        return referencedDatum;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        state.put("precedence", precedence);
        state.put("referencedDatum", referencedDatum);
        return state;
    }
}
