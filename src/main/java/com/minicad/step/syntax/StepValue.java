package com.minicad.step.syntax;

import java.util.List;
import java.util.Objects;

/**
 * Generic STEP parameter value used by the syntax layer.
 */
public interface StepValue {

    /**
     * STEP reference such as {@code #42}.
     */
    static final class ReferenceValue implements StepValue {
        private final int id;

        public ReferenceValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReferenceValue that = (ReferenceValue) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "ReferenceValue{id=" + id + "}";
        }
    }

    /**
     * Numeric literal.
     */
    static final class NumberValue implements StepValue {
        private final double value;
        private final String raw;

        public NumberValue(double value, String raw) {
            this.value = value;
            this.raw = raw;
        }

        public double getValue() {
            return value;
        }

        public String getRaw() {
            return raw;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NumberValue that = (NumberValue) o;
            return Double.compare(that.value, value) == 0 && Objects.equals(raw, that.raw);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, raw);
        }

        @Override
        public String toString() {
            return "NumberValue{value=" + value + ", raw='" + raw + "'}";
        }
    }

    /**
     * STEP string literal.
     */
    static final class StringValue implements StepValue {
        private final String value;

        public StringValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StringValue that = (StringValue) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "StringValue{value='" + value + "'}";
        }
    }

    /**
     * STEP enumeration or logical literal such as {@code .T.}.
     */
    static final class EnumValue implements StepValue {
        private final String value;

        public EnumValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EnumValue that = (EnumValue) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "EnumValue{value='" + value + "'}";
        }
    }

    /**
     * Typed STEP value such as {@code LENGTH_MEASURE(1.0)}.
     */
    static final class TypedValue implements StepValue {
        private final String typeName;
        private final StepValue value;

        public TypedValue(String typeName, StepValue value) {
            this.typeName = typeName;
            this.value = value;
        }

        public String getTypeName() {
            return typeName;
        }

        public StepValue getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypedValue that = (TypedValue) o;
            return Objects.equals(typeName, that.typeName) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeName, value);
        }

        @Override
        public String toString() {
            return "TypedValue{typeName='" + typeName + "', value=" + value + "}";
        }
    }

    /**
     * Omitted parameter represented by {@code $}.
     */
    static final class OmittedValue implements StepValue {

        public OmittedValue() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o != null && getClass() == o.getClass();
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "OmittedValue{}";
        }
    }

    /**
     * Not-provided parameter represented by {@code *}.
     */
    static final class NotProvidedValue implements StepValue {

        public NotProvidedValue() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o != null && getClass() == o.getClass();
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return "NotProvidedValue{}";
        }
    }

    /**
     * STEP list value.
     */
    static final class ListValue implements StepValue {
        private final List<StepValue> elements;

        /**
         * Creates an immutable list value.
         */
        public ListValue(List<StepValue> elements) {
            this.elements = List.copyOf(elements);
        }

        public List<StepValue> getElements() {
            return elements;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListValue that = (ListValue) o;
            return Objects.equals(elements, that.elements);
        }

        @Override
        public int hashCode() {
            return elements.hashCode();
        }

        @Override
        public String toString() {
            return "ListValue{elements=" + elements + "}";
        }
    }
}