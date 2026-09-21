package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CUSTOMER_INFORMATION.
 * A customer information entity.
 *
 * @param id STEP instance id
 * @param name customer name
 * @param customerId customer identifier
 * @param customerContact customer contact information
 * @param orderedProducts products ordered by customer
 * @param customerRequirements customer-specific requirements
 * @variancePriority customer variance priority level
 * @param customerStatus customer status (active, inactive)
 */
public final class StepCustomerInformation extends AbstractStepEntity {
    private final String customerId;
    private final StepEntity customerContact;
    private final List<StepEntity> orderedProducts;
    private final List<StepEntity> customerRequirements;
    private final int variancePriority;
    private final String customerStatus;

    public StepCustomerInformation(int id, String name, String customerId, StepEntity customerContact, List<StepEntity> orderedProducts, List<StepEntity> customerRequirements, int variancePriority, String customerStatus) {
        super(id, name);
        this.customerId = customerId;
        this.customerContact = customerContact;
        this.orderedProducts = orderedProducts == null ? null : java.util.List.copyOf(orderedProducts);
        this.customerRequirements = customerRequirements == null ? null : java.util.List.copyOf(customerRequirements);
        this.variancePriority = variancePriority;
        this.customerStatus = customerStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public StepEntity getCustomerContact() {
        return customerContact;
    }

    public List<StepEntity> getOrderedProducts() {
        return orderedProducts;
    }

    public List<StepEntity> getCustomerRequirements() {
        return customerRequirements;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("customerId", customerId);
        state.put("customerContact", customerContact);
        state.put("orderedProducts", orderedProducts);
        state.put("customerRequirements", customerRequirements);
        state.put("variancePriority", variancePriority);
        state.put("customerStatus", customerStatus);
        return state;
    }
}
