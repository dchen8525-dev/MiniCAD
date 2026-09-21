package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATUM_SYSTEM_REFERENCE.
 * A datum system reference entity.
 *
 * @param id STEP instance id
 * @param name datum system name
 * @param datumSystem the datum system being referenced
 * @param precedenceLevel precedence level in the datum system
 */
public final class StepDatumSystemReference extends AbstractStepEntity {
    private final StepEntity datumSystem;
    private final int precedenceLevel;

    public StepDatumSystemReference(int id, String name, StepEntity datumSystem, int precedenceLevel) {
        super(id, name);
        this.datumSystem = datumSystem;
        this.precedenceLevel = precedenceLevel;
    }

    public StepEntity getDatumSystem() {
        return datumSystem;
    }

    public int getPrecedenceLevel() {
        return precedenceLevel;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("datumSystem", datumSystem);
        state.put("precedenceLevel", precedenceLevel);
        return state;
    }
}
