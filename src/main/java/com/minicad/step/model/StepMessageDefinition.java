package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MESSAGE_DEFINITION.
 * A message definition entity.
 *
 * @param id STEP instance id
 * @param name message name
 * @param messageType message variance type
 * @param messageFormat message variance format
 * @param messageFields message variance field definitions
 * @param messageStatus message variance status
 */
/**
 * Resolved MESSAGE_DEFINITION.
 * A message definition entity.
 *
 * @param id STEP instance id
 * @param name message name
 * @param messageType message variance type
 * @param messageFormat message variance format
 * @param messageFields message variance field definitions
 * @param messageStatus message variance status
 */
public final class StepMessageDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String messageType;
    private final String messageFormat;
    private final List<String> messageFields;
    private final String messageStatus;

    public StepMessageDefinition(int id, String name, String messageType, String messageFormat, List<String> messageFields, String messageStatus) {
        this.id = id;
        this.name = name;
        this.messageType = messageType;
        this.messageFormat = messageFormat;
        this.messageFields = messageFields == null ? null : java.util.List.copyOf(messageFields);
        this.messageStatus = messageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public List<String> getMessageFields() {
        return messageFields;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMessageDefinition that = (StepMessageDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(messageType, that.messageType) && Objects.equals(messageFormat, that.messageFormat) && Objects.equals(messageFields, that.messageFields) && Objects.equals(messageStatus, that.messageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, messageType, messageFormat, messageFields, messageStatus);
    }

    @Override
    public String toString() {
        return "StepMessageDefinition{" + "id=" + id + "name=" + name + "messageType=" + messageType + "messageFormat=" + messageFormat + "messageFields=" + messageFields + "messageStatus=" + messageStatus + "}";
    }
}