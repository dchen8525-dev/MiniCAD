package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FITTING_FEATURE.
 * A fitting feature entity.
 *
 * @param id STEP instance id
 * @param name fitting name
 * @param fittingType fitting type (elbow, tee, reducer, coupling)
 * @param fittingAngle fitting angle for elbows
 * @param connectionEnds connection end features
 * @param fittingMaterial fitting material specification
 * @param fittingStandard fitting standard specification
 */
/**
 * Resolved FITTING_FEATURE.
 * A fitting feature entity.
 *
 * @param id STEP instance id
 * @param name fitting name
 * @param fittingType fitting type (elbow, tee, reducer, coupling)
 * @param fittingAngle fitting angle for elbows
 * @param connectionEnds connection end features
 * @param fittingMaterial fitting material specification
 * @param fittingStandard fitting standard specification
 */
public final class StepFittingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String fittingType;
    private final double fittingAngle;
    private final List<StepEntity> connectionEnds;
    private final StepEntity fittingMaterial;
    private final String fittingStandard;

    public StepFittingFeature(int id, String name, String fittingType, double fittingAngle, List<StepEntity> connectionEnds, StepEntity fittingMaterial, String fittingStandard) {
        this.id = id;
        this.name = name;
        this.fittingType = fittingType;
        this.fittingAngle = fittingAngle;
        this.connectionEnds = connectionEnds == null ? null : java.util.List.copyOf(connectionEnds);
        this.fittingMaterial = fittingMaterial;
        this.fittingStandard = fittingStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFittingType() {
        return fittingType;
    }

    public double getFittingAngle() {
        return fittingAngle;
    }

    public List<StepEntity> getConnectionEnds() {
        return connectionEnds;
    }

    public StepEntity getFittingMaterial() {
        return fittingMaterial;
    }

    public String getFittingStandard() {
        return fittingStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFittingFeature that = (StepFittingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(fittingType, that.fittingType) && fittingAngle == that.fittingAngle && Objects.equals(connectionEnds, that.connectionEnds) && Objects.equals(fittingMaterial, that.fittingMaterial) && Objects.equals(fittingStandard, that.fittingStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fittingType, fittingAngle, connectionEnds, fittingMaterial, fittingStandard);
    }

    @Override
    public String toString() {
        return "StepFittingFeature{" + "id=" + id + "name=" + name + "fittingType=" + fittingType + "fittingAngle=" + fittingAngle + "connectionEnds=" + connectionEnds + "fittingMaterial=" + fittingMaterial + "fittingStandard=" + fittingStandard + "}";
    }
}