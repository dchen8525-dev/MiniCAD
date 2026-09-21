package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved JIG_FEATURE.
 * A jig feature entity.
 *
 * @param id STEP instance id
 * @param name jig name
 * @param jigType jig type classification
 * @param jigGeometry jig geometry representation
 * @param guideElements guide elements for tool positioning
 * @param referenceSurfaces reference surfaces for alignment
 * @param jigCapacity jig capacity/workpiece size
 * @param jigMaterial jig material reference
 */
public final class StepJigFeature extends AbstractStepEntity {
    private final String jigType;
    private final StepEntity jigGeometry;
    private final List<StepEntity> guideElements;
    private final List<StepEntity> referenceSurfaces;
    private final double jigCapacity;
    private final StepEntity jigMaterial;

    public StepJigFeature(int id, String name, String jigType, StepEntity jigGeometry, List<StepEntity> guideElements, List<StepEntity> referenceSurfaces, double jigCapacity, StepEntity jigMaterial) {
        super(id, name);
        this.jigType = jigType;
        this.jigGeometry = jigGeometry;
        this.guideElements = guideElements == null ? null : java.util.List.copyOf(guideElements);
        this.referenceSurfaces = referenceSurfaces == null ? null : java.util.List.copyOf(referenceSurfaces);
        this.jigCapacity = jigCapacity;
        this.jigMaterial = jigMaterial;
    }

    public String getJigType() {
        return jigType;
    }

    public StepEntity getJigGeometry() {
        return jigGeometry;
    }

    public List<StepEntity> getGuideElements() {
        return guideElements;
    }

    public List<StepEntity> getReferenceSurfaces() {
        return referenceSurfaces;
    }

    public double getJigCapacity() {
        return jigCapacity;
    }

    public StepEntity getJigMaterial() {
        return jigMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("jigType", jigType);
        state.put("jigGeometry", jigGeometry);
        state.put("guideElements", guideElements);
        state.put("referenceSurfaces", referenceSurfaces);
        state.put("jigCapacity", jigCapacity);
        state.put("jigMaterial", jigMaterial);
        return state;
    }
}
