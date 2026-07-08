package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HANDLING_FEATURE.
 * A handling feature entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @param handlingType handling type (lift, grab, support, transport)
 * @param handlingGeometry handling geometry representation
 * @param handlingPoints handling point locations
 * @param handlingWeight handling weight capacity
 * @param handlingEquipment handling equipment reference
 */
/**
 * Resolved HANDLING_FEATURE.
 * A handling feature entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @param handlingType handling type (lift, grab, support, transport)
 * @param handlingGeometry handling geometry representation
 * @param handlingPoints handling point locations
 * @param handlingWeight handling weight capacity
 * @param handlingEquipment handling equipment reference
 */
public final class StepHandlingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String handlingType;
    private final StepEntity handlingGeometry;
    private final List<StepEntity> handlingPoints;
    private final double handlingWeight;
    private final StepEntity handlingEquipment;

    public StepHandlingFeature(int id, String name, String handlingType, StepEntity handlingGeometry, List<StepEntity> handlingPoints, double handlingWeight, StepEntity handlingEquipment) {
        this.id = id;
        this.name = name;
        this.handlingType = handlingType;
        this.handlingGeometry = handlingGeometry;
        this.handlingPoints = handlingPoints == null ? null : java.util.List.copyOf(handlingPoints);
        this.handlingWeight = handlingWeight;
        this.handlingEquipment = handlingEquipment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHandlingType() {
        return handlingType;
    }

    public StepEntity getHandlingGeometry() {
        return handlingGeometry;
    }

    public List<StepEntity> getHandlingPoints() {
        return handlingPoints;
    }

    public double getHandlingWeight() {
        return handlingWeight;
    }

    public StepEntity getHandlingEquipment() {
        return handlingEquipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHandlingFeature that = (StepHandlingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(handlingType, that.handlingType) && Objects.equals(handlingGeometry, that.handlingGeometry) && Objects.equals(handlingPoints, that.handlingPoints) && handlingWeight == that.handlingWeight && Objects.equals(handlingEquipment, that.handlingEquipment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handlingType, handlingGeometry, handlingPoints, handlingWeight, handlingEquipment);
    }

    @Override
    public String toString() {
        return "StepHandlingFeature{" + "id=" + id + "name=" + name + "handlingType=" + handlingType + "handlingGeometry=" + handlingGeometry + "handlingPoints=" + handlingPoints + "handlingWeight=" + handlingWeight + "handlingEquipment=" + handlingEquipment + "}";
    }
}