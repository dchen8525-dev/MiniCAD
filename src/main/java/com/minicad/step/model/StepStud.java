package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STUD.
 * Represents a stud/protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name stud name
 * @param profile profile definition
 * @param height stud height
 * @param direction stud direction
 */
public record StepStud(
    int id,
    String name,
    StepEntity profile,
    Double height,
    StepEntity direction) implements StepEntity {
}