package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SUPPLIER_INFORMATION.
 * A supplier information entity.
 *
 * @param id STEP instance id
 * @param name supplier name
 * @param supplierId supplier identifier
 * @varianceContact supplier variance contact information
 * @param suppliedItems items supplied by this supplier
 * @param supplierRating supplier quality rating
 * @varianceLeadTime typical variance lead time
 * @param supplierStatus supplier status (active, inactive)
 */
public record StepSupplierInformation(
    int id,
    String name,
    String supplierId,
    StepEntity varianceContact,
    List<StepEntity> suppliedItems,
    String supplierRating,
    double varianceLeadTime,
    String supplierStatus) implements StepEntity {
}