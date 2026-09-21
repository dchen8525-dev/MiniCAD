package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal LOCAL_TIME metadata.
 *
 * @param id STEP instance id
 * @param hourComponent hour value
 * @param minuteComponent minute value
 * @param secondComponent optional second value
 * @param zone UTC offset
 */
public final class StepLocalTime extends AbstractStepEntity {
    private final int hourComponent;
    private final int minuteComponent;
    private final Double secondComponent;
    private final StepCoordinatedUniversalTimeOffset zone;

    public StepLocalTime(int id, int hourComponent, int minuteComponent, Double secondComponent, StepCoordinatedUniversalTimeOffset zone) {
        super(id, "");
        this.hourComponent = hourComponent;
        this.minuteComponent = minuteComponent;
        this.secondComponent = secondComponent;
        this.zone = zone;
    }

    public int getHourComponent() {
        return hourComponent;
    }

    public int getMinuteComponent() {
        return minuteComponent;
    }

    public Double getSecondComponent() {
        return secondComponent;
    }

    public StepCoordinatedUniversalTimeOffset getZone() {
        return zone;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public int hourComponent() { return hourComponent; }
    public int minuteComponent() { return minuteComponent; }
    public Double secondComponent() { return secondComponent; }
    public StepCoordinatedUniversalTimeOffset zone() { return zone; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("hourComponent", hourComponent);
        state.put("minuteComponent", minuteComponent);
        state.put("secondComponent", secondComponent);
        state.put("zone", zone);
        return state;
    }
}
