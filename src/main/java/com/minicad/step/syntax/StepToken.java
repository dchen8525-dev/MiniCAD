package com.minicad.step.syntax;

/**
 * Single token produced by the minimal STEP tokenizer.
 *
 * @param type token kind
 * @param text source text for the token
 * @param position zero-based character position
 */
public record StepToken(StepTokenType type, String text, int position) {
}
