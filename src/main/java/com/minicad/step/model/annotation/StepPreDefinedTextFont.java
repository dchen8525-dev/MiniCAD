package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined text font name
 */
public record StepPreDefinedTextFont(int id, String name) implements StepEntity {
}
