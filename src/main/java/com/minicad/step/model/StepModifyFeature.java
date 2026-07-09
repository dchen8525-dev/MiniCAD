package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepModifyFeature implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity originalFeature;
    private final String modificationType;
    private final List<StepEntity> modificationParameters;
    private final StepEntity modifiedGeometry;

    public StepModifyFeature(int id, String name, StepEntity originalFeature, String modificationType, List<StepEntity> modificationParameters, StepEntity modifiedGeometry) {
        this.id = id;
        this.name = name;
        this.originalFeature = originalFeature;
        this.modificationType = modificationType;
        this.modificationParameters = modificationParameters == null ? null : java.util.List.copyOf(modificationParameters);
        this.modifiedGeometry = modifiedGeometry;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModifyFeature that = (StepModifyFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(originalFeature, that.originalFeature) && Objects.equals(modificationType, that.modificationType) && Objects.equals(modificationParameters, that.modificationParameters) && Objects.equals(modifiedGeometry, that.modifiedGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, originalFeature, modificationType, modificationParameters, modifiedGeometry);
    }

    @Override
    public String toString() {
        return "StepModifyFeature{" + "id=" + id + "name=" + name + "originalFeature=" + originalFeature + "modificationType=" + modificationType + "modificationParameters=" + modificationParameters + "modifiedGeometry=" + modifiedGeometry + "}";
    }
}