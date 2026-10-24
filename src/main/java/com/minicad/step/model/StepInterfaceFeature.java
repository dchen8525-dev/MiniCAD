package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepInterfaceFeature extends AbstractStepEntity {
    private final String interfaceType;
    private final StepEntity interfaceGeometry;
    private final StepEntity interfacePosition;
    private final int varianceConnections;
    private final String interfaceStandard;
    private final StepEntity matingInterface;

    public StepInterfaceFeature(int id, String name, String interfaceType, StepEntity interfaceGeometry, StepEntity interfacePosition, int varianceConnections, String interfaceStandard, StepEntity matingInterface) {
        super(id, name);
        this.interfaceType = interfaceType;
        this.interfaceGeometry = interfaceGeometry;
        this.interfacePosition = interfacePosition;
        this.varianceConnections = varianceConnections;
        this.interfaceStandard = interfaceStandard;
        this.matingInterface = matingInterface;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "interfaceType", interfaceType,
                "interfaceGeometry", interfaceGeometry, "interfacePosition", interfacePosition,
                "varianceConnections", varianceConnections, "interfaceStandard", interfaceStandard,
                "matingInterface", matingInterface);
    }
}
