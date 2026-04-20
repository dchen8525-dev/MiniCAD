package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepServiceInformation(
    int id,
    String name,
    String serviceType,
    StepEntity serviceItem,
    StepEntity serviceProvider,
    StepEntity varianceDate,
    double serviceCost,
    String serviceStatus,
    String serviceNotes) implements StepEntity {
}