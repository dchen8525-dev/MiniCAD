package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTERFACE_FEATURE.
 * An interface feature entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface type (mechanical, electrical, data)
 * @param interfaceGeometry interface geometry representation
 * @param interfacePosition interface position placement
 * @varianceConnections variance connections count
 * @param interfaceStandard interface standard reference
 * @param matingInterface mating interface reference
 */
/**
 * Resolved INTERFACE_FEATURE.
 * An interface feature entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface type (mechanical, electrical, data)
 * @param interfaceGeometry interface geometry representation
 * @param interfacePosition interface position placement
 * @varianceConnections variance connections count
 * @param interfaceStandard interface standard reference
 * @param matingInterface mating interface reference
 */
public final class StepInterfaceFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String interfaceType;
    private final StepEntity interfaceGeometry;
    private final StepEntity interfacePosition;
    private final int varianceConnections;
    private final String interfaceStandard;
    private final StepEntity matingInterface;

    public StepInterfaceFeature(int id, String name, String interfaceType, StepEntity interfaceGeometry, StepEntity interfacePosition, int varianceConnections, String interfaceStandard, StepEntity matingInterface) {
        this.id = id;
        this.name = name;
        this.interfaceType = interfaceType;
        this.interfaceGeometry = interfaceGeometry;
        this.interfacePosition = interfacePosition;
        this.varianceConnections = varianceConnections;
        this.interfaceStandard = interfaceStandard;
        this.matingInterface = matingInterface;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public StepEntity getInterfaceGeometry() {
        return interfaceGeometry;
    }

    public StepEntity getInterfacePosition() {
        return interfacePosition;
    }

    public int getVarianceConnections() {
        return varianceConnections;
    }

    public String getInterfaceStandard() {
        return interfaceStandard;
    }

    public StepEntity getMatingInterface() {
        return matingInterface;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInterfaceFeature that = (StepInterfaceFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(interfaceType, that.interfaceType) && Objects.equals(interfaceGeometry, that.interfaceGeometry) && Objects.equals(interfacePosition, that.interfacePosition) && varianceConnections == that.varianceConnections && Objects.equals(interfaceStandard, that.interfaceStandard) && Objects.equals(matingInterface, that.matingInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, interfaceType, interfaceGeometry, interfacePosition, varianceConnections, interfaceStandard, matingInterface);
    }

    @Override
    public String toString() {
        return "StepInterfaceFeature{" + "id=" + id + "name=" + name + "interfaceType=" + interfaceType + "interfaceGeometry=" + interfaceGeometry + "interfacePosition=" + interfacePosition + "varianceConnections=" + varianceConnections + "interfaceStandard=" + interfaceStandard + "matingInterface=" + matingInterface + "}";
    }
}