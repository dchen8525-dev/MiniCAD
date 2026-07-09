package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCommandInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity commandDefinition;
    private final String commandState;
    private final List<String> commandParameters;
    private final String commandResult;
    private final String commandStatus;

    public StepCommandInstance(int id, String name, StepEntity commandDefinition, String commandState, List<String> commandParameters, String commandResult, String commandStatus) {
        this.id = id;
        this.name = name;
        this.commandDefinition = commandDefinition;
        this.commandState = commandState;
        this.commandParameters = commandParameters == null ? null : java.util.List.copyOf(commandParameters);
        this.commandResult = commandResult;
        this.commandStatus = commandStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCommandInstance that = (StepCommandInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(commandDefinition, that.commandDefinition) && Objects.equals(commandState, that.commandState) && Objects.equals(commandParameters, that.commandParameters) && Objects.equals(commandResult, that.commandResult) && Objects.equals(commandStatus, that.commandStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, commandDefinition, commandState, commandParameters, commandResult, commandStatus);
    }

    @Override
    public String toString() {
        return "StepCommandInstance{" + "id=" + id + "name=" + name + "commandDefinition=" + commandDefinition + "commandState=" + commandState + "commandParameters=" + commandParameters + "commandResult=" + commandResult + "commandStatus=" + commandStatus + "}";
    }
}