package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSupplierInformation extends AbstractStepEntity {
    private final String supplierId;
    private final StepEntity varianceContact;
    private final List<StepEntity> suppliedItems;
    private final String supplierRating;
    private final double varianceLeadTime;
    private final String supplierStatus;

    public StepSupplierInformation(int id, String name, String supplierId, StepEntity varianceContact, List<StepEntity> suppliedItems, String supplierRating, double varianceLeadTime, String supplierStatus) {
        super(id, name);
        this.supplierId = supplierId;
        this.varianceContact = varianceContact;
        this.suppliedItems = suppliedItems == null ? null : java.util.List.copyOf(suppliedItems);
        this.supplierRating = supplierRating;
        this.varianceLeadTime = varianceLeadTime;
        this.supplierStatus = supplierStatus;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public StepEntity getVarianceContact() {
        return varianceContact;
    }

    public List<StepEntity> getSuppliedItems() {
        return suppliedItems;
    }

    public String getSupplierRating() {
        return supplierRating;
    }

    public double getVarianceLeadTime() {
        return varianceLeadTime;
    }

    public String getSupplierStatus() {
        return supplierStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "supplierId", supplierId,
                "varianceContact", varianceContact, "suppliedItems", suppliedItems,
                "supplierRating", supplierRating, "varianceLeadTime", varianceLeadTime,
                "supplierStatus", supplierStatus);
    }
}
