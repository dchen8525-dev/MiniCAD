package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NETWORK_INSTANCE.
 * A network instance entity.
 *
 * @param id STEP instance id
 * @param name network instance name
 * @param networkDefinition network variance definition reference
 * @param networkState network variance state
 * @param networkTraffic network variance traffic level
 * @param networkBandwidth network variance bandwidth
 * @param networkStatus network variance status
 */
public record StepNetworkInstance(
    int id,
    String name,
    StepEntity networkDefinition,
    String networkState,
    double networkTraffic,
    double networkBandwidth,
    String networkStatus) implements StepEntity {
}