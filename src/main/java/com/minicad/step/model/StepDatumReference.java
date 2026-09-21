package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DATUM_REFERENCE.
 * A datum reference used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param precedence datum precedence
 * @param referencedDatum referenced shape aspect
 */
public final class StepDatumReference extends AbstractStepEntity {
    private final int precedence;
    private final StepEntity referencedDatum;

    public StepDatumReference(int id, String name, int precedence, StepEntity referencedDatum) {
        super(id, name);
        this.precedence = precedence;
        this.referencedDatum = referencedDatum;
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
        state.put("precedence", precedence);
        state.put("referencedDatum", referencedDatum);
        return state;
    }
}
