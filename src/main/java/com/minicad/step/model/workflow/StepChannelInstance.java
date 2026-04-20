package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepChannelInstance(
    int id,
    String name,
    StepEntity channelDefinition,
    String channelState,
    double channelOccupancy,
    int channelMessages,
    String channelStatus) implements StepEntity {
}