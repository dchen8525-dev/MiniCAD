package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CONTRACT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public final class StepContractType extends AbstractStepEntity {
    private final String description;

    public StepContractType(int id, String description) {
        super(id, "");
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return description != null ? description : "";
    }

    // Record-style accessor - name from description
    public String name() {
        return description;
    }

    public String kind() {
        return description;
    }

    public String description() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("description", description);
        return state;
    }
}
