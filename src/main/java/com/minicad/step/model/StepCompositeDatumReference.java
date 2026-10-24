package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "datums", datums, "compositeType", compositeType);
    }
}
