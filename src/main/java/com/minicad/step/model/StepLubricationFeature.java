package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LUBRICATION_FEATURE.
 * A lubrication feature entity.
 *
 * @param id STEP instance id
 * @param name lubrication name
 * @param lubricationType lubrication type (oil, grease, spray)
 * @param lubricationPoints lubrication point locations
 * @param lubricationMethod lubrication method specification
 * @param lubricationInterval lubrication interval/frequency
 * @param lubricantType lubricant type specification
 */
public final class StepLubricationFeature extends AbstractStepEntity {
    private final String lubricationType;
    private final List<StepEntity> lubricationPoints;
    private final String lubricationMethod;
    private final String lubricationInterval;
    private final String lubricantType;

    public StepLubricationFeature(int id, String name, String lubricationType, List<StepEntity> lubricationPoints, String lubricationMethod, String lubricationInterval, String lubricantType) {
        super(id, name);
        this.lubricationType = lubricationType;
        this.lubricationPoints = lubricationPoints == null ? null : java.util.List.copyOf(lubricationPoints);
        this.lubricationMethod = lubricationMethod;
        this.lubricationInterval = lubricationInterval;
        this.lubricantType = lubricantType;
    }

    public String getLubricationType() {
        return lubricationType;
    }

    public List<StepEntity> getLubricationPoints() {
        return lubricationPoints;
    }

    public String getLubricationMethod() {
        return lubricationMethod;
    }

    public String getLubricationInterval() {
        return lubricationInterval;
    }

    public String getLubricantType() {
        return lubricantType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("lubricationType", lubricationType);
        state.put("lubricationPoints", lubricationPoints);
        state.put("lubricationMethod", lubricationMethod);
        state.put("lubricationInterval", lubricationInterval);
        state.put("lubricantType", lubricantType);
        return state;
    }
}
