package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOSITE_DATUM_REFERENCE.
 * A composite datum reference entity.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param datums list of datum entities
 * @param compositeType composite type (common, simultaneous, etc.)
 */
public final class StepCompositeDatumReference extends AbstractStepEntity {
    private final List<StepEntity> datums;
    private final String compositeType;

    public StepCompositeDatumReference(int id, String name, List<StepEntity> datums, String compositeType) {
        super(id, name);
        this.datums = datums == null ? null : java.util.List.copyOf(datums);
        this.compositeType = compositeType;
    }

    public List<StepEntity> getDatums() {
        return datums;
    }

    public String getCompositeType() {
        return compositeType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("datums", datums);
        state.put("compositeType", compositeType);
        return state;
    }
}
