package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INTERFACE_INSTANCE.
 * An interface instance entity.
 *
 * @param id STEP instance id
 * @param name interface instance name
 * @param interfaceDefinition interface variance definition reference
 * @param interfaceLocation interface variance location reference
 * @param interfaceState interface variance state
 * @param interfaceConnections interface variance connections
 * @param interfaceStatus interface variance status
 */
public final class StepInterfaceInstance extends AbstractStepEntity {
    private final StepEntity interfaceDefinition;
    private final StepEntity interfaceLocation;
    private final String interfaceState;
    private final List<StepEntity> interfaceConnections;
    private final String interfaceStatus;

    public StepInterfaceInstance(int id, String name, StepEntity interfaceDefinition, StepEntity interfaceLocation, String interfaceState, List<StepEntity> interfaceConnections, String interfaceStatus) {
        super(id, name);
        this.interfaceDefinition = interfaceDefinition;
        this.interfaceLocation = interfaceLocation;
        this.interfaceState = interfaceState;
        this.interfaceConnections = interfaceConnections == null ? null : java.util.List.copyOf(interfaceConnections);
        this.interfaceStatus = interfaceStatus;
    }

    public StepEntity getInterfaceDefinition() {
        return interfaceDefinition;
    }

    public StepEntity getInterfaceLocation() {
        return interfaceLocation;
    }

    public String getInterfaceState() {
        return interfaceState;
    }

    public List<StepEntity> getInterfaceConnections() {
        return interfaceConnections;
    }

    public String getInterfaceStatus() {
        return interfaceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("interfaceDefinition", interfaceDefinition);
        state.put("interfaceLocation", interfaceLocation);
        state.put("interfaceState", interfaceState);
        state.put("interfaceConnections", interfaceConnections);
        state.put("interfaceStatus", interfaceStatus);
        return state;
    }
}
