package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("packetDefinition", packetDefinition);
        state.put("packetSequence", packetSequence);
        state.put("packetData", packetData);
        state.put("packetChecksum", packetChecksum);
        state.put("packetStatus", packetStatus);
        return state;
    }
}
