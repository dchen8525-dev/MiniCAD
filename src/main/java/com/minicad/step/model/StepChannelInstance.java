package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepChannelInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity channelDefinition;
    private final String channelState;
    private final double channelOccupancy;
    private final int channelMessages;
    private final String channelStatus;

    public StepChannelInstance(int id, String name, StepEntity channelDefinition, String channelState, double channelOccupancy, int channelMessages, String channelStatus) {
        this.id = id;
        this.name = name;
        this.channelDefinition = channelDefinition;
        this.channelState = channelState;
        this.channelOccupancy = channelOccupancy;
        this.channelMessages = channelMessages;
        this.channelStatus = channelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChannelInstance that = (StepChannelInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(channelDefinition, that.channelDefinition) && Objects.equals(channelState, that.channelState) && channelOccupancy == that.channelOccupancy && channelMessages == that.channelMessages && Objects.equals(channelStatus, that.channelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, channelDefinition, channelState, channelOccupancy, channelMessages, channelStatus);
    }

    @Override
    public String toString() {
        return "StepChannelInstance{" + "id=" + id + "name=" + name + "channelDefinition=" + channelDefinition + "channelState=" + channelState + "channelOccupancy=" + channelOccupancy + "channelMessages=" + channelMessages + "channelStatus=" + channelStatus + "}";
    }
}