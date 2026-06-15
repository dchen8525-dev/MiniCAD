package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DATE_AND_TIME metadata.
 *
 * @param id STEP instance id
 * @param dateComponent calendar date
 * @param timeComponent local time
 */
/**
 * Minimal DATE_AND_TIME metadata.
 *
 * @param id STEP instance id
 * @param dateComponent calendar date
 * @param timeComponent local time
 */
public final class StepDateAndTime implements StepEntity {
    private final int id;
    private final StepCalendarDate dateComponent;
    private final StepLocalTime timeComponent;

    public StepDateAndTime(int id, StepCalendarDate dateComponent, StepLocalTime timeComponent) {
        this.id = id;
        this.dateComponent = dateComponent;
        this.timeComponent = timeComponent;
    }

    public int getId() {
        return id;
    }

    public StepCalendarDate getDateComponent() {
        return dateComponent;
    }

    public StepLocalTime getTimeComponent() {
        return timeComponent;
    }

    // Record-style accessors
    public int id() { return id; }
    public String getName() { return ""; }
    public StepCalendarDate dateComponent() { return dateComponent; }
    public StepLocalTime timeComponent() { return timeComponent; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDateAndTime that = (StepDateAndTime) o;
        return id == that.id && Objects.equals(dateComponent, that.dateComponent) && Objects.equals(timeComponent, that.timeComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateComponent, timeComponent);
    }

    @Override
    public String toString() {
        return "StepDateAndTime{" + "id=" + id + "dateComponent=" + dateComponent + "timeComponent=" + timeComponent + "}";
    }
}
