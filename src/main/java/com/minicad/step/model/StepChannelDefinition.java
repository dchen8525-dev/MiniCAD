package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepChannelDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String channelType;
    private final String channelDirection;
    private final String channelProtocol;
    private final int channelCapacity;
    private final String channelStatus;

    public StepChannelDefinition(int id, String name, String channelType, String channelDirection, String channelProtocol, int channelCapacity, String channelStatus) {
        this.id = id;
        this.name = name;
        this.channelType = channelType;
        this.channelDirection = channelDirection;
        this.channelProtocol = channelProtocol;
        this.channelCapacity = channelCapacity;
        this.channelStatus = channelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChannelDefinition that = (StepChannelDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(channelType, that.channelType) && Objects.equals(channelDirection, that.channelDirection) && Objects.equals(channelProtocol, that.channelProtocol) && channelCapacity == that.channelCapacity && Objects.equals(channelStatus, that.channelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, channelType, channelDirection, channelProtocol, channelCapacity, channelStatus);
    }

    @Override
    public String toString() {
        return "StepChannelDefinition{" + "id=" + id + "name=" + name + "channelType=" + channelType + "channelDirection=" + channelDirection + "channelProtocol=" + channelProtocol + "channelCapacity=" + channelCapacity + "channelStatus=" + channelStatus + "}";
    }
}