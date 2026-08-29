package com.minicad.export.json;

import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepActionPropertyRepresentation;
import com.minicad.step.model.StepBackChainingRuleBody;
import com.minicad.step.model.StepContactRatioRepresentation;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.StepPlacedDatumTargetFeature;
import com.minicad.step.model.StepPropertyDefinitionRelationship;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepResourcePropertyRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Per-export lookup indexes over the resolved entity map for
 * {@link StepPmiTargetBuilder#collectSemanticTargets}. The recursion used to
 * re-scan every resolved entity (93k on large files) once per visited node;
 * the index answers the two hot query shapes from lazily built buckets
 * instead. Buckets preserve resolved-map iteration order, so target
 * insertion order is unchanged. The generated STEP model hierarchy is flat
 * (every class directly implements StepEntity), so exact concrete-class
 * buckets are equivalent to the instanceof scans they replace.
 */
final class PmiEntityIndex {

    private final Map<Integer, StepEntity> resolved;
    private final Map<Integer, List<StepPropertyDefinition>> propertyDefinitionsByDefinitionId = new HashMap<>();
    private boolean propertyDefinitionsIndexed;
    private List<StepEntity> propertyDefinitionLinks;

    PmiEntityIndex(Map<Integer, StepEntity> resolved) {
        this.resolved = resolved;
    }

    /**
     * Property definitions whose definition() points at the given id, in
     * resolved order. Backs collectTargetsReferencingEntity, which the
     * semantic-target recursion calls once per visited entity.
     */
    List<StepPropertyDefinition> propertyDefinitionsReferencing(int definitionId) {
        if (!propertyDefinitionsIndexed) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPropertyDefinition) {
                    StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                    propertyDefinitionsByDefinitionId
                            .computeIfAbsent(propertyDefinition.definition().id(), key -> new ArrayList<>())
                            .add(propertyDefinition);
                }
            }
            propertyDefinitionsIndexed = true;
        }
        return propertyDefinitionsByDefinitionId.getOrDefault(definitionId, List.of());
    }

    /**
     * Entities of the representation-link types handled by the
     * StepPropertyDefinition branch of collectSemanticTargets, in resolved order.
     */
    List<StepEntity> propertyDefinitionLinks() {
        if (propertyDefinitionLinks == null) {
            List<StepEntity> links = new ArrayList<>();
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPropertyDefinitionRepresentation
                        || candidate instanceof StepActionPropertyRepresentation
                        || candidate instanceof StepContactRatioRepresentation
                        || candidate instanceof StepKinematicPropertyDefinitionRepresentation
                        || candidate instanceof StepKinematicPropertyMechanismRepresentation
                        || candidate instanceof StepKinematicPropertyRepresentationRelation
                        || candidate instanceof StepKinematicPropertyTopologyRepresentation
                        || candidate instanceof StepResourcePropertyRepresentation
                        || candidate instanceof StepForwardChainingRulePremise
                        || candidate instanceof StepBackChainingRuleBody
                        || candidate instanceof StepPlacedDatumTargetFeature
                        || candidate instanceof StepPropertyDefinitionRelationship) {
                    links.add(candidate);
                }
            }
            propertyDefinitionLinks = List.copyOf(links);
        }
        return propertyDefinitionLinks;
    }
}
