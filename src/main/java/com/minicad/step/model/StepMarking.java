package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MARKING.
 * Represents a marking feature in manufacturing (etching, engraving).
 *
 * @param id STEP instance id
 * @param name marking name
 * @param profile profile definition (text, symbol, etc)
 * @param depth marking depth
 * @param direction marking direction
 */
public record StepMarking(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}