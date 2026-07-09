package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCommandDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String commandType;
    private final String commandDescription;
    private final List<String> commandParameters;
    private final String commandReturnType;
    private final String commandStatus;

    public StepCommandDefinition(int id, String name, String commandType, String commandDescription, List<String> commandParameters, String commandReturnType, String commandStatus) {
        this.id = id;
        this.name = name;
        this.commandType = commandType;
        this.commandDescription = commandDescription;
        this.commandParameters = commandParameters == null ? null : java.util.List.copyOf(commandParameters);
        this.commandReturnType = commandReturnType;
        this.commandStatus = commandStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCommandDefinition that = (StepCommandDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(commandType, that.commandType) && Objects.equals(commandDescription, that.commandDescription) && Objects.equals(commandParameters, that.commandParameters) && Objects.equals(commandReturnType, that.commandReturnType) && Objects.equals(commandStatus, that.commandStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, commandType, commandDescription, commandParameters, commandReturnType, commandStatus);
    }

    @Override
    public String toString() {
        return "StepCommandDefinition{" + "id=" + id + "name=" + name + "commandType=" + commandType + "commandDescription=" + commandDescription + "commandParameters=" + commandParameters + "commandReturnType=" + commandReturnType + "commandStatus=" + commandStatus + "}";
    }
}