package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHANNEL_INSTANCE.
 * A channel instance entity.
 *
 * @param id STEP instance id
 * @param name channel instance name
 * @param channelDefinition channel variance definition reference
 * @param channelState channel variance state
 * @param channelOccupancy channel variance occupancy
 * @param channelMessages channel variance message count
 * @param channelStatus channel variance status
 */
public final class StepChannelInstance extends AbstractStepEntity {
    private final StepEntity channelDefinition;
    private final String channelState;
    private final double channelOccupancy;
    private final int channelMessages;
    private final String channelStatus;

    public StepChannelInstance(int id, String name, StepEntity channelDefinition, String channelState, double channelOccupancy, int channelMessages, String channelStatus) {
        super(id, name);
        this.channelDefinition = channelDefinition;
        this.channelState = channelState;
        this.channelOccupancy = channelOccupancy;
        this.channelMessages = channelMessages;
        this.channelStatus = channelStatus;
    }

    public StepEntity getChannelDefinition() {
        return channelDefinition;
    }

    public String getChannelState() {
        return channelState;
    }

    public double getChannelOccupancy() {
        return channelOccupancy;
    }

    public int getChannelMessages() {
        return channelMessages;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("channelDefinition", channelDefinition);
        state.put("channelState", channelState);
        state.put("channelOccupancy", channelOccupancy);
        state.put("channelMessages", channelMessages);
        state.put("channelStatus", channelStatus);
        return state;
    }
}
