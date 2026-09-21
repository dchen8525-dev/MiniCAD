package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INTERFACE_SPECIFICATION.
 * An interface specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceInterface interface variance reference
 * @varianceMechanical mechanical variance requirements
 * @varianceElectrical electrical variance requirements
 * @varianceData data variance requirements
 * @varianceStandard interface variance standard reference
 * @varianceStatus specification variance status
 */
public final class StepInterfaceSpecification extends AbstractStepEntity {
    private final StepEntity varianceInterface;
    private final List<StepEntity> varianceMechanical;
    private final List<StepEntity> varianceElectrical;
    private final List<StepEntity> varianceData;
    private final String varianceStandard;
    private final String varianceStatus;

    public StepInterfaceSpecification(int id, String name, StepEntity varianceInterface, List<StepEntity> varianceMechanical, List<StepEntity> varianceElectrical, List<StepEntity> varianceData, String varianceStandard, String varianceStatus) {
        super(id, name);
        this.varianceInterface = varianceInterface;
        this.varianceMechanical = varianceMechanical == null ? null : java.util.List.copyOf(varianceMechanical);
        this.varianceElectrical = varianceElectrical == null ? null : java.util.List.copyOf(varianceElectrical);
        this.varianceData = varianceData == null ? null : java.util.List.copyOf(varianceData);
        this.varianceStandard = varianceStandard;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceInterface() {
        return varianceInterface;
    }

    public List<StepEntity> getVarianceMechanical() {
        return varianceMechanical;
    }

    public List<StepEntity> getVarianceElectrical() {
        return varianceElectrical;
    }

    public List<StepEntity> getVarianceData() {
        return varianceData;
    }

    public String getVarianceStandard() {
        return varianceStandard;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceInterface", varianceInterface);
        state.put("varianceMechanical", varianceMechanical);
        state.put("varianceElectrical", varianceElectrical);
        state.put("varianceData", varianceData);
        state.put("varianceStandard", varianceStandard);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
