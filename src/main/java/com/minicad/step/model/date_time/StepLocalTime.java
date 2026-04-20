package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal LOCAL_TIME metadata.
 *
 * @param id STEP instance id
 * @param hourComponent hour value
 * @param minuteComponent minute value
 * @param secondComponent optional second value
 * @param zone UTC offset
 */
public record StepLocalTime(
        int id,
        int hourComponent,
        int minuteComponent,
        Double secondComponent,
        StepCoordinatedUniversalTimeOffset zone
) implements StepEntity {

    @Override
    public String name() {
        if (secondComponent == null) {
            return "%02d:%02d".formatted(hourComponent, minuteComponent);
        }
        return "%02d:%02d:%s".formatted(hourComponent, minuteComponent, secondComponent);
    }
}
