package com.minicad.step.syntax;

import java.util.Objects;

/**
 * Single token produced by the minimal STEP tokenizer.
 *
 * @param type token kind
 * @param text source text for the token
 * @param position zero-based character position
 */
/**
 * Single token produced by the minimal STEP tokenizer.
 *
 * @param type token kind
 * @param text source text for the token
 * @param position zero-based character position
 */
public final class StepToken {
    private final StepTokenType type;
    private final String text;
    private final int position;

    public StepToken(StepTokenType type, String text, int position) {
        this.type = type;
        this.text = text;
        this.position = position;
    }

    public StepTokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    // Record-style accessors
    public StepTokenType type() {
        return type;
    }

    public String text() {
        return text;
    }

    public int position() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToken that = (StepToken) o;
        return Objects.equals(type, that.type) && Objects.equals(text, that.text) && position == that.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text, position);
    }

    @Override
    public String toString() {
        return "StepToken{" + "type=" + type + "text=" + text + "position=" + position + "}";
    }
}
