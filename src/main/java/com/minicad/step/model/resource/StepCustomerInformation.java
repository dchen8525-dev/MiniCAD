package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepCustomerInformation(
    int id,
    String name,
    String customerId,
    StepEntity customerContact,
    List<StepEntity> orderedProducts,
    List<StepEntity> customerRequirements,
    int variancePriority,
    String customerStatus) implements StepEntity {
}