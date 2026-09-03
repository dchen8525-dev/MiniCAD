package com.minicad.preview.builder;

import java.util.List;
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

    // pmiTargetName dispatch table (first-match-return, mirrors the original sequential ifs).
    private record PmiTargetNameRule(Class<? extends StepEntity> type, PmiTargetNameHandler handler) {}

    private interface PmiTargetNameHandler {
        String name(StepEntity target);
    }

    private static PmiTargetNameRule pmiTargetNameRule(
            Class<? extends StepEntity> type, PmiTargetNameHandler handler) {
        return new PmiTargetNameRule(type, handler);
    }

    private static final List<PmiTargetNameRule> PMI_TARGET_NAME_RULES = List.of(
        pmiTargetNameRule(StepFaceEntity.class, (target) -> {
            StepFaceEntity face = (StepFaceEntity) target;
            return StepMetadataHelper.faceDisplayName(face);
        }),
        pmiTargetNameRule(StepEdgeCurve.class, (target) -> {
            StepEdgeCurve edge = (StepEdgeCurve) target;
            return edge.name();
        }),
        pmiTargetNameRule(StepSubedge.class, (target) -> {
            StepSubedge subedge = (StepSubedge) target;
            return subedge.name();
        }),
        pmiTargetNameRule(StepOrientedEdge.class, (target) -> {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) target;
            return orientedEdge.name();
        }),
        pmiTargetNameRule(StepPath.class, (target) -> {
            StepPath path = (StepPath) target;
            return path.name();
        }),
        pmiTargetNameRule(StepOpenPath.class, (target) -> {
            StepOpenPath path = (StepOpenPath) target;
            return path.name();
        }),
        pmiTargetNameRule(StepSubpath.class, (target) -> {
            StepSubpath subpath = (StepSubpath) target;
            return subpath.name();
        }),
        pmiTargetNameRule(StepOrientedPath.class, (target) -> {
            StepOrientedPath orientedPath = (StepOrientedPath) target;
            return orientedPath.name();
        }),
        pmiTargetNameRule(StepConnectedEdgeSet.class, (target) -> {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) target;
            return edgeSet.name();
        }),
        pmiTargetNameRule(StepPointSet.class, (target) -> {
            StepPointSet pointSet = (StepPointSet) target;
            return pointSet.name();
        }),
        pmiTargetNameRule(StepAnnotationSymbol.class, (target) -> {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) target;
            return annotationSymbol.name();
        }),
        pmiTargetNameRule(StepAnnotationText.class, (target) -> {
            StepAnnotationText annotationText = (StepAnnotationText) target;
            return annotationText.name();
        }),
        pmiTargetNameRule(StepAnnotationTextCharacter.class, (target) -> {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) target;
            return annotationTextCharacter.name();
        }),
        pmiTargetNameRule(StepAnnotationFillArea.class, (target) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) target;
            return fillArea.name();
        }),
        pmiTargetNameRule(StepGeometricSet.class, (target) -> {
            StepGeometricSet geometricSet = (StepGeometricSet) target;
            return geometricSet.name();
        }),
        pmiTargetNameRule(StepGeometricCurveSet.class, (target) -> {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) target;
            return curveSet.name();
        }),
        pmiTargetNameRule(StepOpenShell.class, (target) -> {
            StepOpenShell openShell = (StepOpenShell) target;
            return openShell.name();
        }),
        pmiTargetNameRule(StepSurfacedOpenShell.class, (target) -> {
            StepSurfacedOpenShell openShell = (StepSurfacedOpenShell) target;
            return openShell.name();
        }),
        pmiTargetNameRule(StepOrientedOpenShell.class, (target) -> {
            StepOrientedOpenShell openShell = (StepOrientedOpenShell) target;
            return openShell.name();
        }),
        pmiTargetNameRule(StepClosedShell.class, (target) -> {
            StepClosedShell closedShell = (StepClosedShell) target;
            return closedShell.name();
        }),
        pmiTargetNameRule(StepOrientedClosedShell.class, (target) -> {
            StepOrientedClosedShell closedShell = (StepOrientedClosedShell) target;
            return closedShell.name();
        }),
        pmiTargetNameRule(StepWireShell.class, (target) -> {
            StepWireShell wireShell = (StepWireShell) target;
            return wireShell.name();
        }),
        pmiTargetNameRule(StepVertexShell.class, (target) -> {
            StepVertexShell vertexShell = (StepVertexShell) target;
            return vertexShell.name();
        }),
        pmiTargetNameRule(StepEdgeLoop.class, (target) -> {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) target;
            return edgeLoop.name();
        }),
        pmiTargetNameRule(StepVertexLoop.class, (target) -> {
            StepVertexLoop vertexLoop = (StepVertexLoop) target;
            return vertexLoop.name();
        }),
        pmiTargetNameRule(StepPolyLoop.class, (target) -> {
            StepPolyLoop polyLoop = (StepPolyLoop) target;
            return polyLoop.name();
        }),
        pmiTargetNameRule(StepConnectedFaceSet.class, (target) -> {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) target;
            return faceSet.name();
        }),
        pmiTargetNameRule(StepConnectedFaceSubSet.class, (target) -> {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) target;
            return faceSet.name();
        }),
        pmiTargetNameRule(StepFaceBasedSurfaceModel.class, (target) -> {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) target;
            return surfaceModel.name();
        }),
        pmiTargetNameRule(StepShellBasedSurfaceModel.class, (target) -> {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) target;
            return surfaceModel.name();
        }),
        pmiTargetNameRule(StepEdgeBasedWireframeModel.class, (target) -> {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) target;
            return wireframeModel.name();
        }),
        pmiTargetNameRule(StepShellBasedWireframeModel.class, (target) -> {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) target;
            return wireframeModel.name();
        }),
        pmiTargetNameRule(StepManifoldSolidBrep.class, (target) -> {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepBrepWithVoids.class, (target) -> {
            StepBrepWithVoids solid = (StepBrepWithVoids) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepSweptAreaSolid.class, (target) -> {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepSolidReplica.class, (target) -> {
            StepSolidReplica solid = (StepSolidReplica) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepCsgSolid.class, (target) -> {
            StepCsgSolid solid = (StepCsgSolid) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepCsgPrimitive.class, (target) -> {
            StepCsgPrimitive solid = (StepCsgPrimitive) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepBooleanResult.class, (target) -> {
            StepBooleanResult solid = (StepBooleanResult) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepBooleanClippingResult.class, (target) -> {
            StepBooleanClippingResult solid = (StepBooleanClippingResult) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepSweptDiskSolid.class, (target) -> {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepComplexClippingResult.class, (target) -> {
            StepComplexClippingResult solid = (StepComplexClippingResult) target;
            return solid.name();
        }),
        pmiTargetNameRule(StepRepresentation.class, (target) -> {
            StepRepresentation representation = (StepRepresentation) target;
            return representation.name();
        })
    );

    public static String pmiTargetName(StepEntity target) {
        for (PmiTargetNameRule rule : PMI_TARGET_NAME_RULES) {
            if (rule.type().isInstance(target)) {
                return rule.handler().name(target);
            }
        }

        return "";
    }
}
