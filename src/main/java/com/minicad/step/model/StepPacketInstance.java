package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepPacketInstance extends AbstractStepEntity {
    private final StepEntity packetDefinition;
    private final long packetSequence;
    private final String packetData;
    private final String packetChecksum;
    private final String packetStatus;

    public StepPacketInstance(int id, String name, StepEntity packetDefinition, long packetSequence, String packetData, String packetChecksum, String packetStatus) {
        super(id, name);
        this.packetDefinition = packetDefinition;
        this.packetSequence = packetSequence;
        this.packetData = packetData;
        this.packetChecksum = packetChecksum;
        this.packetStatus = packetStatus;
    }

    public StepEntity getPacketDefinition() {
        return packetDefinition;
    }

    public long getPacketSequence() {
        return packetSequence;
    }

    public String getPacketData() {
        return packetData;
    }

    public String getPacketChecksum() {
        return packetChecksum;
    }

    public String getPacketStatus() {
        return packetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "packetDefinition", packetDefinition,
                "packetSequence", packetSequence, "packetData", packetData, "packetChecksum", packetChecksum,
                "packetStatus", packetStatus);
    }
}
