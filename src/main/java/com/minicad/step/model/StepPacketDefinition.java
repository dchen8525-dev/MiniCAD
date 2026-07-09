package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPacketDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String packetType;
    private final String packetFormat;
    private final int packetSize;
    private final String packetHeader;
    private final String packetStatus;

    public StepPacketDefinition(int id, String name, String packetType, String packetFormat, int packetSize, String packetHeader, String packetStatus) {
        this.id = id;
        this.name = name;
        this.packetType = packetType;
        this.packetFormat = packetFormat;
        this.packetSize = packetSize;
        this.packetHeader = packetHeader;
        this.packetStatus = packetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPacketDefinition that = (StepPacketDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(packetType, that.packetType) && Objects.equals(packetFormat, that.packetFormat) && packetSize == that.packetSize && Objects.equals(packetHeader, that.packetHeader) && Objects.equals(packetStatus, that.packetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packetType, packetFormat, packetSize, packetHeader, packetStatus);
    }

    @Override
    public String toString() {
        return "StepPacketDefinition{" + "id=" + id + "name=" + name + "packetType=" + packetType + "packetFormat=" + packetFormat + "packetSize=" + packetSize + "packetHeader=" + packetHeader + "packetStatus=" + packetStatus + "}";
    }
}