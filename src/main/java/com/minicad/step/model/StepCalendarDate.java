package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CALENDAR_DATE metadata.
 *
 * @param id STEP instance id
 * @param yearComponent year value
 * @param dayComponent day of month
 * @param monthComponent month of year
 */
public final class StepCalendarDate extends AbstractStepEntity {
    private final int yearComponent;
    private final int dayComponent;
    private final int monthComponent;

    public StepCalendarDate(int id, int yearComponent, int dayComponent, int monthComponent) {
        super(id, "");
        this.yearComponent = yearComponent;
        this.dayComponent = dayComponent;
        this.monthComponent = monthComponent;
    }

    public int getYearComponent() {
        return yearComponent;
    }

    public int getDayComponent() {
        return dayComponent;
    }

    public int getMonthComponent() {
        return monthComponent;
    }

    public String getName() {
        return String.format("%04d-%02d-%02d", yearComponent, monthComponent, dayComponent);
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("yearComponent", yearComponent);
        state.put("dayComponent", dayComponent);
        state.put("monthComponent", monthComponent);
        return state;
    }
}
