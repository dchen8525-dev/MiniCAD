package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "channelDefinition", channelDefinition,
                "channelState", channelState, "channelOccupancy", channelOccupancy,
                "channelMessages", channelMessages, "channelStatus", channelStatus);
    }
}
