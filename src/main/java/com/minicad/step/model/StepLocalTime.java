package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal LOCAL_TIME metadata.
 *
 * @param id STEP instance id
 * @param hourComponent hour value
 * @param minuteComponent minute value
 * @param secondComponent optional second value
 * @param zone UTC offset
 */
/**
 * Minimal LOCAL_TIME metadata.
 *
 * @param id STEP instance id
 * @param hourComponent hour value
 * @param minuteComponent minute value
 * @param secondComponent optional second value
 * @param zone UTC offset
 */
public final class StepLocalTime implements StepEntity {
    private final int id;
    private final int hourComponent;
    private final int minuteComponent;
    private final Double secondComponent;
    private final StepCoordinatedUniversalTimeOffset zone;

    public StepLocalTime(int id, int hourComponent, int minuteComponent, Double secondComponent, StepCoordinatedUniversalTimeOffset zone) {
        this.id = id;
        this.hourComponent = hourComponent;
        this.minuteComponent = minuteComponent;
        this.secondComponent = secondComponent;
        this.zone = zone;
    }

    public int getId() {
        return id;
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
    public int id() { return id; }
    public String getName() { return ""; }
    public int hourComponent() { return hourComponent; }
    public int minuteComponent() { return minuteComponent; }
    public Double secondComponent() { return secondComponent; }
    public StepCoordinatedUniversalTimeOffset zone() { return zone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLocalTime that = (StepLocalTime) o;
        return id == that.id && hourComponent == that.hourComponent && minuteComponent == that.minuteComponent && Objects.equals(secondComponent, that.secondComponent) && Objects.equals(zone, that.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hourComponent, minuteComponent, secondComponent, zone);
    }

    @Override
    public String toString() {
        return "StepLocalTime{" + "id=" + id + "hourComponent=" + hourComponent + "minuteComponent=" + minuteComponent + "secondComponent=" + secondComponent + "zone=" + zone + "}";
    }
}
