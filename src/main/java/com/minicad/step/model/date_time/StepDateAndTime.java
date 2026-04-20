package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DATE_AND_TIME metadata.
 *
 * @param id STEP instance id
 * @param dateComponent calendar date
 * @param timeComponent local time
 */
public record StepDateAndTime(
        int id,
        StepCalendarDate dateComponent,
        StepLocalTime timeComponent
) implements StepEntity {

    @Override
    public String name() {
        return dateComponent.name() + " " + timeComponent.name();
    }
}
