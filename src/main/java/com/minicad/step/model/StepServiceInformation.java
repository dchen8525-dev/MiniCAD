package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SERVICE_INFORMATION.
 * A service information entity.
 *
 * @param id STEP instance id
 * @param name service name
 * @param serviceType service type (repair, replacement, calibration)
 * @param serviceItem item being serviced
 * @param serviceProvider service provider reference
 * @varianceDate service variance date
 * @param serviceCost service cost
 * @param serviceStatus service status
 * @param serviceNotes service notes/comments
 */
/**
 * Resolved SERVICE_INFORMATION.
 * A service information entity.
 *
 * @param id STEP instance id
 * @param name service name
 * @param serviceType service type (repair, replacement, calibration)
 * @param serviceItem item being serviced
 * @param serviceProvider service provider reference
 * @varianceDate service variance date
 * @param serviceCost service cost
 * @param serviceStatus service status
 * @param serviceNotes service notes/comments
 */
public final class StepServiceInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String serviceType;
    private final StepEntity serviceItem;
    private final StepEntity serviceProvider;
    private final StepEntity varianceDate;
    private final double serviceCost;
    private final String serviceStatus;
    private final String serviceNotes;

    public StepServiceInformation(int id, String name, String serviceType, StepEntity serviceItem, StepEntity serviceProvider, StepEntity varianceDate, double serviceCost, String serviceStatus, String serviceNotes) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.serviceItem = serviceItem;
        this.serviceProvider = serviceProvider;
        this.varianceDate = varianceDate;
        this.serviceCost = serviceCost;
        this.serviceStatus = serviceStatus;
        this.serviceNotes = serviceNotes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public StepEntity getServiceItem() {
        return serviceItem;
    }

    public StepEntity getServiceProvider() {
        return serviceProvider;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public String getServiceNotes() {
        return serviceNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepServiceInformation that = (StepServiceInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(serviceType, that.serviceType) && Objects.equals(serviceItem, that.serviceItem) && Objects.equals(serviceProvider, that.serviceProvider) && Objects.equals(varianceDate, that.varianceDate) && serviceCost == that.serviceCost && Objects.equals(serviceStatus, that.serviceStatus) && Objects.equals(serviceNotes, that.serviceNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, serviceType, serviceItem, serviceProvider, varianceDate, serviceCost, serviceStatus, serviceNotes);
    }

    @Override
    public String toString() {
        return "StepServiceInformation{" + "id=" + id + "name=" + name + "serviceType=" + serviceType + "serviceItem=" + serviceItem + "serviceProvider=" + serviceProvider + "varianceDate=" + varianceDate + "serviceCost=" + serviceCost + "serviceStatus=" + serviceStatus + "serviceNotes=" + serviceNotes + "}";
    }
}