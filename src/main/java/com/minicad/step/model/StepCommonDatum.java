package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMMON_DATUM.
 * A datum established from two or more datum features.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param ofShape product definition shape
 * @param constituentDatums constituent datum references
 */
public final class StepCommonDatum extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;
    private final List<StepEntity> constituentDatums;

    public StepCommonDatum(int id, String name, String description, StepEntity ofShape, List<StepEntity> constituentDatums) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.constituentDatums = constituentDatums == null ? null : java.util.List.copyOf(constituentDatums);
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    public List<StepEntity> getConstituentDatums() {
        return constituentDatums;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        state.put("constituentDatums", constituentDatums);
        return state;
    }
}
