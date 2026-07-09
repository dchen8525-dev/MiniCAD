package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TIMER_DEFINITION.
 * A timer definition entity.
 *
 * @param id STEP instance id
 * @param name timer name
 * @param timerType timer variance type
 * @param timerDuration timer variance duration
 * @param timerAction timer variance action reference
 * @param timerRecurring timer variance recurring flag
 * @param timerStatus timer variance status
 */
/**
 * Resolved TIMER_DEFINITION.
 * A timer definition entity.
 *
 * @param id STEP instance id
 * @param name timer name
 * @param timerType timer variance type
 * @param timerDuration timer variance duration
 * @param timerAction timer variance action reference
 * @param timerRecurring timer variance recurring flag
 * @param timerStatus timer variance status
 */
public final class StepTimerDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String timerType;
    private final int timerDuration;
    private final StepEntity timerAction;
    private final boolean timerRecurring;
    private final String timerStatus;

    public StepTimerDefinition(int id, String name, String timerType, int timerDuration, StepEntity timerAction, boolean timerRecurring, String timerStatus) {
        this.id = id;
        this.name = name;
        this.timerType = timerType;
        this.timerDuration = timerDuration;
        this.timerAction = timerAction;
        this.timerRecurring = timerRecurring;
        this.timerStatus = timerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimerType() {
        return timerType;
    }

    public int getTimerDuration() {
        return timerDuration;
    }

    public StepEntity getTimerAction() {
        return timerAction;
    }

    public boolean isTimerRecurring() {
        return timerRecurring;
    }

    public String getTimerStatus() {
        return timerStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTimerDefinition that = (StepTimerDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(timerType, that.timerType) && timerDuration == that.timerDuration && Objects.equals(timerAction, that.timerAction) && timerRecurring == that.timerRecurring && Objects.equals(timerStatus, that.timerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, timerType, timerDuration, timerAction, timerRecurring, timerStatus);
    }

    @Override
    public String toString() {
        return "StepTimerDefinition{" + "id=" + id + "name=" + name + "timerType=" + timerType + "timerDuration=" + timerDuration + "timerAction=" + timerAction + "timerRecurring=" + timerRecurring + "timerStatus=" + timerStatus + "}";
    }
}