package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTransactionDefinition extends AbstractStepEntity {
    private final String transactionType;
    private final String transactionIsolation;
    private final int transactionTimeout;
    private final List<String> transactionOperations;
    private final String transactionStatus;

    public StepTransactionDefinition(int id, String name, String transactionType, String transactionIsolation, int transactionTimeout, List<String> transactionOperations, String transactionStatus) {
        super(id, name);
        this.transactionType = transactionType;
        this.transactionIsolation = transactionIsolation;
        this.transactionTimeout = transactionTimeout;
        this.transactionOperations = transactionOperations == null ? null : java.util.List.copyOf(transactionOperations);
        this.transactionStatus = transactionStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transactionType", transactionType);
        state.put("transactionIsolation", transactionIsolation);
        state.put("transactionTimeout", transactionTimeout);
        state.put("transactionOperations", transactionOperations);
        state.put("transactionStatus", transactionStatus);
        return state;
    }
}
