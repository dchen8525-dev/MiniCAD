package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DATE_AND_TIME metadata.
 *
 * @param id STEP instance id
 * @param dateComponent calendar date
 * @param timeComponent local time
 */
public final class StepDateAndTime extends AbstractStepEntity {
    private final StepCalendarDate dateComponent;
    private final StepLocalTime timeComponent;

    public StepDateAndTime(int id, StepCalendarDate dateComponent, StepLocalTime timeComponent) {
        super(id, "");
        this.dateComponent = dateComponent;
        this.timeComponent = timeComponent;
    }

    public StepCalendarDate getDateComponent() {
        return dateComponent;
    }

    public StepLocalTime getTimeComponent() {
        return timeComponent;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String getName() {
        if (dateComponent == null && timeComponent == null) {
            return "";
        }
        String datePart = dateComponent != null ? dateComponent.getName() : "";
        String timePart = timeComponent != null ? formatTime(timeComponent) : "";
        if (datePart.isEmpty()) {
            return timePart;
        }
        if (timePart.isEmpty()) {
            return datePart;
        }
        return datePart + " " + timePart;
    }
    private static String formatTime(StepLocalTime time) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d:%02d", time.getHourComponent(), time.getMinuteComponent()));
        if (time.getSecondComponent() != null) {
            double seconds = time.getSecondComponent();
            if (seconds == Math.floor(seconds)) {
                sb.append(String.format(":%02d", (int) seconds));
            } else {
                sb.append(String.format(":%s", trimTrailingZeros(seconds)));
            }
        }
        return sb.toString();
    }
    private static String trimTrailingZeros(double value) {
        String s = Double.toString(value);
        return s;
    }

    public StepCalendarDate dateComponent() { return dateComponent; }
    public StepLocalTime timeComponent() { return timeComponent; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("dateComponent", dateComponent);
        state.put("timeComponent", timeComponent);
        return state;
    }
}
