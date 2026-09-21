package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMMAND_DEFINITION.
 * A command definition entity.
 *
 * @param id STEP instance id
 * @param name command name
 * @param commandType command variance type
 * @param commandDescription command variance description
 * @param commandParameters command variance parameters
 * @param commandReturnType command variance return type
 * @param commandStatus command variance status
 */
public final class StepCommandDefinition extends AbstractStepEntity {
    private final String commandType;
    private final String commandDescription;
    private final List<String> commandParameters;
    private final String commandReturnType;
    private final String commandStatus;

    public StepCommandDefinition(int id, String name, String commandType, String commandDescription, List<String> commandParameters, String commandReturnType, String commandStatus) {
        super(id, name);
        this.commandType = commandType;
        this.commandDescription = commandDescription;
        this.commandParameters = commandParameters == null ? null : java.util.List.copyOf(commandParameters);
        this.commandReturnType = commandReturnType;
        this.commandStatus = commandStatus;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public List<String> getCommandParameters() {
        return commandParameters;
    }

    public String getCommandReturnType() {
        return commandReturnType;
    }

    public String getCommandStatus() {
        return commandStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("commandType", commandType);
        state.put("commandDescription", commandDescription);
        state.put("commandParameters", commandParameters);
        state.put("commandReturnType", commandReturnType);
        state.put("commandStatus", commandStatus);
        return state;
    }
}
