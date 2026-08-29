package com.minicad.app;

import com.minicad.step.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for collecting STEP entity IDs from various entity types.
 */
public final class StepEntityIdCollector {

    private StepEntityIdCollector() {
    }

    /**
     * Collects face IDs from shell entities.
     *
     * @param entities the entities to process
     * @return set of face IDs
     */
    public static Set<Integer> collectShellFaceIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOpenShell) {
                StepOpenShell openShell = (StepOpenShell) entity;
                openShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepSurfacedOpenShell) {
                StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
                surfacedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedOpenShell) {
                StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
                orientedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepClosedShell) {
                StepClosedShell closedShell = (StepClosedShell) entity;
                closedShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedClosedShell) {
                StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
                orientedClosedShell.faces().forEach(face -> ids.add(face.id()));
            }
        }
        return ids;
    }

    /**
     * Collects oriented edge IDs from edge loop entities.
     *
     * @param entities the entities to process
     * @return set of oriented edge IDs
     */
    public static Set<Integer> collectLoopOrientedEdgeIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof com.minicad.step.model.StepEdgeLoop) {
                com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) entity;
                edgeLoop.edges().forEach(edge -> ids.add(edge.id()));
            }
        }
        return ids;
    }

    /**
     * Collects edge element IDs from oriented edge entities.
     *
     * @param entities the entities to process
     * @return set of edge element IDs
     */
    public static Set<Integer> collectOrientedEdgeElementIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOrientedEdge) {
                StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
                ids.add(orientedEdge.edgeElement().id());
            }
        }
        return ids;
    }

    /**
     * Collects loop IDs from face bound entities.
     *
     * @param entities the entities to process
     * @return set of loop IDs
     */
    public static Set<Integer> collectFaceBoundLoopIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepFaceBound) {
                StepFaceBound faceBound = (StepFaceBound) entity;
                ids.add(faceBound.loop().id());
            }
        }
        return ids;
    }
}
