package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT.
 */
/**
 * Resolved TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT.
 */
public final class StepTextLiteralWithDraughtingCallout implements StepEntity {
    private final int id;
    private final String name;
    private final String textLiteral;
    private final StepEntity callout;

    public StepTextLiteralWithDraughtingCallout(int id, String name, String textLiteral, StepEntity callout) {
        this.id = id;
        this.name = name;
        this.textLiteral = textLiteral;
        this.callout = callout;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTextLiteral() {
        return textLiteral;
    }

    public StepEntity getCallout() {
        return callout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextLiteralWithDraughtingCallout that = (StepTextLiteralWithDraughtingCallout) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(textLiteral, that.textLiteral) && Objects.equals(callout, that.callout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, textLiteral, callout);
    }

    @Override
    public String toString() {
        return "StepTextLiteralWithDraughtingCallout{" + "id=" + id + "name=" + name + "textLiteral=" + textLiteral + "callout=" + callout + "}";
    }
}
