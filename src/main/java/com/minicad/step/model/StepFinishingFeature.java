package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FINISHING_FEATURE.
 * A finishing feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param finishingType finishing type (polishing, grinding, honing, lapping)
 * @param surfaceGeometry surface geometry to be finished
 * @param surfaceRoughness target surface roughness (Ra)
 * @param finishingParameters finishing process parameters
 * @param finishingMaterial finishing material/tool reference
 */
/**
 * Resolved FINISHING_FEATURE.
 * A finishing feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param finishingType finishing type (polishing, grinding, honing, lapping)
 * @param surfaceGeometry surface geometry to be finished
 * @param surfaceRoughness target surface roughness (Ra)
 * @param finishingParameters finishing process parameters
 * @param finishingMaterial finishing material/tool reference
 */
public final class StepFinishingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String finishingType;
    private final StepEntity surfaceGeometry;
    private final double surfaceRoughness;
    private final List<Double> finishingParameters;
    private final StepEntity finishingMaterial;

    public StepFinishingFeature(int id, String name, String finishingType, StepEntity surfaceGeometry, double surfaceRoughness, List<Double> finishingParameters, StepEntity finishingMaterial) {
        this.id = id;
        this.name = name;
        this.finishingType = finishingType;
        this.surfaceGeometry = surfaceGeometry;
        this.surfaceRoughness = surfaceRoughness;
        this.finishingParameters = finishingParameters == null ? null : java.util.List.copyOf(finishingParameters);
        this.finishingMaterial = finishingMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFinishingType() {
        return finishingType;
    }

    public StepEntity getSurfaceGeometry() {
        return surfaceGeometry;
    }

    public double getSurfaceRoughness() {
        return surfaceRoughness;
    }

    public List<Double> getFinishingParameters() {
        return finishingParameters;
    }

    public StepEntity getFinishingMaterial() {
        return finishingMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFinishingFeature that = (StepFinishingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(finishingType, that.finishingType) && Objects.equals(surfaceGeometry, that.surfaceGeometry) && surfaceRoughness == that.surfaceRoughness && Objects.equals(finishingParameters, that.finishingParameters) && Objects.equals(finishingMaterial, that.finishingMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, finishingType, surfaceGeometry, surfaceRoughness, finishingParameters, finishingMaterial);
    }

    @Override
    public String toString() {
        return "StepFinishingFeature{" + "id=" + id + "name=" + name + "finishingType=" + finishingType + "surfaceGeometry=" + surfaceGeometry + "surfaceRoughness=" + surfaceRoughness + "finishingParameters=" + finishingParameters + "finishingMaterial=" + finishingMaterial + "}";
    }
}