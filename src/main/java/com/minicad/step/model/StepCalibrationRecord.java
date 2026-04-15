package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CALIBRATION_RECORD.
 * A calibration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment calibrated variance equipment
 * @varianceStandard calibration variance standard
 * @varianceDate calibration variance date
 * @varianceResults calibration variance results
 * @varianceNext next variance calibration date
 * @varianceStatus record variance status
 */
public record StepCalibrationRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    StepEntity varianceStandard,
    StepEntity varianceDate,
    List<Double> varianceResults,
    StepEntity varianceNext,
    String varianceStatus) implements StepEntity {
}