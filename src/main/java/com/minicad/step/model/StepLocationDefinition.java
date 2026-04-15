package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOCATION_DEFINITION.
 * A location definition entity.
 *
 * @param id STEP instance id
 * @param name location name
 * @param locationType location variance type
 * @param locationAddress location variance address
 * @param locationCoordinates location variance coordinates
 * @param locationDescription location variance description
 * @param locationStatus location variance status
 */
public record StepLocationDefinition(
    int id,
    String name,
    String locationType,
    StepEntity locationAddress,
    List<Double> locationCoordinates,
    String locationDescription,
    String locationStatus) implements StepEntity {
}