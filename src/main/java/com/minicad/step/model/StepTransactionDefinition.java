package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRANSACTION_DEFINITION.
 * A transaction definition entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction variance type
 * @param transactionIsolation transaction variance isolation level
 * @param transactionTimeout transaction variance timeout
 * @param transactionOperations transaction variance operations
 * @param transactionStatus transaction variance status
 */
/**
 * Resolved TRANSACTION_DEFINITION.
 * A transaction definition entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction variance type
 * @param transactionIsolation transaction variance isolation level
 * @param transactionTimeout transaction variance timeout
 * @param transactionOperations transaction variance operations
 * @param transactionStatus transaction variance status
 */
public final class StepTransactionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String transactionType;
    private final String transactionIsolation;
    private final int transactionTimeout;
    private final List<String> transactionOperations;
    private final String transactionStatus;

    public StepTransactionDefinition(int id, String name, String transactionType, String transactionIsolation, int transactionTimeout, List<String> transactionOperations, String transactionStatus) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.transactionIsolation = transactionIsolation;
        this.transactionTimeout = transactionTimeout;
        this.transactionOperations = transactionOperations == null ? null : java.util.List.copyOf(transactionOperations);
        this.transactionStatus = transactionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionIsolation() {
        return transactionIsolation;
    }

    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    public List<String> getTransactionOperations() {
        return transactionOperations;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransactionDefinition that = (StepTransactionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transactionType, that.transactionType) && Objects.equals(transactionIsolation, that.transactionIsolation) && transactionTimeout == that.transactionTimeout && Objects.equals(transactionOperations, that.transactionOperations) && Objects.equals(transactionStatus, that.transactionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transactionType, transactionIsolation, transactionTimeout, transactionOperations, transactionStatus);
    }

    @Override
    public String toString() {
        return "StepTransactionDefinition{" + "id=" + id + "name=" + name + "transactionType=" + transactionType + "transactionIsolation=" + transactionIsolation + "transactionTimeout=" + transactionTimeout + "transactionOperations=" + transactionOperations + "transactionStatus=" + transactionStatus + "}";
    }
}