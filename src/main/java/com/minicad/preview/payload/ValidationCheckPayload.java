package com.minicad.preview.payload;

/**
 * Validation check payload for individual validation check.
 */
public final class ValidationCheckPayload {
    private final String propertyId;
    private final String name;
    private final String measureType;
    private final double expected;
    private final double actual;
    private final double delta;
    private final String status;
    private final boolean matches;

    public ValidationCheckPayload(String propertyId, String name, String measureType, double expected, double actual, double delta, String status, boolean matches) {
        this.propertyId = propertyId;
        this.name = name;
        this.measureType = measureType;
        this.expected = expected;
        this.actual = actual;
        this.delta = delta;
        this.status = status;
        this.matches = matches;
    }

    public String getPropertyId() { return propertyId; }
    public String getName() { return name; }
    public String getMeasureType() { return measureType; }
    public double getExpected() { return expected; }
    public double getActual() { return actual; }
    public double getDelta() { return delta; }
    public String getStatus() { return status; }
    public boolean getMatches() { return matches; }

    // Record-style accessors
    public String propertyId() { return propertyId; }
    public String name() { return name; }
    public String measureType() { return measureType; }
    public double expected() { return expected; }
    public double actual() { return actual; }
    public double delta() { return delta; }
    public String status() { return status; }
    public boolean matches() { return matches; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationCheckPayload)) return false;
        ValidationCheckPayload that = (ValidationCheckPayload) o;
        return java.util.Objects.equals(propertyId, that.propertyId) && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(measureType, that.measureType) && Double.compare(that.expected, expected) == 0 && Double.compare(that.actual, actual) == 0 && Double.compare(that.delta, delta) == 0 && java.util.Objects.equals(status, that.status) && matches == that.matches;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(propertyId, name, measureType, Double.hashCode(expected), Double.hashCode(actual), Double.hashCode(delta), status, matches);
    }

    @Override public String toString() {
        return "ValidationCheckPayload{" + "propertyId=propertyId, name=name, measureType=measureType, expected=expected, actual=actual, delta=delta, status=status, matches=matches" + "}";
    }
}