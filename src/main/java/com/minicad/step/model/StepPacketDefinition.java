package com.minicad.step.model;

import java.util.List;

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
public record StepPacketDefinition(
    int id,
    String name,
    String packetType,
    String packetFormat,
    int packetSize,
    String packetHeader,
    String packetStatus) implements StepEntity {
}