package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INTEGRATION_TEST_RESULT.
 * An integration test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceComponents integrated variance components
 * @varianceInterfaces tested variance interfaces
 * @varianceIssues integration variance issues
 * @varianceResolved resolved variance issues count
 * @varianceStatus result variance status
 */
public record StepIntegrationTestResult(
    int id,
    String name,
    List<StepEntity> varianceComponents,
    List<StepEntity> varianceInterfaces,
    List<String> varianceIssues,
    int varianceResolved,
    String varianceStatus) implements StepEntity {
}