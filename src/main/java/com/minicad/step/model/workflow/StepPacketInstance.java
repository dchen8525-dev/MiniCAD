package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PACKET_INSTANCE.
 * A packet instance entity.
 *
 * @param id STEP instance id
 * @param name packet instance name
 * @param packetDefinition packet variance definition reference
 * @param packetSequence packet variance sequence number
 * @param packetData packet variance data content
 * @param packetChecksum packet variance checksum
 * @param packetStatus packet variance status
 */
public record StepPacketInstance(
    int id,
    String name,
    StepEntity packetDefinition,
    long packetSequence,
    String packetData,
    String packetChecksum,
    String packetStatus) implements StepEntity {
}