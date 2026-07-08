package com.minicad.helper.geometry;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.annotation.StepPlanarBox;
import com.minicad.step.model.annotation.StepPlanarExtent;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.geometry.StepSurfacePatch;
import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.product.StepGeometricSurfaceSet;
import com.minicad.step.model.product.StepTessellatedFace;
import com.minicad.step.model.product.StepTessellatedFaceSet;
import com.minicad.step.model.topology.StepClosedShell;
import com.minicad.step.model.topology.StepConnectedFaceSet;
import com.minicad.step.model.topology.StepConnectedFaceSubSet;
import com.minicad.step.model.topology.StepOpenShell;
import com.minicad.step.model.topology.StepOrientedClosedShell;
import com.minicad.step.model.topology.StepOrientedOpenShell;

import java.util.List;

/**
 * Helper methods for shell entity handling.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
final class ShellHelper {

    private ShellHelper() {
        // Static helper class - no instances
    }

    /**
     * Returns the faces of a shell-like entity.
     * Supports open/closed shells, connected face sets, and face subsets.
     */
    static List<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return orientedClosedShell.faces();
        }
        if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return connectedFaceSet.faces();
        }
        if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return connectedFaceSubSet.faces();
        }
        throw new UnsupportedGeometryException(
                "preview export requires shell or connected face set geometry");
    }

    /**
     * Checks if an entity is a shell entity (open or closed shell).
     */
    static boolean isShellEntity(StepEntity entity) {
        return entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell;
    }

    /**
     * Checks if an entity is shell-like (shell, connected face set, tessellated, etc.).
     */
    static boolean isShellLikeEntity(StepEntity entity) {
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