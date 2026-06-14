package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal profile definition used by swept area solids.
 *
 * @param id step id
 * @param profileType profile type enum token
 * @param profileName profile label
 * @param position optional parameterized profile placement
 * @param curves referenced profile curves, if any
 * @param parameters numeric profile parameters in STEP order
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal profile definition used by swept area solids.
 *
 * @param id step id
 * @param profileType profile type enum token
 * @param profileName profile label
 * @param position optional parameterized profile placement
 * @param curves referenced profile curves, if any
 * @param parameters numeric profile parameters in STEP order
 * @param entityName concrete STEP entity name
 */
public final class StepProfileDef implements StepEntity {
    private final int id;
    private final String profileType;
    private final String profileName;
    private final StepEntity position;
    private final List<StepEntity> curves;
    private final List<Double> parameters;
    private final String entityName;

    public StepProfileDef(int id, String profileType, String profileName, StepEntity position, List<StepEntity> curves, List<Double> parameters, String entityName) {
        this.id = id;
        this.profileType = profileType;
        this.profileName = profileName;
        this.position = position;
        this.curves = curves == null ? null : java.util.List.copyOf(curves);
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getProfileType() {
        return profileType;
    }

    public String getProfileName() {
        return profileName;
    }

    public StepEntity getPosition() {
        return position;
    }

    public List<StepEntity> getCurves() {
        return curves;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProfileDef that = (StepProfileDef) o;
        return id == that.id && Objects.equals(profileType, that.profileType) && Objects.equals(profileName, that.profileName) && Objects.equals(position, that.position) && Objects.equals(curves, that.curves) && Objects.equals(parameters, that.parameters) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileType, profileName, position, curves, parameters, entityName);
    }

    @Override
    public String toString() {
        return "StepProfileDef{" + "id=" + id + "profileType=" + profileType + "profileName=" + profileName + "position=" + position + "curves=" + curves + "parameters=" + parameters + "entityName=" + entityName + "}";
    }
}
