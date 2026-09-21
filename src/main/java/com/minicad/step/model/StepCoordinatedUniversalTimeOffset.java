package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal COORDINATED_UNIVERSAL_TIME_OFFSET metadata.
 *
 * @param id STEP instance id
 * @param hourOffset hour offset from UTC
 * @param minuteOffset optional minute offset from UTC
 * @param sense offset direction enumeration
 */
public final class StepCoordinatedUniversalTimeOffset extends AbstractStepEntity {
    private final int hourOffset;
    private final Integer minuteOffset;
    private final String sense;

    public StepCoordinatedUniversalTimeOffset(int id, int hourOffset, Integer minuteOffset, String sense) {
        super(id, "");
        this.hourOffset = hourOffset;
        this.minuteOffset = minuteOffset;
        this.sense = sense;
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

    // Record-style accessors
    public int id() { return getId(); }
    public int hourOffset() { return hourOffset; }
    public Integer minuteOffset() { return minuteOffset; }
    public String sense() { return sense; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("hourOffset", hourOffset);
        state.put("minuteOffset", minuteOffset);
        state.put("sense", sense);
        return state;
    }
}
