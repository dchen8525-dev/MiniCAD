package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SEAL_FEATURE.
 * A seal feature entity.
 *
 * @param id STEP instance id
 * @param name seal name
 * @param sealType seal type classification (O-ring, gasket, lip seal)
 * @param innerDiameter inner diameter
 * @param outerDiameter outer diameter
 * @param sealWidth seal width/cross-section
 * @param sealMaterial seal material specification
 * @param sealPlacement seal position placement
 */
public final class StepSealFeature extends AbstractStepEntity {
    private final String sealType;
    private final double innerDiameter;
    private final double outerDiameter;
    private final double sealWidth;
    private final StepEntity sealMaterial;
    private final StepEntity sealPlacement;

    public StepSealFeature(int id, String name, String sealType, double innerDiameter, double outerDiameter, double sealWidth, StepEntity sealMaterial, StepEntity sealPlacement) {
        super(id, name);
        this.sealType = sealType;
        this.innerDiameter = innerDiameter;
        this.outerDiameter = outerDiameter;
        this.sealWidth = sealWidth;
        this.sealMaterial = sealMaterial;
        this.sealPlacement = sealPlacement;
    }

    public String getSealType() {
        return sealType;
    }

    public double getInnerDiameter() {
        return innerDiameter;
    }

    public double getOuterDiameter() {
        return outerDiameter;
    }

    public double getSealWidth() {
        return sealWidth;
    }

    public StepEntity getSealMaterial() {
        return sealMaterial;
    }

    public StepEntity getSealPlacement() {
        return sealPlacement;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sealType", sealType);
        state.put("innerDiameter", innerDiameter);
        state.put("outerDiameter", outerDiameter);
        state.put("sealWidth", sealWidth);
        state.put("sealMaterial", sealMaterial);
        state.put("sealPlacement", sealPlacement);
        return state;
    }
}
