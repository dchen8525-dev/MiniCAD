package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MODIFY_FEATURE.
 * A modify feature entity for feature modifications.
 *
 * @param id STEP instance id
 * @param name modification name
 * @param originalFeature original feature being modified
 * @param modificationType modification type classification
 * @param modificationParameters modification parameters
 * @param modifiedGeometry modified geometry result
 */
public final class StepModifyFeature extends AbstractStepEntity {
    private final StepEntity originalFeature;
    private final String modificationType;
    private final List<StepEntity> modificationParameters;
    private final StepEntity modifiedGeometry;

    public StepModifyFeature(int id, String name, StepEntity originalFeature, String modificationType, List<StepEntity> modificationParameters, StepEntity modifiedGeometry) {
        super(id, name);
        this.originalFeature = originalFeature;
        this.modificationType = modificationType;
        this.modificationParameters = modificationParameters == null ? null : java.util.List.copyOf(modificationParameters);
        this.modifiedGeometry = modifiedGeometry;
    }

    public StepEntity getOriginalFeature() {
        return originalFeature;
    }

    public String getModificationType() {
        return modificationType;
    }

    public List<StepEntity> getModificationParameters() {
        return modificationParameters;
    }

    public StepEntity getModifiedGeometry() {
        return modifiedGeometry;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("originalFeature", originalFeature);
        state.put("modificationType", modificationType);
        state.put("modificationParameters", modificationParameters);
        state.put("modifiedGeometry", modifiedGeometry);
        return state;
    }
}
