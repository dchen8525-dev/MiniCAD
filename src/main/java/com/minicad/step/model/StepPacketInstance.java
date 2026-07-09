package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPacketInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity packetDefinition;
    private final long packetSequence;
    private final String packetData;
    private final String packetChecksum;
    private final String packetStatus;

    public StepPacketInstance(int id, String name, StepEntity packetDefinition, long packetSequence, String packetData, String packetChecksum, String packetStatus) {
        this.id = id;
        this.name = name;
        this.packetDefinition = packetDefinition;
        this.packetSequence = packetSequence;
        this.packetData = packetData;
        this.packetChecksum = packetChecksum;
        this.packetStatus = packetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPacketInstance that = (StepPacketInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(packetDefinition, that.packetDefinition) && packetSequence == that.packetSequence && Objects.equals(packetData, that.packetData) && Objects.equals(packetChecksum, that.packetChecksum) && Objects.equals(packetStatus, that.packetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packetDefinition, packetSequence, packetData, packetChecksum, packetStatus);
    }

    @Override
    public String toString() {
        return "StepPacketInstance{" + "id=" + id + "name=" + name + "packetDefinition=" + packetDefinition + "packetSequence=" + packetSequence + "packetData=" + packetData + "packetChecksum=" + packetChecksum + "packetStatus=" + packetStatus + "}";
    }
}