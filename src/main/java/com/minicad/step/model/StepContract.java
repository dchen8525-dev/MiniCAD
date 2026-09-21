package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CONTRACT metadata.
 *
 * @param id STEP instance id
 * @param name contract name
 * @param purpose contract purpose
 * @param kind contract type
 */
public final class StepContract extends AbstractStepEntity {
    private final String purpose;
    private final StepContractType kind;

    public StepContract(int id, String name, String purpose, StepContractType kind) {
        super(id, name);
        this.purpose = purpose;
        this.kind = kind;
    }

    public String getPurpose() {
        return purpose;
    }

    public StepContractType getKind() {
        return kind;
    }

    // Record-style accessor
    public StepContractType kind() {
        return kind;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("purpose", purpose);
        state.put("kind", kind);
        return state;
    }
}
