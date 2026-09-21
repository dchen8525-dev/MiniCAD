package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHANNEL_DEFINITION.
 * A channel definition entity.
 *
 * @param id STEP instance id
 * @param name channel name
 * @param channelType channel variance type
 * @param channelDirection channel variance direction
 * @param channelProtocol channel variance protocol
 * @param channelCapacity channel variance capacity
 * @param channelStatus channel variance status
 */
public final class StepChannelDefinition extends AbstractStepEntity {
    private final String channelType;
    private final String channelDirection;
    private final String channelProtocol;
    private final int channelCapacity;
    private final String channelStatus;

    public StepChannelDefinition(int id, String name, String channelType, String channelDirection, String channelProtocol, int channelCapacity, String channelStatus) {
        super(id, name);
        this.channelType = channelType;
        this.channelDirection = channelDirection;
        this.channelProtocol = channelProtocol;
        this.channelCapacity = channelCapacity;
        this.channelStatus = channelStatus;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getChannelDirection() {
        return channelDirection;
    }

    public String getChannelProtocol() {
        return channelProtocol;
    }

    public int getChannelCapacity() {
        return channelCapacity;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("channelType", channelType);
        state.put("channelDirection", channelDirection);
        state.put("channelProtocol", channelProtocol);
        state.put("channelCapacity", channelCapacity);
        state.put("channelStatus", channelStatus);
        return state;
    }
}
