package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.StepEntity;

/**
 * Validation methods extracted from StepEntityResolver.
 * Contains static methods for validating STEP entity types.
 */
final class StepEntityValidator {

  private StepEntityValidator() {}

  static boolean isOpenShellEntity(StepEntity entity) {
    return entity instanceof StepOpenShell || entity instanceof StepSurfacedOpenShell;
  }

  static boolean isClosedShellEntity(StepEntity entity) {
    return entity instanceof StepClosedShell;
  }

  static boolean isShellEntity(StepEntity entity) {
    return isOpenShellEntity(entity) || isClosedShellEntity(entity);
  }

  static boolean isBooleanOperandEntity(StepEntity entity) {
    return isShellEntity(entity)
        || entity instanceof StepManifoldSolidBrep
        || entity instanceof StepBrepWithVoids
        || entity instanceof StepBooleanResult
        || entity instanceof StepBooleanClippingResult;
  }

  static boolean isConnectedFaceSetEntity(StepEntity entity) {
    return entity instanceof StepConnectedFaceSet
        || entity instanceof StepOpenShell
        || entity instanceof StepClosedShell
        || entity instanceof StepSurfacedOpenShell;
  }

  static boolean isAnnotationOccurrence(StepEntity entity) {
    return entity instanceof StepAnnotationTextOccurrence
        || entity instanceof StepAnnotationPointOccurrence
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepLeaderCurve
        || entity instanceof StepProjectionCurve
        || entity instanceof StepDimensionCurve
        || entity instanceof StepAnnotationFillAreaOccurrence
        || entity instanceof StepAnnotationPlaceholderOccurrence
        || entity instanceof StepAnnotationPlane
        || entity instanceof StepAnnotationSymbolOccurrence
        || entity instanceof StepAnnotationSubfigureOccurrence
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol;
  }

  static boolean isSupportedAnnotationWrapperItem(StepEntity entity) {
    return entity instanceof StepAnnotationTextOccurrence
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepAnnotationFillAreaOccurrence
        || entity instanceof StepAnnotationPlaceholderOccurrence
        || entity instanceof StepAnnotationSymbolOccurrence
        || entity instanceof StepAnnotationSubfigureOccurrence
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol
        || entity instanceof StepAnnotationPlane;
  }

  static boolean isSupportedAnnotationUsageItem(StepEntity entity) {
    return entity instanceof StepDraughtingCallout || isSupportedAnnotationWrapperItem(entity);
  }

  static boolean isSupportedGeometricCurveSetElement(StepEntity element) {
    return element instanceof StepCartesianPoint
        || element instanceof StepPoint
        || element instanceof StepCurve
        || element instanceof StepBoundedCurve
        || element instanceof StepAnnotationCurveOccurrence
        || element instanceof StepLeaderCurve
        || element instanceof StepProjectionCurve
        || element instanceof StepDimensionCurve;
  }

  static boolean isSupportedGeometricSetElement(StepEntity element) {
    return isSupportedGeometricCurveSetElement(element)
        || element instanceof StepSurface
        || element instanceof StepBoundedSurface
        || element instanceof StepAnnotationFillArea;
  }

  static boolean isPointLikeSetElement(StepEntity element) {
    return element instanceof StepCartesianPoint
        || element instanceof StepPoint
        || element instanceof StepVertexPoint;
  }

  static boolean isPathEntity(StepEntity entity) {
    return entity instanceof StepPath || entity instanceof StepEdgeLoop || entity instanceof StepLoop;
  }
}
