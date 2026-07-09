package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMessageInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity messageDefinition;
    private final StepEntity messageSource;
    private final StepEntity messageDestination;
    private final String messagePayload;
    private final StepEntity messageSentTime;
    private final String messageStatus;

    public StepMessageInstance(int id, String name, StepEntity messageDefinition, StepEntity messageSource, StepEntity messageDestination, String messagePayload, StepEntity messageSentTime, String messageStatus) {
        this.id = id;
        this.name = name;
        this.messageDefinition = messageDefinition;
        this.messageSource = messageSource;
        this.messageDestination = messageDestination;
        this.messagePayload = messagePayload;
        this.messageSentTime = messageSentTime;
        this.messageStatus = messageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMessageInstance that = (StepMessageInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(messageDefinition, that.messageDefinition) && Objects.equals(messageSource, that.messageSource) && Objects.equals(messageDestination, that.messageDestination) && Objects.equals(messagePayload, that.messagePayload) && Objects.equals(messageSentTime, that.messageSentTime) && Objects.equals(messageStatus, that.messageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, messageDefinition, messageSource, messageDestination, messagePayload, messageSentTime, messageStatus);
    }

    @Override
    public String toString() {
        return "StepMessageInstance{" + "id=" + id + "name=" + name + "messageDefinition=" + messageDefinition + "messageSource=" + messageSource + "messageDestination=" + messageDestination + "messagePayload=" + messagePayload + "messageSentTime=" + messageSentTime + "messageStatus=" + messageStatus + "}";
    }
}