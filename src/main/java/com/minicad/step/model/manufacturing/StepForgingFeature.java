package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FORGING_FEATURE.
 * A forging feature entity.
 *
 * @param id STEP instance id
 * @param name forging name
 * @param forgingType forging type classification (open die, closed die, upset)
 * @param forgingGeometry forging geometry representation
 * @param dieFlash die flash allowance
 * @param forgingGrain grain direction specification
 * @param forgingMaterial forging material specification
 * @param forgingTemperature forging temperature range
 */
/**
 * Resolved FORGING_FEATURE.
 * A forging feature entity.
 *
 * @param id STEP instance id
 * @param name forging name
 * @param forgingType forging type classification (open die, closed die, upset)
 * @param forgingGeometry forging geometry representation
 * @param dieFlash die flash allowance
 * @param forgingGrain grain direction specification
 * @param forgingMaterial forging material specification
 * @param forgingTemperature forging temperature range
 */
public final class StepForgingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String forgingType;
    private final StepEntity forgingGeometry;
    private final double dieFlash;
    private final StepEntity forgingGrain;
    private final StepEntity forgingMaterial;
    private final List<Double> forgingTemperature;

    public StepForgingFeature(int id, String name, String forgingType, StepEntity forgingGeometry, double dieFlash, StepEntity forgingGrain, StepEntity forgingMaterial, List<Double> forgingTemperature) {
        this.id = id;
        this.name = name;
        this.forgingType = forgingType;
        this.forgingGeometry = forgingGeometry;
        this.dieFlash = dieFlash;
        this.forgingGrain = forgingGrain;
        this.forgingMaterial = forgingMaterial;
        this.forgingTemperature = forgingTemperature == null ? null : java.util.List.copyOf(forgingTemperature);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getForgingType() {
        return forgingType;
    }

    public StepEntity getForgingGeometry() {
        return forgingGeometry;
    }

    public double getDieFlash() {
        return dieFlash;
    }

    public StepEntity getForgingGrain() {
        return forgingGrain;
    }

    public StepEntity getForgingMaterial() {
        return forgingMaterial;
    }

    public List<Double> getForgingTemperature() {
        return forgingTemperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepForgingFeature that = (StepForgingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(forgingType, that.forgingType) && Objects.equals(forgingGeometry, that.forgingGeometry) && dieFlash == that.dieFlash && Objects.equals(forgingGrain, that.forgingGrain) && Objects.equals(forgingMaterial, that.forgingMaterial) && Objects.equals(forgingTemperature, that.forgingTemperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, forgingType, forgingGeometry, dieFlash, forgingGrain, forgingMaterial, forgingTemperature);
    }

    @Override
    public String toString() {
        return "StepForgingFeature{" + "id=" + id + "name=" + name + "forgingType=" + forgingType + "forgingGeometry=" + forgingGeometry + "dieFlash=" + dieFlash + "forgingGrain=" + forgingGrain + "forgingMaterial=" + forgingMaterial + "forgingTemperature=" + forgingTemperature + "}";
    }
}