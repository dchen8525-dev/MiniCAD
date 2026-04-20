package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal COORDINATED_UNIVERSAL_TIME_OFFSET metadata.
 *
 * @param id STEP instance id
 * @param hourOffset hour offset from UTC
 * @param minuteOffset optional minute offset from UTC
 * @param sense offset direction enumeration
 */
public record StepCoordinatedUniversalTimeOffset(
        int id,
        int hourOffset,
        Integer minuteOffset,
        String sense
) implements StepEntity {

    @Override
    public String name() {
        String minutes = minuteOffset == null ? "" : ":" + "%02d".formatted(minuteOffset);
        return hourOffset + minutes + " " + sense;
    }
}
