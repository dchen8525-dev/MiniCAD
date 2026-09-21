package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PAINTING_FEATURE.
 * A painting feature entity.
 *
 * @param id STEP instance id
 * @param name painting name
 * @param paintType paint type classification
 * @param paintColor paint color specification
 * @param paintThickness paint thickness
 * @param appliedSurfaces surfaces to be painted
 * @param primerCoat primer coat specification
 * @varnishCoat varnish/clear coat specification
 * @param paintStandard paint standard reference
 */
public final class StepPaintingFeature extends AbstractStepEntity {
    private final String paintType;
    private final StepEntity paintColor;
    private final double paintThickness;
    private final List<StepEntity> appliedSurfaces;
    private final StepEntity primerCoat;
    private final StepEntity varnishCoat;
    private final String paintStandard;

    public StepPaintingFeature(int id, String name, String paintType, StepEntity paintColor, double paintThickness, List<StepEntity> appliedSurfaces, StepEntity primerCoat, StepEntity varnishCoat, String paintStandard) {
        super(id, name);
        this.paintType = paintType;
        this.paintColor = paintColor;
        this.paintThickness = paintThickness;
        this.appliedSurfaces = appliedSurfaces == null ? null : java.util.List.copyOf(appliedSurfaces);
        this.primerCoat = primerCoat;
        this.varnishCoat = varnishCoat;
        this.paintStandard = paintStandard;
    }

    public String getPaintType() {
        return paintType;
    }

    public StepEntity getPaintColor() {
        return paintColor;
    }

    public double getPaintThickness() {
        return paintThickness;
    }

    public List<StepEntity> getAppliedSurfaces() {
        return appliedSurfaces;
    }

    public StepEntity getPrimerCoat() {
        return primerCoat;
    }

    public StepEntity getVarnishCoat() {
        return varnishCoat;
    }

    public String getPaintStandard() {
        return paintStandard;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("paintType", paintType);
        state.put("paintColor", paintColor);
        state.put("paintThickness", paintThickness);
        state.put("appliedSurfaces", appliedSurfaces);
        state.put("primerCoat", primerCoat);
        state.put("varnishCoat", varnishCoat);
        state.put("paintStandard", paintStandard);
        return state;
    }
}
