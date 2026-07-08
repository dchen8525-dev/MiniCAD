package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSupplierInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String supplierId;
    private final StepEntity varianceContact;
    private final List<StepEntity> suppliedItems;
    private final String supplierRating;
    private final double varianceLeadTime;
    private final String supplierStatus;

    public StepSupplierInformation(int id, String name, String supplierId, StepEntity varianceContact, List<StepEntity> suppliedItems, String supplierRating, double varianceLeadTime, String supplierStatus) {
        this.id = id;
        this.name = name;
        this.supplierId = supplierId;
        this.varianceContact = varianceContact;
        this.suppliedItems = suppliedItems == null ? null : java.util.List.copyOf(suppliedItems);
        this.supplierRating = supplierRating;
        this.varianceLeadTime = varianceLeadTime;
        this.supplierStatus = supplierStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSupplierInformation that = (StepSupplierInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(supplierId, that.supplierId) && Objects.equals(varianceContact, that.varianceContact) && Objects.equals(suppliedItems, that.suppliedItems) && Objects.equals(supplierRating, that.supplierRating) && varianceLeadTime == that.varianceLeadTime && Objects.equals(supplierStatus, that.supplierStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, supplierId, varianceContact, suppliedItems, supplierRating, varianceLeadTime, supplierStatus);
    }

    @Override
    public String toString() {
        return "StepSupplierInformation{" + "id=" + id + "name=" + name + "supplierId=" + supplierId + "varianceContact=" + varianceContact + "suppliedItems=" + suppliedItems + "supplierRating=" + supplierRating + "varianceLeadTime=" + varianceLeadTime + "supplierStatus=" + supplierStatus + "}";
    }
}