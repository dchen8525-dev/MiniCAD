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
  // === Element Property Entities ===

  StepCurve3dElementProperty resolveCurve3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_3D_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCurve3dElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepFea2DElementProperty resolveFea2DElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_2D_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFea2DElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_2D_ELEMENT_PROPERTY properties must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFea3DElementProperty resolveFea3DElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_3D_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFea3DElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_3D_ELEMENT_PROPERTY properties must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaBeamElementProperty resolveFeaBeamElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_BEAM_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepFeaBeamElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_BEAM_ELEMENT_PROPERTY properties must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepFeaShellElementProperty resolveFeaShellElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_SHELL_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaShellElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_SHELL_ELEMENT_PROPERTY properties must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaSpringElementProperty resolveFeaSpringElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_SPRING_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaSpringElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaTrussElementProperty resolveFeaTrussElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_TRUSS_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaTrussElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaVolumeElementProperty resolveFeaVolumeElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_VOLUME_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaVolumeElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_VOLUME_ELEMENT_PROPERTY properties must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepSurface3dElementProperty resolveSurface3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_3D_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSurface3dElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepVolume3dElementProperty resolveVolume3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VOLUME_3D_ELEMENT_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepVolume3dElementProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }
  // === FEA Node / Element / Load Entities ===

  StepFeaElement resolveFeaElement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ELEMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity elementProperty = resolver.tryResolveReference(definition.parameters().get(3));
    return new StepFeaElement(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2, "ELEMENT nodes must contain entity references"),
        elementProperty);
  }

  StepFeaLoad resolveFeaLoad(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LOAD");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaLoad(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.numberValue(instance, definition, 3));
  }

  StepFeaNode resolveFeaNode(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NODE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaNode(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  // === FEA Material / Variable Entities ===

  StepFeaAxis2Placement3d resolveFeaAxis2Placement3d(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_AXIS_2_PLACEMENT_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepFeaAxis2Placement3d(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepFeaConstantFunction3d resolveFeaConstantFunction3d(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_CONSTANT_FUNCTION_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaConstantFunction3d(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaLinearAlgebraicMatrix resolveFeaLinearAlgebraicMatrix(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_LINEAR_ALGEBRAIC_MATRIX");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepFeaLinearAlgebraicMatrix(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        (int) resolver.numberValue(instance, definition, 1),
        (int) resolver.numberValue(instance, definition, 2),
        resolver.numberList(instance, definition, 3));
  }

  StepFeaLinearAlgebraicVector resolveFeaLinearAlgebraicVector(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_LINEAR_ALGEBRAIC_VECTOR");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaLinearAlgebraicVector(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        (int) resolver.numberValue(instance, definition, 1),
        resolver.numberList(instance, definition, 2));
  }

  StepFeaMassDensity resolveFeaMassDensity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_MASS_DENSITY");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepFeaMassDensity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1));
  }

  StepFeaSecuredVariable resolveFeaSecuredVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_SECURED_VARIABLE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaSecuredVariable(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepFeaUltimateStress resolveFeaUltimateStress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_ULTIMATE_STRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepFeaUltimateStress(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1));
  }

  StepFeaYieldStress resolveFeaYieldStress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_YIELD_STRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepFeaYieldStress(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1));
  }
}
