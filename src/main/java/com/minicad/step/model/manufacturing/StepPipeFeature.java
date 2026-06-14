package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPipeFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String pipeType;
    private final double outerDiameter;
    private final double innerDiameter;
    private final double pipeLength;
    private final double wallThickness;
    private final List<StepEntity> pipeBends;
    private final StepEntity pipeMaterial;

    public StepPipeFeature(int id, String name, String pipeType, double outerDiameter, double innerDiameter, double pipeLength, double wallThickness, List<StepEntity> pipeBends, StepEntity pipeMaterial) {
        this.id = id;
        this.name = name;
        this.pipeType = pipeType;
        this.outerDiameter = outerDiameter;
        this.innerDiameter = innerDiameter;
        this.pipeLength = pipeLength;
        this.wallThickness = wallThickness;
        this.pipeBends = pipeBends == null ? null : java.util.List.copyOf(pipeBends);
        this.pipeMaterial = pipeMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPipeFeature that = (StepPipeFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pipeType, that.pipeType) && outerDiameter == that.outerDiameter && innerDiameter == that.innerDiameter && pipeLength == that.pipeLength && wallThickness == that.wallThickness && Objects.equals(pipeBends, that.pipeBends) && Objects.equals(pipeMaterial, that.pipeMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pipeType, outerDiameter, innerDiameter, pipeLength, wallThickness, pipeBends, pipeMaterial);
    }

    @Override
    public String toString() {
        return "StepPipeFeature{" + "id=" + id + "name=" + name + "pipeType=" + pipeType + "outerDiameter=" + outerDiameter + "innerDiameter=" + innerDiameter + "pipeLength=" + pipeLength + "wallThickness=" + wallThickness + "pipeBends=" + pipeBends + "pipeMaterial=" + pipeMaterial + "}";
    }
}