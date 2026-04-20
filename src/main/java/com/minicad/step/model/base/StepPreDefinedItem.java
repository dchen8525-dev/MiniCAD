package com.minicad.step.model.base;

/**
 * Minimal PRE_DEFINED_ITEM.
 *
 * @param id step id
 * @param name predefined item name
 */
public record StepPreDefinedItem(int id, String name) implements StepEntity {
}
