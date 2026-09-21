package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepProfileDef extends AbstractStepEntity {
    private final String profileType;
    private final String profileName;
    private final StepEntity position;
    private final List<StepEntity> curves;
    private final List<Double> parameters;
    private final String entityName;

    public StepProfileDef(int id, String profileType, String profileName, StepEntity position, List<StepEntity> curves, List<Double> parameters, String entityName) {
        super(id, "");
        this.profileType = profileType;
        this.profileName = profileName;
        this.position = position;
        this.curves = curves == null ? null : java.util.List.copyOf(curves);
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
        this.entityName = entityName;
    }

    public String getName() {
        return profileName != null ? profileName : "";
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public String entityName() { return getEntityName(); }
    public String profileType() { return getProfileType(); }
    public String profileName() { return getProfileName(); }
    public List<StepEntity> curves() { return getCurves(); }
    public List<Double> parameters() { return getParameters(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("profileType", profileType);
        state.put("profileName", profileName);
        state.put("position", position);
        state.put("curves", curves);
        state.put("parameters", parameters);
        state.put("entityName", entityName);
        return state;
    }
}
