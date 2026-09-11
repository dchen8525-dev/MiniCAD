package com.minicad.helper;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepPlanarBox;
import com.minicad.step.model.StepPlanarExtent;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.step.model.StepGeometricSurfaceSet;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;

import java.util.List;

/**
 * Helper methods for shell entity handling.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
public final class ShellHelper {

    private ShellHelper() {
        // Static helper class - no instances
    }

    // shellFaces dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record FacesRule(
            Class<? extends StepEntity> type, FacesHandler handler) {}

    private interface FacesHandler {
        List<StepFaceEntity> build(StepEntity entity);
    }

    private static FacesRule facesRule(
            Class<? extends StepEntity> type, FacesHandler handler) {
        return new FacesRule(type, handler);
    }

    private static final List<FacesRule> FACES_RULES = List.of(
        facesRule(StepOpenShell.class, (entity) -> {
            StepOpenShell openShell = (StepOpenShell) entity;
            return openShell.faces();
        }),
        facesRule(StepSurfacedOpenShell.class, (entity) -> {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return surfacedOpenShell.faces();
        }),
        facesRule(StepOrientedOpenShell.class, (entity) -> {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return orientedOpenShell.faces();
        }),
        facesRule(StepClosedShell.class, (entity) -> {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return closedShell.faces();
        }),
        facesRule(StepOrientedClosedShell.class, (entity) -> {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return orientedClosedShell.faces();
        }),
        facesRule(StepConnectedFaceSet.class, (entity) -> {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return connectedFaceSet.faces();
        }),
        facesRule(StepConnectedFaceSubSet.class, (entity) -> {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return connectedFaceSubSet.faces();
        })
    );

    /**
     * Returns the faces of a shell-like entity.
     * Supports open/closed shells, connected face sets, and face subsets.
     */
    public static List<StepFaceEntity> shellFaces(StepEntity entity) {
        for (FacesRule rule : FACES_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().build(entity);
            }
        }

        throw new UnsupportedGeometryException(
                "preview export requires shell or connected face set geometry");
    }

    /**
     * Checks if an entity is a shell entity (open or closed shell).
     */
    public static boolean isShellEntity(StepEntity entity) {
        return entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell;
    }

    /**
     * Checks if an entity is shell-like (shell, connected face set, tessellated, etc.).
     */
    public static boolean isShellLikeEntity(StepEntity entity) {
        return isShellEntity(entity)
                || entity instanceof StepConnectedFaceSet
                || entity instanceof StepConnectedFaceSubSet
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepGeometricSurfaceSet
                || entity instanceof StepPlanarBox
                || entity instanceof StepPlanarExtent
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepSurfacePatch;
    }
}
