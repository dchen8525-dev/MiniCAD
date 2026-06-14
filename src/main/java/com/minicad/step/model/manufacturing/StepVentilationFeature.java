package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VENTILATION_FEATURE.
 * A ventilation feature entity.
 *
 * @param id STEP instance id
 * @param name ventilation name
 * @param ventilationType ventilation type (natural, forced, exhaust)
 * @param ventilationGeometry ventilation geometry representation
 * @varianceAirflow variance airflow capacity
 * @param inletFeatures inlet features
 * @param outletFeatures outlet features
 * @param ventilationControl ventilation control specification
 */
/**
 * Resolved VENTILATION_FEATURE.
 * A ventilation feature entity.
 *
 * @param id STEP instance id
 * @param name ventilation name
 * @param ventilationType ventilation type (natural, forced, exhaust)
 * @param ventilationGeometry ventilation geometry representation
 * @varianceAirflow variance airflow capacity
 * @param inletFeatures inlet features
 * @param outletFeatures outlet features
 * @param ventilationControl ventilation control specification
 */
public final class StepVentilationFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String ventilationType;
    private final StepEntity ventilationGeometry;
    private final double varianceAirflow;
    private final List<StepEntity> inletFeatures;
    private final List<StepEntity> outletFeatures;
    private final StepEntity ventilationControl;

    public StepVentilationFeature(int id, String name, String ventilationType, StepEntity ventilationGeometry, double varianceAirflow, List<StepEntity> inletFeatures, List<StepEntity> outletFeatures, StepEntity ventilationControl) {
        this.id = id;
        this.name = name;
        this.ventilationType = ventilationType;
        this.ventilationGeometry = ventilationGeometry;
        this.varianceAirflow = varianceAirflow;
        this.inletFeatures = inletFeatures == null ? null : java.util.List.copyOf(inletFeatures);
        this.outletFeatures = outletFeatures == null ? null : java.util.List.copyOf(outletFeatures);
        this.ventilationControl = ventilationControl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVentilationType() {
        return ventilationType;
    }

    public StepEntity getVentilationGeometry() {
        return ventilationGeometry;
    }

    public double getVarianceAirflow() {
        return varianceAirflow;
    }

    public List<StepEntity> getInletFeatures() {
        return inletFeatures;
    }

    public List<StepEntity> getOutletFeatures() {
        return outletFeatures;
    }

    public StepEntity getVentilationControl() {
        return ventilationControl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVentilationFeature that = (StepVentilationFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(ventilationType, that.ventilationType) && Objects.equals(ventilationGeometry, that.ventilationGeometry) && varianceAirflow == that.varianceAirflow && Objects.equals(inletFeatures, that.inletFeatures) && Objects.equals(outletFeatures, that.outletFeatures) && Objects.equals(ventilationControl, that.ventilationControl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ventilationType, ventilationGeometry, varianceAirflow, inletFeatures, outletFeatures, ventilationControl);
    }

    @Override
    public String toString() {
        return "StepVentilationFeature{" + "id=" + id + "name=" + name + "ventilationType=" + ventilationType + "ventilationGeometry=" + ventilationGeometry + "varianceAirflow=" + varianceAirflow + "inletFeatures=" + inletFeatures + "outletFeatures=" + outletFeatures + "ventilationControl=" + ventilationControl + "}";
    }
}