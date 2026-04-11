package com.minicad.step.model;

/**
 * Minimal CALENDAR_DATE metadata.
 *
 * @param id STEP instance id
 * @param yearComponent year value
 * @param dayComponent day of month
 * @param monthComponent month of year
 */
public record StepCalendarDate(
        int id,
        int yearComponent,
        int dayComponent,
        int monthComponent
) implements StepEntity {

    @Override
    public String name() {
        return "%04d-%02d-%02d".formatted(yearComponent, monthComponent, dayComponent);
    }
}
