package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepServiceInformation extends AbstractStepEntity {
    private final String serviceType;
    private final StepEntity serviceItem;
    private final StepEntity serviceProvider;
    private final StepEntity varianceDate;
    private final double serviceCost;
    private final String serviceStatus;
    private final String serviceNotes;

    public StepServiceInformation(int id, String name, String serviceType, StepEntity serviceItem, StepEntity serviceProvider, StepEntity varianceDate, double serviceCost, String serviceStatus, String serviceNotes) {
        super(id, name);
        this.serviceType = serviceType;
        this.serviceItem = serviceItem;
        this.serviceProvider = serviceProvider;
        this.varianceDate = varianceDate;
        this.serviceCost = serviceCost;
        this.serviceStatus = serviceStatus;
        this.serviceNotes = serviceNotes;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "serviceType", serviceType, "serviceItem", serviceItem,
                "serviceProvider", serviceProvider, "varianceDate", varianceDate, "serviceCost", serviceCost,
                "serviceStatus", serviceStatus, "serviceNotes", serviceNotes);
    }
}
