package com.minicad.step.syntax;

import java.util.List;

/**
 * Generic STEP parameter value used by the syntax layer.
 */
public sealed interface StepValue permits StepValue.ReferenceValue, StepValue.NumberValue,
        StepValue.StringValue, StepValue.EnumValue, StepValue.OmittedValue, StepValue.ListValue {

    /**
     * STEP reference such as {@code #42}.
     *
     * @param id referenced entity id
     */
    record ReferenceValue(int id) implements StepValue {
    }

    /**
     * Numeric literal.
     *
     * @param value parsed floating-point value
     * @param raw original token text
     */
    record NumberValue(double value, String raw) implements StepValue {
    }

    /**
     * STEP string literal.
     *
     * @param value unescaped string value
     */
    record StringValue(String value) implements StepValue {
    }

    /**
     * STEP enumeration or logical literal such as {@code .T.}.
     *
     * @param value enumeration payload without dots
     */
    record EnumValue(String value) implements StepValue {
    }

    /**
     * Omitted parameter represented by {@code $}.
     */
    record OmittedValue() implements StepValue {
    }

    /**
     * STEP list value.
     *
     * @param elements list elements
     */
    record ListValue(List<StepValue> elements) implements StepValue {
        /**
         * Creates an immutable list value.
         */
        public ListValue {
            elements = List.copyOf(elements);
        }
    }
}
