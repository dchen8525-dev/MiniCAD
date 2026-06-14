package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepInterfaceSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceInterface;
    private final List<StepEntity> varianceMechanical;
    private final List<StepEntity> varianceElectrical;
    private final List<StepEntity> varianceData;
    private final String varianceStandard;
    private final String varianceStatus;

    public StepInterfaceSpecification(int id, String name, StepEntity varianceInterface, List<StepEntity> varianceMechanical, List<StepEntity> varianceElectrical, List<StepEntity> varianceData, String varianceStandard, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceInterface = varianceInterface;
        this.varianceMechanical = varianceMechanical == null ? null : java.util.List.copyOf(varianceMechanical);
        this.varianceElectrical = varianceElectrical == null ? null : java.util.List.copyOf(varianceElectrical);
        this.varianceData = varianceData == null ? null : java.util.List.copyOf(varianceData);
        this.varianceStandard = varianceStandard;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInterfaceSpecification that = (StepInterfaceSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceInterface, that.varianceInterface) && Objects.equals(varianceMechanical, that.varianceMechanical) && Objects.equals(varianceElectrical, that.varianceElectrical) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceStandard, that.varianceStandard) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceInterface, varianceMechanical, varianceElectrical, varianceData, varianceStandard, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepInterfaceSpecification{" + "id=" + id + "name=" + name + "varianceInterface=" + varianceInterface + "varianceMechanical=" + varianceMechanical + "varianceElectrical=" + varianceElectrical + "varianceData=" + varianceData + "varianceStandard=" + varianceStandard + "varianceStatus=" + varianceStatus + "}";
    }
}