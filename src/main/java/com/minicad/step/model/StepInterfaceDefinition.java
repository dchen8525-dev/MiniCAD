package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INTERFACE_DEFINITION.
 * An interface definition entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface variance type
 * @param interfaceProtocol interface variance protocol
 * @param interfaceParameters interface variance parameters
 * @param interfaceConstraints interface variance constraints
 * @param interfaceStatus interface variance status
 */
public final class StepInterfaceDefinition extends AbstractStepEntity {
    private final String interfaceType;
    private final String interfaceProtocol;
    private final List<String> interfaceParameters;
    private final List<String> interfaceConstraints;
    private final String interfaceStatus;

    public StepInterfaceDefinition(int id, String name, String interfaceType, String interfaceProtocol, List<String> interfaceParameters, List<String> interfaceConstraints, String interfaceStatus) {
        super(id, name);
        this.interfaceType = interfaceType;
        this.interfaceProtocol = interfaceProtocol;
        this.interfaceParameters = interfaceParameters == null ? null : java.util.List.copyOf(interfaceParameters);
        this.interfaceConstraints = interfaceConstraints == null ? null : java.util.List.copyOf(interfaceConstraints);
        this.interfaceStatus = interfaceStatus;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public String getInterfaceProtocol() {
        return interfaceProtocol;
    }

    public List<String> getInterfaceParameters() {
        return interfaceParameters;
    }

    public List<String> getInterfaceConstraints() {
        return interfaceConstraints;
    }

    public String getInterfaceStatus() {
        return interfaceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("interfaceType", interfaceType);
        state.put("interfaceProtocol", interfaceProtocol);
        state.put("interfaceParameters", interfaceParameters);
        state.put("interfaceConstraints", interfaceConstraints);
        state.put("interfaceStatus", interfaceStatus);
        return state;
    }
}
