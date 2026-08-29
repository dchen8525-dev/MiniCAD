package com.minicad.preview.builder;

import com.minicad.export.json.StepMetadataHelper;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepComplexClippingResult;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepWireShell;

public final class PmiTargetHelper {

    private PmiTargetHelper() {
        // Utility class
    }

    public static String pmiTargetType(StepEntity target) {
        if (target instanceof StepFaceEntity) {
            return "face";
        }
        if (target instanceof StepEdgeCurve
                || target instanceof StepSubedge
                || target instanceof StepOrientedEdge) {
            return "edge";
        }
        if (target instanceof StepPath
                || target instanceof StepOpenPath
                || target instanceof StepSubpath
                || target instanceof StepOrientedPath) {
            return "path";
        }
        if (target instanceof StepConnectedEdgeSet) {
            return "edge_set";
        }
        if (target instanceof StepPointSet) {
            return "point_set";
        }
        if (target instanceof StepAnnotationSymbol) {
            return "annotation_symbol";
        }
        if (target instanceof StepAnnotationText) {
            return "annotation_text";
        }
        if (target instanceof StepAnnotationTextCharacter) {
            return "annotation_text_character";
        }
        if (target instanceof StepAnnotationFillArea) {
            return "annotation_fill_area";
        }
        if (target instanceof StepGeometricSet) {
            return "geometric_set";
        }
        if (target instanceof StepGeometricCurveSet) {
            return "curve_set";
        }
        if (target instanceof StepOpenShell
                || target instanceof StepSurfacedOpenShell
                || target instanceof StepOrientedOpenShell
                || target instanceof StepClosedShell
                || target instanceof StepOrientedClosedShell) {
            return "shell";
        }
        if (target instanceof StepWireShell) {
            return "wire_shell";
        }
        if (target instanceof StepVertexShell) {
            return "vertex_shell";
        }
        if (target instanceof StepEdgeLoop
                || target instanceof StepVertexLoop
                || target instanceof StepPolyLoop) {
            return "loop";
        }
        if (target instanceof StepConnectedFaceSet || target instanceof StepConnectedFaceSubSet) {
            return "face_set";
        }
        if (target instanceof StepFaceBasedSurfaceModel || target instanceof StepShellBasedSurfaceModel) {
            return "surface_model";
        }
        if (target instanceof StepEdgeBasedWireframeModel || target instanceof StepShellBasedWireframeModel) {
            return "wireframe_model";
        }
        if (target instanceof StepManifoldSolidBrep
                || target instanceof StepBrepWithVoids
                || target instanceof StepSweptAreaSolid
                || target instanceof StepSolidReplica
                || target instanceof StepCsgSolid
                || target instanceof StepCsgPrimitive
                || target instanceof StepBooleanResult
                || target instanceof StepBooleanClippingResult
                || target instanceof StepSweptDiskSolid
                || target instanceof StepExtrudedAreaSolidTapered
                || target instanceof StepRevolvedAreaSolidTapered
                || target instanceof StepSurfaceCurveSweptAreaSolid
                || target instanceof StepPolygonalBoundedHalfSpace
                || target instanceof StepComplexClippingResult) {
            return "solid";
        }
        if (target instanceof StepRepresentation) {
            return "representation";
        }
        return "entity";
    }

    public static String pmiTargetName(StepEntity target) {
        if (target instanceof StepFaceEntity) {
            StepFaceEntity face = (StepFaceEntity) target;
            return StepMetadataHelper.faceDisplayName(face);
        }
        if (target instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) target;
            return edge.name();
        }
        if (target instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) target;
            return subedge.name();
        }
        if (target instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) target;
            return orientedEdge.name();
        }
        if (target instanceof StepPath) {
            StepPath path = (StepPath) target;
            return path.name();
        }
        if (target instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) target;
            return path.name();
        }
        if (target instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) target;
            return subpath.name();
        }
        if (target instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) target;
            return orientedPath.name();
        }
        if (target instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) target;
            return edgeSet.name();
        }
        if (target instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) target;
            return pointSet.name();
        }
        if (target instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) target;
            return annotationSymbol.name();
        }
        if (target instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) target;
            return annotationText.name();
        }
        if (target instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) target;
            return annotationTextCharacter.name();
        }
        if (target instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) target;
            return fillArea.name();
        }
        if (target instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) target;
            return geometricSet.name();
        }
        if (target instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) target;
            return curveSet.name();
        }
        if (target instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell openShell = (StepSurfacedOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell openShell = (StepOrientedOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) target;
            return closedShell.name();
        }
        if (target instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell closedShell = (StepOrientedClosedShell) target;
            return closedShell.name();
        }
        if (target instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) target;
            return wireShell.name();
        }
        if (target instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) target;
            return vertexShell.name();
        }
        if (target instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) target;
            return edgeLoop.name();
        }
        if (target instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) target;
            return vertexLoop.name();
        }
        if (target instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) target;
            return polyLoop.name();
        }
        if (target instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) target;
            return faceSet.name();
        }
        if (target instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) target;
            return faceSet.name();
        }
        if (target instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) target;
            return surfaceModel.name();
        }
        if (target instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) target;
            return surfaceModel.name();
        }
        if (target instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) target;
            return wireframeModel.name();
        }
        if (target instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) target;
            return wireframeModel.name();
        }
        if (target instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) target;
            return solid.name();
        }
        if (target instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) target;
            return solid.name();
        }
        if (target instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) target;
            return solid.name();
        }
        if (target instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) target;
            return solid.name();
        }
        if (target instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) target;
            return solid.name();
        }
        if (target instanceof StepCsgPrimitive) {
            StepCsgPrimitive solid = (StepCsgPrimitive) target;
            return solid.name();
        }
        if (target instanceof StepBooleanResult) {
            StepBooleanResult solid = (StepBooleanResult) target;
            return solid.name();
        }
        if (target instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult solid = (StepBooleanClippingResult) target;
            return solid.name();
        }
        if (target instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) target;
            return solid.name();
        }
        if (target instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) target;
            return solid.name();
        }
        if (target instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) target;
            return representation.name();
        }
        return "";
    }
}
