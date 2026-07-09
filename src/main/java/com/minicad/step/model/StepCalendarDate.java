package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal CALENDAR_DATE metadata.
 *
 * @param id STEP instance id
 * @param yearComponent year value
 * @param dayComponent day of month
 * @param monthComponent month of year
 */
/**
 * Minimal CALENDAR_DATE metadata.
 *
 * @param id STEP instance id
 * @param yearComponent year value
 * @param dayComponent day of month
 * @param monthComponent month of year
 */
public final class StepCalendarDate implements StepEntity {
    private final int id;
    private final int yearComponent;
    private final int dayComponent;
    private final int monthComponent;

    public StepCalendarDate(int id, int yearComponent, int dayComponent, int monthComponent) {
        this.id = id;
        this.yearComponent = yearComponent;
        this.dayComponent = dayComponent;
        this.monthComponent = monthComponent;
    }

    public int getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalendarDate that = (StepCalendarDate) o;
        return id == that.id && yearComponent == that.yearComponent && dayComponent == that.dayComponent && monthComponent == that.monthComponent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, yearComponent, dayComponent, monthComponent);
    }

    @Override
    public String toString() {
        return "StepCalendarDate{" + "id=" + id + "yearComponent=" + yearComponent + "dayComponent=" + dayComponent + "monthComponent=" + monthComponent + "}";
    }
}
