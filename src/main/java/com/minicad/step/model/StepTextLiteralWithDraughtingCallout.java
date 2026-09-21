package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT.
 */
public final class StepTextLiteralWithDraughtingCallout extends AbstractStepEntity {
    private final String textLiteral;
    private final StepEntity callout;

    public StepTextLiteralWithDraughtingCallout(int id, String name, String textLiteral, StepEntity callout) {
        super(id, name);
        this.textLiteral = textLiteral;
        this.callout = callout;
    }

    public String getTextLiteral() {
        return textLiteral;
    }

    public StepEntity getCallout() {
        return callout;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("textLiteral", textLiteral);
        state.put("callout", callout);
        return state;
    }
}
