package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STATISTICS_RECORD.
 * A statistics record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSubject statistics variance subject
 * @varianceMetrics statistical variance metrics
 * @varianceValues statistical variance values
 * @variancePeriod statistics variance period
 * @varianceTrend trend variance analysis
 * @varianceStatus record variance status
 */
public record StepStatisticsRecord(
    int id,
    String name,
    String varianceSubject,
    List<String> varianceMetrics,
    List<Double> varianceValues,
    String variancePeriod,
    String varianceTrend,
    String varianceStatus) implements StepEntity {
}