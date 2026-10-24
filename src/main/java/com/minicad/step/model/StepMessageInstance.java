package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved MESSAGE_INSTANCE.
 * A message instance entity.
 *
 * @param id STEP instance id
 * @param name message instance name
 * @param messageDefinition message variance definition reference
 * @param messageSource message variance source reference
 * @param messageDestination message variance destination reference
 * @param messagePayload message variance payload content
 * @param messageSentTime message variance sent time
 * @param messageStatus message variance status
 */
public final class StepMessageInstance extends AbstractStepEntity {
    private final StepEntity messageDefinition;
    private final StepEntity messageSource;
    private final StepEntity messageDestination;
    private final String messagePayload;
    private final StepEntity messageSentTime;
    private final String messageStatus;

    public StepMessageInstance(int id, String name, StepEntity messageDefinition, StepEntity messageSource, StepEntity messageDestination, String messagePayload, StepEntity messageSentTime, String messageStatus) {
        super(id, name);
        this.messageDefinition = messageDefinition;
        this.messageSource = messageSource;
        this.messageDestination = messageDestination;
        this.messagePayload = messagePayload;
        this.messageSentTime = messageSentTime;
        this.messageStatus = messageStatus;
    }

    public StepEntity getMessageDefinition() {
        return messageDefinition;
    }

    public StepEntity getMessageSource() {
        return messageSource;
    }

    public StepEntity getMessageDestination() {
        return messageDestination;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public StepEntity getMessageSentTime() {
        return messageSentTime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "messageDefinition", messageDefinition,
                "messageSource", messageSource, "messageDestination", messageDestination,
                "messagePayload", messagePayload, "messageSentTime", messageSentTime,
                "messageStatus", messageStatus);
    }
}
