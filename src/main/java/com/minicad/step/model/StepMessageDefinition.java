package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMessageDefinition extends AbstractStepEntity {
    private final String messageType;
    private final String messageFormat;
    private final List<String> messageFields;
    private final String messageStatus;

    public StepMessageDefinition(int id, String name, String messageType, String messageFormat, List<String> messageFields, String messageStatus) {
        super(id, name);
        this.messageType = messageType;
        this.messageFormat = messageFormat;
        this.messageFields = messageFields == null ? null : java.util.List.copyOf(messageFields);
        this.messageStatus = messageStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("messageType", messageType);
        state.put("messageFormat", messageFormat);
        state.put("messageFields", messageFields);
        state.put("messageStatus", messageStatus);
        return state;
    }
}
