package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal COORDINATED_UNIVERSAL_TIME_OFFSET metadata.
 *
 * @param id STEP instance id
 * @param hourOffset hour offset from UTC
 * @param minuteOffset optional minute offset from UTC
 * @param sense offset direction enumeration
 */
/**
 * Minimal COORDINATED_UNIVERSAL_TIME_OFFSET metadata.
 *
 * @param id STEP instance id
 * @param hourOffset hour offset from UTC
 * @param minuteOffset optional minute offset from UTC
 * @param sense offset direction enumeration
 */
public final class StepCoordinatedUniversalTimeOffset implements StepEntity {
    private final int id;
    private final int hourOffset;
    private final Integer minuteOffset;
    private final String sense;

    public StepCoordinatedUniversalTimeOffset(int id, int hourOffset, Integer minuteOffset, String sense) {
        this.id = id;
        this.hourOffset = hourOffset;
        this.minuteOffset = minuteOffset;
        this.sense = sense;
    }

    public int getId() {
        return id;
    }

    public int getHourOffset() {
        return hourOffset;
    }

    public Integer getMinuteOffset() {
        return minuteOffset;
    }

    public String getSense() {
        return sense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCoordinatedUniversalTimeOffset that = (StepCoordinatedUniversalTimeOffset) o;
        return id == that.id && hourOffset == that.hourOffset && Objects.equals(minuteOffset, that.minuteOffset) && Objects.equals(sense, that.sense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hourOffset, minuteOffset, sense);
    }

    @Override
    public String toString() {
        return "StepCoordinatedUniversalTimeOffset{" + "id=" + id + "hourOffset=" + hourOffset + "minuteOffset=" + minuteOffset + "sense=" + sense + "}";
    }
}
