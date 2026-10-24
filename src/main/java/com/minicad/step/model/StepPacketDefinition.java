package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved PACKET_DEFINITION.
 * A packet definition entity.
 *
 * @param id STEP instance id
 * @param name packet name
 * @param packetType packet variance type
 * @param packetFormat packet variance format
 * @param packetSize packet variance size
 * @param packetHeader packet variance header format
 * @param packetStatus packet variance status
 */
public final class StepPacketDefinition extends AbstractStepEntity {
    private final String packetType;
    private final String packetFormat;
    private final int packetSize;
    private final String packetHeader;
    private final String packetStatus;

    public StepPacketDefinition(int id, String name, String packetType, String packetFormat, int packetSize, String packetHeader, String packetStatus) {
        super(id, name);
        this.packetType = packetType;
        this.packetFormat = packetFormat;
        this.packetSize = packetSize;
        this.packetHeader = packetHeader;
        this.packetStatus = packetStatus;
    }

    public String getPacketType() {
        return packetType;
    }

    public String getPacketFormat() {
        return packetFormat;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    public String getPacketStatus() {
        return packetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "packetType", packetType, "packetFormat", packetFormat,
                "packetSize", packetSize, "packetHeader", packetHeader, "packetStatus", packetStatus);
    }
}
