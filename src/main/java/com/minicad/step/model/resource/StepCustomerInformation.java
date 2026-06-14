package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCustomerInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String customerId;
    private final StepEntity customerContact;
    private final List<StepEntity> orderedProducts;
    private final List<StepEntity> customerRequirements;
    private final int variancePriority;
    private final String customerStatus;

    public StepCustomerInformation(int id, String name, String customerId, StepEntity customerContact, List<StepEntity> orderedProducts, List<StepEntity> customerRequirements, int variancePriority, String customerStatus) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
        this.customerContact = customerContact;
        this.orderedProducts = orderedProducts == null ? null : java.util.List.copyOf(orderedProducts);
        this.customerRequirements = customerRequirements == null ? null : java.util.List.copyOf(customerRequirements);
        this.variancePriority = variancePriority;
        this.customerStatus = customerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCustomerInformation that = (StepCustomerInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(customerId, that.customerId) && Objects.equals(customerContact, that.customerContact) && Objects.equals(orderedProducts, that.orderedProducts) && Objects.equals(customerRequirements, that.customerRequirements) && variancePriority == that.variancePriority && Objects.equals(customerStatus, that.customerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customerId, customerContact, orderedProducts, customerRequirements, variancePriority, customerStatus);
    }

    @Override
    public String toString() {
        return "StepCustomerInformation{" + "id=" + id + "name=" + name + "customerId=" + customerId + "customerContact=" + customerContact + "orderedProducts=" + orderedProducts + "customerRequirements=" + customerRequirements + "variancePriority=" + variancePriority + "customerStatus=" + customerStatus + "}";
    }
}