package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PIPE_FEATURE.
 * A pipe feature entity.
 *
 * @param id STEP instance id
 * @param name pipe name
 * @param pipeType pipe type classification (straight, bent)
 * @param outerDiameter outer diameter
 * @param innerDiameter inner diameter
 * @param pipeLength pipe length
 * @param wallThickness wall thickness
 * @param pipeBends pipe bend features for bent pipes
 * @param pipeMaterial pipe material specification
 */
public final class StepPipeFeature extends AbstractStepEntity {
    private final String pipeType;
    private final double outerDiameter;
    private final double innerDiameter;
    private final double pipeLength;
    private final double wallThickness;
    private final List<StepEntity> pipeBends;
    private final StepEntity pipeMaterial;

    public StepPipeFeature(int id, String name, String pipeType, double outerDiameter, double innerDiameter, double pipeLength, double wallThickness, List<StepEntity> pipeBends, StepEntity pipeMaterial) {
        super(id, name);
        this.pipeType = pipeType;
        this.outerDiameter = outerDiameter;
        this.innerDiameter = innerDiameter;
        this.pipeLength = pipeLength;
        this.wallThickness = wallThickness;
        this.pipeBends = pipeBends == null ? null : java.util.List.copyOf(pipeBends);
        this.pipeMaterial = pipeMaterial;
    }

    public String getPipeType() {
        return pipeType;
    }

    public double getOuterDiameter() {
        return outerDiameter;
    }

    public double getInnerDiameter() {
        return innerDiameter;
    }

    public double getPipeLength() {
        return pipeLength;
    }

    public double getWallThickness() {
        return wallThickness;
    }

    public List<StepEntity> getPipeBends() {
        return pipeBends;
    }

    public StepEntity getPipeMaterial() {
        return pipeMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pipeType", pipeType);
        state.put("outerDiameter", outerDiameter);
        state.put("innerDiameter", innerDiameter);
        state.put("pipeLength", pipeLength);
        state.put("wallThickness", wallThickness);
        state.put("pipeBends", pipeBends);
        state.put("pipeMaterial", pipeMaterial);
        return state;
    }
}
