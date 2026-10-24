package com.minicad.helper;

import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepShapeRepresentationRelationship;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Navigation over the plain links between shape representations.
 *
 * <p>A product's SHAPE_REPRESENTATION often holds no geometry itself: it points,
 * through SHAPE_REPRESENTATION_RELATIONSHIP, at the ADVANCED_BREP_SHAPE_REPRESENTATION
 * that does. Callers asking a product "which geometry do you draw" must follow
 * those links. Assembly placements are deliberately not followed:
 * REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION is a distinct type carrying one
 * instance's placement, so treating it as a plain link would replay the geometry
 * at the wrong place.
 */
public final class ShapeRepresentationHelper {

    private ShapeRepresentationHelper() {
        // Static helper class - no instances
    }

    /**
     * Returns the representation seeded plus every representation reachable from
     * it through non-transform representation relationships, in traversal order.
     */
    public static List<StepRepresentation> linkedRepresentations(
            StepRepresentation seed,
            Map<Integer, StepEntity> resolved
    ) {
        List<StepRepresentation> ordered = new ArrayList<>();
        collectLinkedRepresentations(seed, resolved, new LinkedHashSet<>(), ordered);
        return List.copyOf(ordered);
    }

    private static void collectLinkedRepresentations(
            StepRepresentation current,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visited,
            List<StepRepresentation> ordered
    ) {
        if (!visited.add(current.id())) {
            return;
        }
        ordered.add(current);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepShapeRepresentationRelationship) {
                StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
                continue;
            }
            if (entity instanceof StepRepresentationRelationship) {
                StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
            }
        }
    }
}
