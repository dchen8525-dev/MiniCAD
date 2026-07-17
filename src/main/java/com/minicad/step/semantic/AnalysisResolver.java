package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Analysis resolver - handles analysis, simulation, and configuration entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains analysis results, model definitions, simulation entities, and configuration.
 */
final class AnalysisResolver {

  private final StepEntityResolver resolver;

  AnalysisResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Analysis Entities ===

  StepAnalysisResult resolveAnalysisResult(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANALYSIS_RESULT");
    StepEntityResolver.requireParameterCount(instance, definition, 9);
    List<Double> resultValues = resolver.numberList(instance, definition, 5);
    List<StepEntity> resultLocations = resolver.entityReferenceList(
        instance, definition, 6, "ANALYSIS_RESULT result_locations must contain entity references");
    return new StepAnalysisResult(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resultValues,
        resultLocations,
        resolver.numberValue(instance, definition, 7),
        resolver.numberValue(instance, definition, 8));
  }

  StepAnalysisInstance resolveAnalysisInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANALYSIS_INSTANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<String> analysisResults = resolver.literalList(instance, definition, 4);
    return new StepAnalysisInstance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        analysisResults,
        resolver.stringValue(instance, definition, 5),
        resolver.stringValue(instance, definition, 6));
  }

  // === Configuration Entities ===

  StepConfigurationInstance resolveConfigurationInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONFIGURATION_INSTANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<String> configurationValues = resolver.literalList(instance, definition, 4);
    return new StepConfigurationInstance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        configurationValues,
        resolver.booleanValue(instance, definition, 5),
        resolver.stringValue(instance, definition, 6));
  }

  StepConfigurationItem resolveConfigurationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONFIGURATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepConfigurationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.enumValue(instance, definition, 3));
  }

  StepConfigurationEffectivity resolveConfigurationEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONFIGURATION_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepConfigurationEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Model Entities ===

  StepModelDefinition resolveModelDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MODEL_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<String> modelParameters = resolver.literalList(instance, definition, 4);
    List<String> modelConstraints = resolver.literalList(instance, definition, 5);
    return new StepModelDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        modelParameters,
        modelConstraints,
        resolver.stringValue(instance, definition, 6));
  }

  StepModelInstance resolveModelInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MODEL_INSTANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<String> modelProperties = resolver.literalList(instance, definition, 5);
    return new StepModelInstance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.stringValue(instance, definition, 3),
        modelProperties,
        resolver.stringValue(instance, definition, 6));
  }

  // === Simulation Entities ===

  StepSimulationDefinition resolveSimulationDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SIMULATION_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<String> simulationParameters = resolver.literalList(instance, definition, 4);
    return new StepSimulationDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        simulationParameters,
        resolver.numberValue(instance, definition, 5),
        resolver.stringValue(instance, definition, 6));
  }

  StepSimulationInstance resolveSimulationInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SIMULATION_INSTANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    List<StepEntity> simulationResults = resolver.entityReferenceList(
        instance, definition, 6, "SIMULATION_INSTANCE simulation_results must contain entity references");
    return new StepSimulationInstance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        simulationResults,
        resolver.stringValue(instance, definition, 7));
  }

  // === FEA / Structural Analysis Entities ===

  StepFeaModel resolveFeaModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepFeaModel(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2, "FEA_MODEL elements must contain entity references"),
        resolver.entityReferenceList(instance, definition, 3, "FEA_MODEL loads must contain entity references"),
        resolver.entityReferenceList(instance, definition, 4, "FEA_MODEL boundary conditions must contain entity references"));
  }

  StepStructuralAnalysisModel resolveStructuralAnalysisModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRUCTURAL_ANALYSIS_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepStructuralAnalysisModel(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2, "STRUCTURAL_ANALYSIS_MODEL elements must contain entity references"),
        resolver.entityReferenceList(instance, definition, 3, "STRUCTURAL_ANALYSIS_MODEL loads must contain entity references"),
        resolver.entityReferenceList(instance, definition, 4, "STRUCTURAL_ANALYSIS_MODEL boundary conditions must contain entity references"));
  }

  StepStructuralAnalysisRepresentation resolveStructuralAnalysisRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRUCTURAL_ANALYSIS_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepStructuralAnalysisRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2, "STRUCTURAL_ANALYSIS_REPRESENTATION items must contain entity references"));
  }

  StepStructuralAnalysisRepresentationParameters resolveStructuralAnalysisRepresentationParameters(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepStructuralAnalysisRepresentationParameters(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepStructAnalysisModel resolveStructAnalysisModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRUCT_ANALYSIS_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepStructAnalysisModel(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepStressAnalysis resolveStressAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRESS_ANALYSIS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepStressAnalysis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepBucklingAnalysis resolveBucklingAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BUCKLING_ANALYSIS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepBucklingAnalysis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        (int) resolver.numberValue(instance, definition, 1));
  }

  StepModalAnalysis resolveModalAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MODAL_ANALYSIS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepModalAnalysis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        (int) resolver.numberValue(instance, definition, 1));
  }

  StepThermalAnalysis resolveThermalAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "THERMAL_ANALYSIS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepThermalAnalysis(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepKinematicModel resolveKinematicModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepKinematicModel(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }
}