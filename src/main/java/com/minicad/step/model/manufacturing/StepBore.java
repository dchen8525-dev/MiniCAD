package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BORE.
 * Represents a bore feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name bore name
 * @param profile profile definition
 * @param depth bore depth
 * @param direction bore direction
 */
public record StepBore(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}