package com.minicad.step.model;

import java.util.List;

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
public record StepChannelDefinition(
    int id,
    String name,
    String channelType,
    String channelDirection,
    String channelProtocol,
    int channelCapacity,
    String channelStatus) implements StepEntity {
}