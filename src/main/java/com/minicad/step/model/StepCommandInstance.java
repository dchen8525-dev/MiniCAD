package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMMAND_INSTANCE.
 * A command instance entity.
 *
 * @param id STEP instance id
 * @param name command instance name
 * @param commandDefinition command variance definition reference
 * @param commandState command variance state
 * @param commandParameters command variance parameter values
 * @param commandResult command variance result
 * @param commandStatus command variance status
 */
public final class StepCommandInstance extends AbstractStepEntity {
    private final StepEntity commandDefinition;
    private final String commandState;
    private final List<String> commandParameters;
    private final String commandResult;
    private final String commandStatus;

    public StepCommandInstance(int id, String name, StepEntity commandDefinition, String commandState, List<String> commandParameters, String commandResult, String commandStatus) {
        super(id, name);
        this.commandDefinition = commandDefinition;
        this.commandState = commandState;
        this.commandParameters = commandParameters == null ? null : java.util.List.copyOf(commandParameters);
        this.commandResult = commandResult;
        this.commandStatus = commandStatus;
    }

    public StepEntity getCommandDefinition() {
        return commandDefinition;
    }

    public String getCommandState() {
        return commandState;
    }

    public List<String> getCommandParameters() {
        return commandParameters;
    }

    public String getCommandResult() {
        return commandResult;
    }

    public String getCommandStatus() {
        return commandStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("commandDefinition", commandDefinition);
        state.put("commandState", commandState);
        state.put("commandParameters", commandParameters);
        state.put("commandResult", commandResult);
        state.put("commandStatus", commandStatus);
        return state;
    }
}
