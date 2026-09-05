package com.minicad.export.json;

import com.minicad.preview.builder.PmiTargetHelper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.step.model.*;
import com.minicad.step.semantic.*;
import com.minicad.step.syntax.*;
import com.minicad.topology.*;
import com.minicad.geometry.*;
import com.minicad.common.*;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.preview.payload.*;
import com.minicad.export.glb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accumulates PMI semantic-definition targets from the STEP entity graph.
 */
public final class StepPmiTargetBuilder {
    static final Logger log = LoggerFactory.getLogger(StepPmiTargetBuilder.class);
    private StepPmiTargetBuilder() {}


    static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, null, null, null, null);
    }


    static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendRepresentationBacklinkTarget(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId, null, null);
    }


    static void appendDefinitionBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!StepPmiPayloadBuilder.isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        appendPmiTarget(
                targetsByUsageId,
                identifiedItem.id(),
                representation,
                instanceIdsByTargetId,
                null,
                null,
                null,
                null,
                definitionTypeName(definition),
                definition.id()
        );
    }


    static void appendExistingRepresentationDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!StepPmiPayloadBuilder.isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        List<PmiTargetPayload> existingTargets = List.copyOf(targetsByUsageId.getOrDefault(identifiedItem.id(), List.of()));
        for (PmiTargetPayload target : existingTargets) {
            if (!"representation".equals(target.type())) {
                continue;
            }
            PmiTargetPayload payload = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), target.instanceIds())),
                    null,
                    null,
                    null,
                    null,
                    definitionTypeName(definition),
                    definition.id()
            );
            List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(identifiedItem.id(), ignored -> new ArrayList<>());
            if (!targets.contains(payload)) {
                targets.add(payload);
            }
        }
    }


    static void appendRelationshipBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!StepPmiPayloadBuilder.isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    relationship.entityName(),
                    relationship.id()
            );
        } else if (definition instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id()
            );
        }
    }


    public static void appendSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!StepPmiPayloadBuilder.isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            appendAnnotationOccurrenceRelationshipTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            return;
        }
        if (definition instanceof StepDraughtingCalloutRelationship) {
            appendDraughtingCalloutRelationshipTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            return;
        }
        if (definition instanceof StepPropertyDefinitionRelationship) {
            appendPropertyDefinitionRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
        }
        dispatchSemanticDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                definition,
                resolved,
                instanceIdsByTargetId
        );
        for (StepEntity target : collectSemanticTargets(definition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    definitionTypeName(definition),
                    definition.id()
            );
        }
    }

    /**
     * Dispatch table behind appendSemanticDefinitionTargets.
     *
     * Replaces a ~779-line if/else-if {@code instanceof} chain. The order below is
     * load bearing: {@code instanceof} also matches subtypes and the original chain
     * was "first match wins", so entries keep their original relative order.
     *
     * Each branch body was moved verbatim into a lambda, so behaviour is unchanged
     * -- including branches that call several handlers or run a loop.
     *
     * 15 later branches were unreachable in the original chain because the
     * same type had already been matched earlier; they were dropped here. Keeping
     * the first occurrence of each type means behaviour is unchanged.
     */
    @FunctionalInterface
    private interface SemanticDefinitionHandler {
        void handle(
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                StepEntity identifiedItem,
                StepEntity definition,
                Map<Integer, StepEntity> resolved,
                Map<Integer, List<String>> instanceIdsByTargetId
        );
    }

    private record SemanticDefinitionRule(Class<?> type, SemanticDefinitionHandler handler) {
        boolean matches(StepEntity definition) {
            return type.isInstance(definition);
        }
    }

    private static SemanticDefinitionRule rule(Class<?> type, SemanticDefinitionHandler handler) {
        return new SemanticDefinitionRule(type, handler);
    }

    private static final List<SemanticDefinitionRule> SEMANTIC_DEFINITION_RULES = List.of(
            rule(StepPropertyDefinition.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPropertyDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeneralPropertyRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeneralPropertyRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepShapeAspectRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShapeAspectRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeneralProperty.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepGeneralProperty generalProperty = (StepGeneralProperty) definition;
                        appendGeneralPropertyRelationshipTargets(
                                targetsByUsageId,
                                identifiedItem.id(),
                                generalProperty,
                                resolved,
                                instanceIdsByTargetId
                        );
            }),
            rule(StepShapeAspect.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepShapeAspect shapeAspect = (StepShapeAspect) definition;
                        appendShapeAspectRelationshipTargets(
                                targetsByUsageId,
                                identifiedItem.id(),
                                shapeAspect,
                                resolved,
                                instanceIdsByTargetId
                        );
            }),
            rule(StepProduct.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepProduct product = (StepProduct) definition;
                        appendProductRelationshipTargets(targetsByUsageId, identifiedItem.id(), product, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionFormation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepProductDefinitionFormation formation = (StepProductDefinitionFormation) definition;
                        appendProductDefinitionFormationRelationshipTargets(targetsByUsageId, identifiedItem.id(), formation, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinition.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepProductDefinition productDefinition = (StepProductDefinition) definition;
                        appendProductDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), productDefinition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionFormationRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductDefinitionFormationRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGroupRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGroupRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDocumentRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDocumentRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrganizationRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrganizationRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEffectivityRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEffectivityRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductCategoryRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductCategoryRelationshipDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGroup.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGroupTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDocument.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDocumentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDocumentReference.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDocumentReferenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedDocumentReference.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedDocumentReferenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApprovalAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSecurityClassificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSecurityClassificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepContractAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendContractAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCertificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCertificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPersonAndOrganizationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPersonAndOrganizationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrganizationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrganizationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepLanguageAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendLanguageAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGroupAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGroupAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepClassificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendClassificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDateAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDateAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDateTimeAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDateTimeAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepIdentificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendIdentificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepExternalIdentificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendExternalIdentificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrganization.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepOrganization organization = (StepOrganization) definition;
                        appendOrganizationRelationshipTargets(targetsByUsageId, identifiedItem.id(), organization, resolved, instanceIdsByTargetId);
            }),
            rule(StepEffectivity.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEffectivityTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductCategory.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepProductCategory category = (StepProductCategory) definition;
                        appendProductCategoryRelationshipTargets(targetsByUsageId, identifiedItem.id(), category, resolved, instanceIdsByTargetId);
            }),
            rule(StepExternalSource.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendExternalSourceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepExternallyDefinedItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendExternallyDefinedItemDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDocumentUsageConstraint.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDocumentUsageConstraintTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionShape.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) definition;
                        appendProductDefinitionShapeRepresentationTargets(
                                targetsByUsageId,
                                identifiedItem,
                                productDefinitionShape,
                                resolved,
                                instanceIdsByTargetId
                        );
            }),
            rule(StepNextAssemblyUsageOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) definition;
                        appendOccurrenceRepresentationTargets(
                                targetsByUsageId,
                                identifiedItem,
                                occurrence,
                                resolved,
                                instanceIdsByTargetId
                        );
            }),
            rule(StepShapeAspectOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShapeAspectOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionRelationshipRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductDefinitionRelationshipRelationshipTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApprovalPersonOrganization.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalPersonOrganizationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApprovalDateTime.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalDateTimeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCalendarDate.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCalendarDateTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepNameAttribute.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendNameAttributeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDescriptionAttribute.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDescriptionAttributeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepIdAttribute.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendIdAttributeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepNameAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendNameAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedNameAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedNameAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDateAndTime.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDateAndTimeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepLocalTime.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendLocalTimeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCoordinatedUniversalTimeOffset.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCoordinatedUniversalTimeOffsetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApprovalStatus.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalStatusTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSecurityClassificationLevel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSecurityClassificationLevelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepContractType.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendContractTypeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCertificationType.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCertificationTypeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApprovalRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrganizationRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrganizationRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPersonAndOrganizationRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPersonAndOrganizationRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepClassificationRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendClassificationRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDateRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDateRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDateTimeRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDateTimeRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepIdentificationRole.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendIdentificationRoleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDocumentType.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDocumentTypeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApproval.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApprovalTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSecurityClassification.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSecurityClassificationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepContract.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendContractTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCertification.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCertificationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPerson.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPersonTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPersonAndOrganization.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPersonAndOrganizationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepLanguage.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendLanguageTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedClassificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedClassificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedDateAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedDateAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedDateTimeAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedDateTimeAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedApprovalAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedApprovalAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedSecurityClassificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedSecurityClassificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedContractAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedContractAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedCertificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedCertificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedPersonAndOrganizationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedPersonAndOrganizationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedOrganizationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedOrganizationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedLanguageAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedLanguageAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedGroupAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedGroupAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedIdentificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedIdentificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAppliedExternalIdentificationAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAppliedExternalIdentificationAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationCurveOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationCurveOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationFillArea.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationFillAreaTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationFillAreaOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationFillAreaOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationPlaceholderOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationPlaceholderOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationPointOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationPointOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationSymbolOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationSymbolOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationSubfigureOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationSubfigureOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationTextOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationTextOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDraughtingAnnotationOccurrence.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDraughtingAnnotationOccurrenceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTerminatorSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTerminatorSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPresentationStyleAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPresentationStyleAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleUsage.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleUsageTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceSideStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceSideStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleFillArea.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleFillAreaTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepFillAreaStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendFillAreaStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepFillAreaStyleColour.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendFillAreaStyleColourTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCurveStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCurveStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleBoundary.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleBoundaryTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleParameterLine.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleParameterLineTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleControlGrid.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleControlGridTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleSegmentationCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleSegmentationCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleSilhouette.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleSilhouetteTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCharacterGlyphStyleStroke.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCharacterGlyphStyleStrokeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCharacterGlyphStyleOutline.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCharacterGlyphStyleOutlineTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCharacterGlyphStyleOutlineWithCharacteristics.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCharacterGlyphStyleOutlineWithCharacteristicsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyleWithSpacing.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleWithSpacingTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyleWithBoxCharacteristics.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleWithBoxCharacteristicsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyleWithJustification.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleWithJustificationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyleWithMirror.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleWithMirrorTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTextStyleForDefinedFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTextStyleForDefinedFontTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPointStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPointStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSymbolColour.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSymbolColourTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSymbolStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSymbolStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleTransparent.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleReflectanceAmbient.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleReflectanceAmbientDiffuse.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            }),
            rule(StepSurfaceStyleReflectanceAmbientDiffuseSpecular.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceStyleReflectanceAmbientDiffuseSpecularTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedSurfaceSideStyle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedSurfaceSideStyleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedColour.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedColourTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDraughtingPreDefinedColour.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDraughtingPreDefinedColourTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepColourRgb.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendColourRgbTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepColourSpecification.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendColourSpecificationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepColour.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendColourTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedCurveFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedCurveFontTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDraughtingPreDefinedCurveFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDraughtingPreDefinedCurveFontTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedTextFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedTextFontTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDraughtingPreDefinedTextFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDraughtingPreDefinedTextFontTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedTerminatorSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedTerminatorSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedDimensionSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedDimensionSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedGeometricalToleranceSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedGeometricalToleranceSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationPlane.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAnnotationPlaneTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDraughtingCallout.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDraughtingCalloutTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPresentationLayerAssignment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPresentationLayerAssignmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepStyledItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendStyledItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOverRidingStyledItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOverRidingStyledItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRepresentationMap.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRepresentationMapTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSymbolRepresentationMap.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSymbolRepresentationMapTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepMappedItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendMappedItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeometricReplica.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeometricReplicaTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepItemDefinedTransformation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendItemDefinedTransformationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCartesianTransformationOperator.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCartesianTransformationOperatorTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAxis1Placement.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAxis1PlacementTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAxis2Placement2D.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAxis2Placement2DTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCartesianPoint.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCartesianPointTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPoint.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPointTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDirection.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDirectionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepVector.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendVectorTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAxis2Placement3D.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAxis2Placement3DTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPlane.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPlaneTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeometricSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeometricSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeometricCurveSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeometricCurveSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPointSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPointSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPath.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPathTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOpenPath.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOpenPathTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedPath.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedPathTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSubpath.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSubpathTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEdgeLoop.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEdgeLoopTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPolyLoop.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPolyLoopTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConnectedEdgeSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConnectedEdgeSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEdgeBasedWireframeModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEdgeBasedWireframeModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepShellBasedWireframeModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShellBasedWireframeModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepWireShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendWireShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepVertexShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendVertexShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepVertexLoop.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendVertexLoopTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedEdge.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedEdgeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEdgeCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEdgeCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepVertexPoint.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendVertexPointTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAdvancedFace.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAdvancedFaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepFaceSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendFaceSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedFace.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedFaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConnectedFaceSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConnectedFaceSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConnectedFaceSubSet.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConnectedFaceSubSetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOpenShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOpenShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfacedOpenShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfacedOpenShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepClosedShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendClosedShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedOpenShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedOpenShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedClosedShell.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedClosedShellTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepFaceBasedSurfaceModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendFaceBasedSurfaceModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepShellBasedSurfaceModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShellBasedSurfaceModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepManifoldSolidBrep.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendManifoldSolidBrepTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBrepWithVoids.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBrepWithVoidsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSweptAreaSolid.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSweptAreaSolidTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSweptDiskSolid.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSweptDiskSolidTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepComplexClippingResult.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendComplexClippingResultTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSolidReplica.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSolidReplicaTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepHalfSpaceSolid.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendHalfSpaceSolidTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCsgSolid.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCsgSolidTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCsgPrimitive.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCsgPrimitiveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProfileDef.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProfileDefTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConicCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConicCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBSplineCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBSplineCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBSplineCurveWithKnots.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBSplineCurveWithKnotsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRationalBSplineCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRationalBSplineCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBezierCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBezierCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepUniformCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendUniformCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepQuasiUniformCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendQuasiUniformCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPiecewiseBezierCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPiecewiseBezierCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepLine.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendLineTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCircle.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCircleTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEllipse.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEllipseTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPolyline.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPolylineTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTrimmedCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTrimmedCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOffsetCurve2D.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOffsetCurve2DTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOffsetCurve3D.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOffsetCurve3DTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPcurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPcurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDegeneratePcurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDegeneratePcurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSeamCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSeamCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCompositeCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCompositeCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCompositeCurveOnSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCompositeCurveOnSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCompositeCurveSegment.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCompositeCurveSegmentTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCylindricalSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCylindricalSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConicalSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConicalSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSphericalSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSphericalSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepToroidalSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendToroidalSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceOfLinearExtrusion.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceOfLinearExtrusionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceOfRevolution.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceOfRevolutionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRectangularTrimmedSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRectangularTrimmedSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCurveBoundedSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCurveBoundedSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOrientedSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOrientedSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepOffsetSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendOffsetSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBSplineSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBSplineSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBSplineSurfaceWithKnots.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBSplineSurfaceWithKnotsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRationalBSplineSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRationalBSplineSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBezierSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBezierSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepUniformSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendUniformSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepQuasiUniformSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendQuasiUniformSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPiecewiseBezierSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPiecewiseBezierSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepFace.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendFaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBoundedCurve.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBoundedCurveTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBoundedSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBoundedSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurface.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepMeasureRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendMeasureRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDescriptiveRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDescriptiveRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepValueRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendValueRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSurfaceModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSurfaceModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepSolidModel.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendSolidModelTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGeometricRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeometricRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTopologicalRepresentationItem.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTopologicalRepresentationItemTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepMeasureWithUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendMeasureWithUnitTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepTypedMeasureWithUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendTypedMeasureWithUnitTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepUncertaintyMeasureWithUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendUncertaintyMeasureWithUnitTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConversionBasedUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConversionBasedUnitTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepConversionBasedUnitWithOffset.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendConversionBasedUnitWithOffsetTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDerivedUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDerivedUnitTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDerivedUnitElement.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDerivedUnitElementTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepNamedUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepNamedUnit unit = (StepNamedUnit) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            }),
            rule(StepSiUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepSiUnit unit = (StepSiUnit) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            }),
            rule(StepContextDependentUnit.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepContextDependentUnit unit = (StepContextDependentUnit) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            }),
            rule(StepRepresentationContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepRepresentationContext context = (StepRepresentationContext) definition;
                        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            }),
            rule(StepGeometricRepresentationContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGeometricRepresentationContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGlobalUnitAssignedContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGlobalUnitAssignedContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepGlobalUncertaintyAssignedContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendGlobalUncertaintyAssignedContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAddress.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAddressTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepCharacterizedObject.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendCharacterizedObjectTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepDimensionalExponents.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendDimensionalExponentsTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepVertex.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendVertexTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepEdge.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendEdgeTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAbstractVariable.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAbstractVariableTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRowVariable.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRowVariableTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepScalarVariable.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendScalarVariableTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepForwardChainingRulePremise.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendForwardChainingRulePremiseTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBackChainingRuleBody.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBackChainingRuleBodyTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPropertyDefinitionRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPropertyDefinitionRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepActionPropertyRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendActionPropertyRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepContactRatioRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendContactRatioRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepKinematicPropertyDefinitionRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendKinematicPropertyDefinitionRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepKinematicPropertyMechanismRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendKinematicPropertyMechanismRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepKinematicPropertyRepresentationRelation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendKinematicPropertyRepresentationRelationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepKinematicPropertyTopologyRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendKinematicPropertyTopologyRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepResourcePropertyRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendResourcePropertyRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAttributeAssertion.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendAttributeAssertionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepShapeDefinitionRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShapeDefinitionRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApplicationProtocolDefinition.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApplicationProtocolDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductDefinitionContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepApplicationContext.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendApplicationContextTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductRelatedProductCategory.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductRelatedProductCategoryTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepProductDefinitionEffectivity.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendProductDefinitionEffectivityDefinitionTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRepresentationRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRepresentationRelationshipTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepShapeRepresentationRelationship.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendShapeRepresentationRelationshipTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepContextDependentShapeRepresentation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendContextDependentShapeRepresentationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepRepresentationRelationshipWithTransformation.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendRepresentationRelationshipWithTransformationTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBoxDomain.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBoxDomainTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBooleanClippingResult.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBooleanClippingResultTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepBooleanResult.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendBooleanResultTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedMarker.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedMarkerTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepPreDefinedPointMarkerSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        appendPreDefinedPointMarkerSymbolTargets(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
            }),
            rule(StepAnnotationSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                annotationSymbol.mappingSource(),
                                annotationSymbol.mappingTarget(),
                                instanceIdsByTargetId
                        );
            }),
            rule(StepAnnotationText.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepAnnotationText annotationText = (StepAnnotationText) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                annotationText.mappingSource(),
                                annotationText.mappingTarget(),
                                instanceIdsByTargetId
                        );
            }),
            rule(StepAnnotationTextCharacter.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                annotationTextCharacter.mappingSource(),
                                annotationTextCharacter.mappingTarget(),
                                instanceIdsByTargetId
                        );
            }),
            rule(StepUserDefinedCurveFont.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                curveFont.mappingSource(),
                                curveFont.mappingTarget(),
                                instanceIdsByTargetId
                        );
            }),
            rule(StepUserDefinedMarker.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepUserDefinedMarker marker = (StepUserDefinedMarker) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                marker.mappingSource(),
                                marker.mappingTarget(),
                                instanceIdsByTargetId
                        );
            }),
            rule(StepUserDefinedTerminatorSymbol.class, (
                    targetsByUsageId,
                    identifiedItem,
                    definition,
                    resolved,
                    instanceIdsByTargetId
            ) -> {
                        StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) definition;
                        appendMappedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                symbol.mappingSource(),
                                symbol.mappingTarget(),
                                instanceIdsByTargetId
                        );
            })
    );

    private static void dispatchSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (SemanticDefinitionRule rule : SEMANTIC_DEFINITION_RULES) {
            if (rule.matches(definition)) {
                rule.handler().handle(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
                return;
            }
        }
    }



    private static void appendAnnotationOccurrenceRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationOccurrenceRelationship)) {
            return;
        }
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatingAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatedAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
    }


    private static void appendDraughtingCalloutRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingCalloutRelationship)) {
            return;
        }
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatingCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatedCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
    }


    private static void appendPropertyDefinitionRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPropertyDefinitionRelationship)) {
            return;
        }
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatingPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatedPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendPropertyDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPropertyDefinition)) {
            return;
        }
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) definition;
            appendPropertyDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendGeneralPropertyRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeneralPropertyRelationship)) {
            return;
        }
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendShapeAspectRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShapeAspectRelationship)) {
            return;
        }
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendProductRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductRelationship)) {
            return;
        }
            StepProductRelationship relationship = (StepProductRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendProductDefinitionFormationRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductDefinitionFormationRelationship)) {
            return;
        }
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendGroupRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGroupRelationship)) {
            return;
        }
            StepGroupRelationship relationship = (StepGroupRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendDocumentRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDocumentRelationship)) {
            return;
        }
            StepDocumentRelationship relationship = (StepDocumentRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendOrganizationRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrganizationRelationship)) {
            return;
        }
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendEffectivityRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEffectivityRelationship)) {
            return;
        }
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendProductCategoryRelationshipDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductCategoryRelationship)) {
            return;
        }
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.category(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.subCategory(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendGroupTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGroup)) {
            return;
        }
            StepGroup group = (StepGroup) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
            appendGroupRelationshipTargets(targetsByUsageId, identifiedItem.id(), group, resolved, instanceIdsByTargetId);
    }


    private static void appendDocumentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDocument)) {
            return;
        }
            StepDocument document = (StepDocument) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    document.kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), document, resolved, instanceIdsByTargetId);
    }


    private static void appendDocumentReferenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDocumentReference)) {
            return;
        }
            StepDocumentReference reference = (StepDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedDocumentReferenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedDocumentReference)) {
            return;
        }
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    reference.assignedDocument().kind(),
                    definitionTypeName(reference.assignedDocument().kind()),
                    reference.assignedDocument().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
    }


    private static void appendApprovalAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApprovalAssignment)) {
            return;
        }
            StepApprovalAssignment assignment = (StepApprovalAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedApproval(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval().status(), instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
    }


    private static void appendSecurityClassificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSecurityClassificationAssignment)) {
            return;
        }
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedSecurityClassification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification().securityLevel(), instanceIdsByTargetId);
    }


    private static void appendContractAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepContractAssignment)) {
            return;
        }
            StepContractAssignment assignment = (StepContractAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedContract(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract().kind(), instanceIdsByTargetId);
    }


    private static void appendCertificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCertificationAssignment)) {
            return;
        }
            StepCertificationAssignment assignment = (StepCertificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedCertification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification().kind(), instanceIdsByTargetId);
    }


    private static void appendPersonAndOrganizationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPersonAndOrganizationAssignment)) {
            return;
        }
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedPersonAndOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().person(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().organization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
    }


    private static void appendOrganizationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrganizationAssignment)) {
            return;
        }
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
    }


    private static void appendLanguageAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepLanguageAssignment)) {
            return;
        }
            StepLanguageAssignment assignment = (StepLanguageAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedLanguage(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedLanguage(), instanceIdsByTargetId);
    }


    private static void appendGroupAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGroupAssignment)) {
            return;
        }
            StepGroupAssignment assignment = (StepGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedGroup(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedGroup(), instanceIdsByTargetId);
    }


    private static void appendClassificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepClassificationAssignment)) {
            return;
        }
            StepClassificationAssignment assignment = (StepClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedClass(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedClass(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
    }


    private static void appendDateAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDateAssignment)) {
            return;
        }
            StepDateAssignment assignment = (StepDateAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDate(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDate(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
    }


    private static void appendDateTimeAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDateTimeAssignment)) {
            return;
        }
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDateAndTime(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().dateComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent().zone(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
    }


    private static void appendIdentificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepIdentificationAssignment)) {
            return;
        }
            StepIdentificationAssignment assignment = (StepIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
    }


    private static void appendExternalIdentificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepExternalIdentificationAssignment)) {
            return;
        }
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
    }


    private static void appendEffectivityTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEffectivity)) {
            return;
        }
            StepEffectivity effectivity = (StepEffectivity) definition;
            appendProductDefinitionEffectivityTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    effectivity,
                    resolved,
                    instanceIdsByTargetId
            );
            appendEffectivityRelationshipTargets(targetsByUsageId, identifiedItem.id(), effectivity, resolved, instanceIdsByTargetId);
    }


    private static void appendExternalSourceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepExternalSource)) {
            return;
        }
            StepExternalSource source = (StepExternalSource) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    source,
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
    }


    private static void appendExternallyDefinedItemDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepExternallyDefinedItem)) {
            return;
        }
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
    }


    private static void appendDocumentUsageConstraintTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDocumentUsageConstraint)) {
            return;
        }
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source().kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
    }


    private static void appendRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRepresentation)) {
            return;
        }
            StepRepresentation representation = (StepRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    representation,
                    resolved,
                    instanceIdsByTargetId
            );
            if (representation.context() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        representation.context(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            for (StepEntity item : representation.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendShapeAspectOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShapeAspectOccurrence)) {
            return;
        }
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendProductDefinitionRelationshipRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductDefinitionRelationshipRelationship)) {
            return;
        }
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relating(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.related(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendApprovalPersonOrganizationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApprovalPersonOrganization)) {
            return;
        }
            StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) definition;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval().status(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
    }


    private static void appendApprovalDateTimeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApprovalDateTime)) {
            return;
        }
            StepApprovalDateTime assignment = (StepApprovalDateTime) definition;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().dateComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval().status(),
                    instanceIdsByTargetId
            );
    }


    private static void appendCalendarDateTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCalendarDate)) {
            return;
        }
            StepCalendarDate calendarDate = (StepCalendarDate) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, calendarDate, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, calendarDate, resolved, instanceIdsByTargetId);
    }


    private static void appendNameAttributeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepNameAttribute)) {
            return;
        }
            StepNameAttribute attribute = (StepNameAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.namedItem(), resolved, instanceIdsByTargetId);
    }


    private static void appendDescriptionAttributeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDescriptionAttribute)) {
            return;
        }
            StepDescriptionAttribute attribute = (StepDescriptionAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.describedItem(), resolved, instanceIdsByTargetId);
    }


    private static void appendIdAttributeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepIdAttribute)) {
            return;
        }
            StepIdAttribute attribute = (StepIdAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.identifiedItem(), resolved, instanceIdsByTargetId);
    }


    private static void appendNameAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepNameAssignment)) {
            return;
        }
            StepNameAssignment assignment = (StepNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
    }


    private static void appendAppliedNameAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedNameAssignment)) {
            return;
        }
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendDateAndTimeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDateAndTime)) {
            return;
        }
            StepDateAndTime dateAndTime = (StepDateAndTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, dateAndTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.dateComponent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.timeComponent(), resolved, instanceIdsByTargetId);
    }


    private static void appendLocalTimeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepLocalTime)) {
            return;
        }
            StepLocalTime localTime = (StepLocalTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, localTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, localTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, localTime.zone(), resolved, instanceIdsByTargetId);
    }


    private static void appendCoordinatedUniversalTimeOffsetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCoordinatedUniversalTimeOffset)) {
            return;
        }
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, zone, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, zone, resolved, instanceIdsByTargetId);
    }


    private static void appendApprovalStatusTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApprovalStatus)) {
            return;
        }
            StepApprovalStatus status = (StepApprovalStatus) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, status, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, status, resolved, instanceIdsByTargetId);
    }


    private static void appendSecurityClassificationLevelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSecurityClassificationLevel)) {
            return;
        }
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, level, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, level, resolved, instanceIdsByTargetId);
    }


    private static void appendContractTypeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepContractType)) {
            return;
        }
            StepContractType kind = (StepContractType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
    }


    private static void appendCertificationTypeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCertificationType)) {
            return;
        }
            StepCertificationType kind = (StepCertificationType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
    }


    private static void appendApprovalRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApprovalRole)) {
            return;
        }
            StepApprovalRole role = (StepApprovalRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendOrganizationRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrganizationRole)) {
            return;
        }
            StepOrganizationRole role = (StepOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendPersonAndOrganizationRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPersonAndOrganizationRole)) {
            return;
        }
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendClassificationRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepClassificationRole)) {
            return;
        }
            StepClassificationRole role = (StepClassificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendDateRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDateRole)) {
            return;
        }
            StepDateRole role = (StepDateRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendDateTimeRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDateTimeRole)) {
            return;
        }
            StepDateTimeRole role = (StepDateTimeRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendIdentificationRoleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepIdentificationRole)) {
            return;
        }
            StepIdentificationRole role = (StepIdentificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
    }


    private static void appendDocumentTypeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDocumentType)) {
            return;
        }
            StepDocumentType kind = (StepDocumentType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
    }


    private static void appendApprovalTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApproval)) {
            return;
        }
            StepApproval approval = (StepApproval) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, approval, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, approval.status(), resolved, instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
    }


    private static void appendSecurityClassificationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSecurityClassification)) {
            return;
        }
            StepSecurityClassification classification = (StepSecurityClassification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, classification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, classification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, classification.securityLevel(), resolved, instanceIdsByTargetId);
    }


    private static void appendContractTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepContract)) {
            return;
        }
            StepContract contract = (StepContract) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, contract, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, contract, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, contract.kind(), resolved, instanceIdsByTargetId);
    }


    private static void appendCertificationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCertification)) {
            return;
        }
            StepCertification certification = (StepCertification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, certification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, certification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, certification.kind(), resolved, instanceIdsByTargetId);
    }


    private static void appendPersonTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPerson)) {
            return;
        }
            StepPerson person = (StepPerson) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, person, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, person, resolved, instanceIdsByTargetId);
    }


    private static void appendPersonAndOrganizationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPersonAndOrganization)) {
            return;
        }
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, personAndOrganization, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.person(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.organization(), resolved, instanceIdsByTargetId);
    }


    private static void appendLanguageTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepLanguage)) {
            return;
        }
            StepLanguage language = (StepLanguage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, language, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, language, resolved, instanceIdsByTargetId);
    }


    private static void appendAppliedClassificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedClassificationAssignment)) {
            return;
        }
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedDateAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedDateAssignment)) {
            return;
        }
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDate(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedDateTimeAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedDateTimeAssignment)) {
            return;
        }
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedApprovalAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedApprovalAssignment)) {
            return;
        }
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedApproval().status(),
                    definitionTypeName(assignment.assignedApproval().status()),
                    assignment.assignedApproval().status().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval().status(),
                    instanceIdsByTargetId
            );
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
    }


    private static void appendAppliedSecurityClassificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedSecurityClassificationAssignment)) {
            return;
        }
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedSecurityClassification().securityLevel(),
                    definitionTypeName(assignment.assignedSecurityClassification().securityLevel()),
                    assignment.assignedSecurityClassification().securityLevel().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification().securityLevel(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedContractAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedContractAssignment)) {
            return;
        }
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedContract().kind(),
                    definitionTypeName(assignment.assignedContract().kind()),
                    assignment.assignedContract().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract().kind(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedCertificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedCertificationAssignment)) {
            return;
        }
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedCertification().kind(),
                    definitionTypeName(assignment.assignedCertification().kind()),
                    assignment.assignedCertification().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification().kind(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedPersonAndOrganizationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedPersonAndOrganizationAssignment)) {
            return;
        }
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedOrganizationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedOrganizationAssignment)) {
            return;
        }
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedOrganization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedLanguageAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedLanguageAssignment)) {
            return;
        }
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedLanguage(),
                    definitionTypeName(assignment.assignedLanguage()),
                    assignment.assignedLanguage().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedLanguage(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedGroupAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedGroupAssignment)) {
            return;
        }
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedGroup(),
                    instanceIdsByTargetId
            );
    }


    private static void appendAppliedIdentificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedIdentificationAssignment)) {
            return;
        }
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendAppliedExternalIdentificationAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAppliedExternalIdentificationAssignment)) {
            return;
        }
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendAnnotationCurveOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationCurveOccurrence)) {
            return;
        }
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationFillAreaTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationFillArea)) {
            return;
        }
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, fillArea, instanceIdsByTargetId);
            for (StepEntity boundary : fillArea.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendAnnotationFillAreaOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationFillAreaOccurrence)) {
            return;
        }
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.fillStyleTarget(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationPlaceholderOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationPlaceholderOccurrence)) {
            return;
        }
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationPointOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationPointOccurrence)) {
            return;
        }
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationSymbolOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationSymbolOccurrence)) {
            return;
        }
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationSubfigureOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationSubfigureOccurrence)) {
            return;
        }
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationTextOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationTextOccurrence)) {
            return;
        }
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.position(), resolved, instanceIdsByTargetId);
    }


    private static void appendDraughtingAnnotationOccurrenceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingAnnotationOccurrence)) {
            return;
        }
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendTerminatorSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTerminatorSymbol)) {
            return;
        }
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.annotatedCurve(), resolved, instanceIdsByTargetId);
    }


    private static void appendPresentationStyleAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPresentationStyleAssignment)) {
            return;
        }
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity style : assignment.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendSurfaceStyleUsageTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleUsage)) {
            return;
        }
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, usage, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, usage.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceSideStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceSideStyle)) {
            return;
        }
            StepSurfaceSideStyle style = (StepSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepEntity component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendSurfaceStyleFillAreaTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleFillArea)) {
            return;
        }
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.fillStyle(), resolved, instanceIdsByTargetId);
    }


    private static void appendFillAreaStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepFillAreaStyle)) {
            return;
        }
            StepFillAreaStyle style = (StepFillAreaStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepFillAreaStyleColour component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendFillAreaStyleColourTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepFillAreaStyleColour)) {
            return;
        }
            StepFillAreaStyleColour style = (StepFillAreaStyleColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
    }


    private static void appendCurveStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCurveStyle)) {
            return;
        }
            StepCurveStyle style = (StepCurveStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.curveFont(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleBoundaryTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleBoundary)) {
            return;
        }
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleParameterLineTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleParameterLine)) {
            return;
        }
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleControlGridTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleControlGrid)) {
            return;
        }
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleSegmentationCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleSegmentationCurve)) {
            return;
        }
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleSilhouetteTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleSilhouette)) {
            return;
        }
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
    }


    private static void appendCharacterGlyphStyleStrokeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCharacterGlyphStyleStroke)) {
            return;
        }
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.strokeStyle(), resolved, instanceIdsByTargetId);
    }


    private static void appendCharacterGlyphStyleOutlineTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCharacterGlyphStyleOutline)) {
            return;
        }
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
    }


    private static void appendCharacterGlyphStyleOutlineWithCharacteristicsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCharacterGlyphStyleOutlineWithCharacteristics)) {
            return;
        }
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characteristics(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyle)) {
            return;
        }
            StepTextStyle style = (StepTextStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleWithSpacingTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyleWithSpacing)) {
            return;
        }
            StepTextStyleWithSpacing style = (StepTextStyleWithSpacing) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleWithBoxCharacteristicsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyleWithBoxCharacteristics)) {
            return;
        }
            StepTextStyleWithBoxCharacteristics style = (StepTextStyleWithBoxCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleWithJustificationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyleWithJustification)) {
            return;
        }
            StepTextStyleWithJustification style = (StepTextStyleWithJustification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleWithMirrorTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyleWithMirror)) {
            return;
        }
            StepTextStyleWithMirror style = (StepTextStyleWithMirror) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.mirrorPlacement(), resolved, instanceIdsByTargetId);
    }


    private static void appendTextStyleForDefinedFontTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTextStyleForDefinedFont)) {
            return;
        }
            StepTextStyleForDefinedFont style = (StepTextStyleForDefinedFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.textColour(), resolved, instanceIdsByTargetId);
    }


    private static void appendPointStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPointStyle)) {
            return;
        }
            StepPointStyle style = (StepPointStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.marker(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
    }


    private static void appendSymbolColourTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSymbolColour)) {
            return;
        }
            StepSymbolColour style = (StepSymbolColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
    }


    private static void appendSymbolStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSymbolStyle)) {
            return;
        }
            StepSymbolStyle style = (StepSymbolStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.styleOfSymbol(), resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceStyleReflectanceAmbientDiffuseSpecularTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular)) {
            return;
        }
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.specularColour(), resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedSurfaceSideStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedSurfaceSideStyle)) {
            return;
        }
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedColourTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedColour)) {
            return;
        }
            StepPreDefinedColour colour = (StepPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
    }


    private static void appendDraughtingPreDefinedColourTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingPreDefinedColour)) {
            return;
        }
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
    }


    private static void appendColourRgbTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepColourRgb)) {
            return;
        }
            StepColourRgb colour = (StepColourRgb) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
    }


    private static void appendColourSpecificationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepColourSpecification)) {
            return;
        }
            StepColourSpecification colour = (StepColourSpecification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
    }


    private static void appendColourTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepColour)) {
            return;
        }
            StepColour colour = (StepColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedCurveFontTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedCurveFont)) {
            return;
        }
            StepPreDefinedCurveFont font = (StepPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
    }


    private static void appendDraughtingPreDefinedCurveFontTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingPreDefinedCurveFont)) {
            return;
        }
            StepDraughtingPreDefinedCurveFont font = (StepDraughtingPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedTextFontTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedTextFont)) {
            return;
        }
            StepPreDefinedTextFont font = (StepPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
    }


    private static void appendDraughtingPreDefinedTextFontTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingPreDefinedTextFont)) {
            return;
        }
            StepDraughtingPreDefinedTextFont font = (StepDraughtingPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedTerminatorSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedTerminatorSymbol)) {
            return;
        }
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedSymbol)) {
            return;
        }
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedDimensionSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedDimensionSymbol)) {
            return;
        }
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedGeometricalToleranceSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedGeometricalToleranceSymbol)) {
            return;
        }
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedItem)) {
            return;
        }
            StepPreDefinedItem item = (StepPreDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendAnnotationPlaneTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAnnotationPlane)) {
            return;
        }
            StepAnnotationPlane plane = (StepAnnotationPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : plane.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.item(), resolved, instanceIdsByTargetId);
            for (StepEntity element : plane.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendDraughtingCalloutTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDraughtingCallout)) {
            return;
        }
            StepDraughtingCallout callout = (StepDraughtingCallout) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, callout, instanceIdsByTargetId);
            for (StepEntity content : callout.contents()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, content, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendPresentationLayerAssignmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPresentationLayerAssignment)) {
            return;
        }
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity item : assignment.assignedItems()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendStyledItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepStyledItem)) {
            return;
        }
            StepStyledItem styledItem = (StepStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
    }


    private static void appendOverRidingStyledItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOverRidingStyledItem)) {
            return;
        }
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.overRiddenStyle(), resolved, instanceIdsByTargetId);
    }


    private static void appendRepresentationMapTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRepresentationMap)) {
            return;
        }
            StepRepresentationMap representationMap = (StepRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
    }


    private static void appendSymbolRepresentationMapTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSymbolRepresentationMap)) {
            return;
        }
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
    }


    private static void appendMappedItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepMappedItem)) {
            return;
        }
            StepMappedItem mappedItem = (StepMappedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingSource(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingTarget(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, mappedItem, resolved, instanceIdsByTargetId);
    }


    private static void appendGeometricReplicaTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeometricReplica)) {
            return;
        }
            StepGeometricReplica replica = (StepGeometricReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, replica, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.parent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, replica, resolved, instanceIdsByTargetId);
    }


    private static void appendItemDefinedTransformationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepItemDefinedTransformation)) {
            return;
        }
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem1(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem2(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
    }


    private static void appendCartesianTransformationOperatorTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCartesianTransformationOperator)) {
            return;
        }
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            if (transformation.axis1() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis1(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis2() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis2(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis3() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis3(), resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.localOrigin(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
    }


    private static void appendAxis1PlacementTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAxis1Placement)) {
            return;
        }
            StepAxis1Placement placement = (StepAxis1Placement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
    }


    private static void appendAxis2Placement2DTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAxis2Placement2D)) {
            return;
        }
            StepAxis2Placement2D placement = (StepAxis2Placement2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
    }


    private static void appendCartesianPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCartesianPoint)) {
            return;
        }
            StepCartesianPoint point = (StepCartesianPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
    }


    private static void appendPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPoint)) {
            return;
        }
            StepPoint point = (StepPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
    }


    private static void appendDirectionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDirection)) {
            return;
        }
            StepDirection direction = (StepDirection) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, direction, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, direction, resolved, instanceIdsByTargetId);
    }


    private static void appendVectorTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepVector)) {
            return;
        }
            StepVector vector = (StepVector) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vector, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vector.orientation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vector, resolved, instanceIdsByTargetId);
    }


    private static void appendAxis2Placement3DTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAxis2Placement3D)) {
            return;
        }
            StepAxis2Placement3D placement = (StepAxis2Placement3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, placement, resolved, instanceIdsByTargetId);
    }


    private static void appendPlaneTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPlane)) {
            return;
        }
            StepPlane plane = (StepPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, plane, resolved, instanceIdsByTargetId);
    }


    private static void appendGeometricSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeometricSet)) {
            return;
        }
            StepGeometricSet set = (StepGeometricSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
    }


    private static void appendGeometricCurveSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeometricCurveSet)) {
            return;
        }
            StepGeometricCurveSet set = (StepGeometricCurveSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
    }


    private static void appendPointSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPointSet)) {
            return;
        }
            StepPointSet set = (StepPointSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity point : set.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
    }


    private static void appendPathTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPath)) {
            return;
        }
            StepPath path = (StepPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
    }


    private static void appendOpenPathTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOpenPath)) {
            return;
        }
            StepOpenPath path = (StepOpenPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedPathTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedPath)) {
            return;
        }
            StepOrientedPath path = (StepOrientedPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.pathElement(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
    }


    private static void appendSubpathTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSubpath)) {
            return;
        }
            StepSubpath path = (StepSubpath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.parentPath(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
    }


    private static void appendEdgeLoopTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEdgeLoop)) {
            return;
        }
            StepEdgeLoop loop = (StepEdgeLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepOrientedEdge edge : loop.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
    }


    private static void appendPolyLoopTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPolyLoop)) {
            return;
        }
            StepPolyLoop loop = (StepPolyLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepCartesianPoint point : loop.polygon()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
    }


    private static void appendConnectedEdgeSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConnectedEdgeSet)) {
            return;
        }
            StepConnectedEdgeSet set = (StepConnectedEdgeSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity edge : set.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
    }


    private static void appendEdgeBasedWireframeModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEdgeBasedWireframeModel)) {
            return;
        }
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepConnectedEdgeSet boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendShellBasedWireframeModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShellBasedWireframeModel)) {
            return;
        }
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendWireShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepWireShell)) {
            return;
        }
            StepWireShell shell = (StepWireShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepLoop loop : shell.loops()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendVertexShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepVertexShell)) {
            return;
        }
            StepVertexShell shell = (StepVertexShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.extent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendVertexLoopTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepVertexLoop)) {
            return;
        }
            StepVertexLoop loop = (StepVertexLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop.loopVertex(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedEdgeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedEdge)) {
            return;
        }
            StepOrientedEdge edge = (StepOrientedEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
    }


    private static void appendEdgeCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEdgeCurve)) {
            return;
        }
            StepEdgeCurve edge = (StepEdgeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.start(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.end(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeGeometry(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
    }


    private static void appendVertexPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepVertexPoint)) {
            return;
        }
            StepVertexPoint vertex = (StepVertexPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vertex.point(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
    }


    private static void appendAdvancedFaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAdvancedFace)) {
            return;
        }
            StepAdvancedFace face = (StepAdvancedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
    }


    private static void appendFaceSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepFaceSurface)) {
            return;
        }
            StepFaceSurface face = (StepFaceSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedFaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedFace)) {
            return;
        }
            StepOrientedFace face = (StepOrientedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
    }


    private static void appendConnectedFaceSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConnectedFaceSet)) {
            return;
        }
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
    }


    private static void appendConnectedFaceSubSetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConnectedFaceSubSet)) {
            return;
        }
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet.parentFaceSet(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
    }


    private static void appendOpenShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOpenShell)) {
            return;
        }
            StepOpenShell shell = (StepOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfacedOpenShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfacedOpenShell)) {
            return;
        }
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendClosedShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepClosedShell)) {
            return;
        }
            StepClosedShell shell = (StepClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedOpenShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedOpenShell)) {
            return;
        }
            StepOrientedOpenShell shell = (StepOrientedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.openShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedClosedShellTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedClosedShell)) {
            return;
        }
            StepOrientedClosedShell shell = (StepOrientedClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.closedShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
    }


    private static void appendFaceBasedSurfaceModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepFaceBasedSurfaceModel)) {
            return;
        }
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity faceSet : model.faceSets()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendShellBasedSurfaceModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShellBasedSurfaceModel)) {
            return;
        }
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity shell : model.shells()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendManifoldSolidBrepTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepManifoldSolidBrep)) {
            return;
        }
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendBrepWithVoidsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBrepWithVoids)) {
            return;
        }
            StepBrepWithVoids solid = (StepBrepWithVoids) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            for (StepEntity voidShell : solid.voids()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, voidShell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendSweptAreaSolidTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSweptAreaSolid)) {
            return;
        }
            StepSweptAreaSolid solid = (StepSweptAreaSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptArea(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.position(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweepReference(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendSweptDiskSolidTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSweptDiskSolid)) {
            return;
        }
            StepSweptDiskSolid solid = (StepSweptDiskSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendComplexClippingResultTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepComplexClippingResult)) {
            return;
        }
            StepComplexClippingResult solid = (StepComplexClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.secondOperand(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendSolidReplicaTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSolidReplica)) {
            return;
        }
            StepSolidReplica solid = (StepSolidReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.parentSolid(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendHalfSpaceSolidTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepHalfSpaceSolid)) {
            return;
        }
            StepHalfSpaceSolid solid = (StepHalfSpaceSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.baseSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.enclosure(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendCsgSolidTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCsgSolid)) {
            return;
        }
            StepCsgSolid solid = (StepCsgSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.treeRootExpression(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
    }


    private static void appendCsgPrimitiveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCsgPrimitive)) {
            return;
        }
            StepCsgPrimitive primitive = (StepCsgPrimitive) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, primitive, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, primitive.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, primitive, resolved, instanceIdsByTargetId);
    }


    private static void appendProfileDefTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProfileDef)) {
            return;
        }
            StepProfileDef profile = (StepProfileDef) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, profile, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, profile.position(), resolved, instanceIdsByTargetId);
            for (StepEntity curve : profile.curves()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, profile, resolved, instanceIdsByTargetId);
    }


    private static void appendConicCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConicCurve)) {
            return;
        }
            StepConicCurve curve = (StepConicCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendBSplineCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBSplineCurve)) {
            return;
        }
            StepBSplineCurve curve = (StepBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendBSplineCurveWithKnotsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBSplineCurveWithKnots)) {
            return;
        }
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendRationalBSplineCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRationalBSplineCurve)) {
            return;
        }
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendBezierCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBezierCurve)) {
            return;
        }
            StepBezierCurve curve = (StepBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendUniformCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepUniformCurve)) {
            return;
        }
            StepUniformCurve curve = (StepUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendQuasiUniformCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepQuasiUniformCurve)) {
            return;
        }
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendPiecewiseBezierCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPiecewiseBezierCurve)) {
            return;
        }
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendLineTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepLine)) {
            return;
        }
            StepLine line = (StepLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, line, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.point(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.vector(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, line, resolved, instanceIdsByTargetId);
    }


    private static void appendCircleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCircle)) {
            return;
        }
            StepCircle circle = (StepCircle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, circle, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, circle.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, circle, resolved, instanceIdsByTargetId);
    }


    private static void appendEllipseTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEllipse)) {
            return;
        }
            StepEllipse ellipse = (StepEllipse) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, ellipse, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, ellipse.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, ellipse, resolved, instanceIdsByTargetId);
    }


    private static void appendCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCurve)) {
            return;
        }
            StepCurve curve = (StepCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendPolylineTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPolyline)) {
            return;
        }
            StepPolyline polyline = (StepPolyline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, polyline, instanceIdsByTargetId);
            for (StepCartesianPoint point : polyline.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, polyline, resolved, instanceIdsByTargetId);
    }


    private static void appendTrimmedCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTrimmedCurve)) {
            return;
        }
            StepTrimmedCurve curve = (StepTrimmedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendOffsetCurve2DTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOffsetCurve2D)) {
            return;
        }
            StepOffsetCurve2D curve = (StepOffsetCurve2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendOffsetCurve3DTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOffsetCurve3D)) {
            return;
        }
            StepOffsetCurve3D curve = (StepOffsetCurve3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendPcurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPcurve)) {
            return;
        }
            StepPcurve curve = (StepPcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendDegeneratePcurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDegeneratePcurve)) {
            return;
        }
            StepDegeneratePcurve curve = (StepDegeneratePcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceCurve)) {
            return;
        }
            StepSurfaceCurve curve = (StepSurfaceCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendSeamCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSeamCurve)) {
            return;
        }
            StepSeamCurve curve = (StepSeamCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendCompositeCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCompositeCurve)) {
            return;
        }
            StepCompositeCurve curve = (StepCompositeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendCompositeCurveOnSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCompositeCurveOnSurface)) {
            return;
        }
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendCompositeCurveSegmentTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCompositeCurveSegment)) {
            return;
        }
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, segment, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment.parentCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
    }


    private static void appendCylindricalSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCylindricalSurface)) {
            return;
        }
            StepCylindricalSurface surface = (StepCylindricalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendConicalSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConicalSurface)) {
            return;
        }
            StepConicalSurface surface = (StepConicalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendSphericalSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSphericalSurface)) {
            return;
        }
            StepSphericalSurface surface = (StepSphericalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendToroidalSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepToroidalSurface)) {
            return;
        }
            StepToroidalSurface surface = (StepToroidalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceOfLinearExtrusionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceOfLinearExtrusion)) {
            return;
        }
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.extrusionAxis(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceOfRevolutionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceOfRevolution)) {
            return;
        }
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.axisPosition(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendRectangularTrimmedSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRectangularTrimmedSurface)) {
            return;
        }
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendCurveBoundedSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCurveBoundedSurface)) {
            return;
        }
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            for (StepEntity boundary : surface.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendOrientedSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOrientedSurface)) {
            return;
        }
            StepOrientedSurface surface = (StepOrientedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.surfaceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendOffsetSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepOffsetSurface)) {
            return;
        }
            StepOffsetSurface surface = (StepOffsetSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendBSplineSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBSplineSurface)) {
            return;
        }
            StepBSplineSurface surface = (StepBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendBSplineSurfaceWithKnotsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBSplineSurfaceWithKnots)) {
            return;
        }
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendRationalBSplineSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRationalBSplineSurface)) {
            return;
        }
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendBezierSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBezierSurface)) {
            return;
        }
            StepBezierSurface surface = (StepBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendUniformSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepUniformSurface)) {
            return;
        }
            StepUniformSurface surface = (StepUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendQuasiUniformSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepQuasiUniformSurface)) {
            return;
        }
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendPiecewiseBezierSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPiecewiseBezierSurface)) {
            return;
        }
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendFaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepFace)) {
            return;
        }
            StepFace face = (StepFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
    }


    private static void appendBoundedCurveTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBoundedCurve)) {
            return;
        }
            StepBoundedCurve curve = (StepBoundedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
    }


    private static void appendBoundedSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBoundedSurface)) {
            return;
        }
            StepBoundedSurface surface = (StepBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurface)) {
            return;
        }
            StepSurface surface = (StepSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
    }


    private static void appendMeasureRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepMeasureRepresentationItem)) {
            return;
        }
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.unit(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendDescriptiveRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDescriptiveRepresentationItem)) {
            return;
        }
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendValueRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepValueRepresentationItem)) {
            return;
        }
            StepValueRepresentationItem item = (StepValueRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendSurfaceModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSurfaceModel)) {
            return;
        }
            StepSurfaceModel model = (StepSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendSolidModelTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepSolidModel)) {
            return;
        }
            StepSolidModel model = (StepSolidModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
    }


    private static void appendRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRepresentationItem)) {
            return;
        }
            StepRepresentationItem item = (StepRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendGeometricRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeometricRepresentationItem)) {
            return;
        }
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendTopologicalRepresentationItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTopologicalRepresentationItem)) {
            return;
        }
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
    }


    private static void appendMeasureWithUnitTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepMeasureWithUnit)) {
            return;
        }
            StepMeasureWithUnit measure = (StepMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
    }


    private static void appendTypedMeasureWithUnitTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepTypedMeasureWithUnit)) {
            return;
        }
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
    }


    private static void appendUncertaintyMeasureWithUnitTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepUncertaintyMeasureWithUnit)) {
            return;
        }
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
    }


    private static void appendConversionBasedUnitTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConversionBasedUnit)) {
            return;
        }
            StepConversionBasedUnit unit = (StepConversionBasedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
    }


    private static void appendConversionBasedUnitWithOffsetTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepConversionBasedUnitWithOffset)) {
            return;
        }
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
    }


    private static void appendDerivedUnitTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDerivedUnit)) {
            return;
        }
            StepDerivedUnit unit = (StepDerivedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            for (StepDerivedUnitElement element : unit.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendDerivedUnitElementTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDerivedUnitElement)) {
            return;
        }
            StepDerivedUnitElement element = (StepDerivedUnitElement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, element, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element.unit(), resolved, instanceIdsByTargetId);
    }


    private static void appendGeometricRepresentationContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGeometricRepresentationContext)) {
            return;
        }
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            if (context.globalUnitAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUnitAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUncertaintyAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
    }


    private static void appendGlobalUnitAssignedContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGlobalUnitAssignedContext)) {
            return;
        }
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepEntity unit : context.units()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendGlobalUncertaintyAssignedContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepGlobalUncertaintyAssignedContext)) {
            return;
        }
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepUncertaintyMeasureWithUnit uncertainty : context.uncertainties()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, uncertainty, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendAddressTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAddress)) {
            return;
        }
            StepAddress address = (StepAddress) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, address, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, address, resolved, instanceIdsByTargetId);
    }


    private static void appendCharacterizedObjectTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepCharacterizedObject)) {
            return;
        }
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, characterizedObject, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, characterizedObject, resolved, instanceIdsByTargetId);
    }


    private static void appendDimensionalExponentsTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepDimensionalExponents)) {
            return;
        }
            StepDimensionalExponents exponents = (StepDimensionalExponents) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, exponents, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, exponents, resolved, instanceIdsByTargetId);
    }


    private static void appendVertexTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepVertex)) {
            return;
        }
            StepVertex vertex = (StepVertex) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
    }


    private static void appendEdgeTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepEdge)) {
            return;
        }
            StepEdge edge = (StepEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
    }


    private static void appendAbstractVariableTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAbstractVariable)) {
            return;
        }
            StepAbstractVariable variable = (StepAbstractVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
    }


    private static void appendRowVariableTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRowVariable)) {
            return;
        }
            StepRowVariable variable = (StepRowVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
    }


    private static void appendScalarVariableTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepScalarVariable)) {
            return;
        }
            StepScalarVariable variable = (StepScalarVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
    }


    private static void appendForwardChainingRulePremiseTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepForwardChainingRulePremise)) {
            return;
        }
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
    }


    private static void appendBackChainingRuleBodyTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBackChainingRuleBody)) {
            return;
        }
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
    }


    private static void appendPropertyDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPropertyDefinitionRepresentation)) {
            return;
        }
            StepPropertyDefinitionRepresentation link = (StepPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendActionPropertyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepActionPropertyRepresentation)) {
            return;
        }
            StepActionPropertyRepresentation link = (StepActionPropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendContactRatioRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepContactRatioRepresentation)) {
            return;
        }
            StepContactRatioRepresentation link = (StepContactRatioRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendKinematicPropertyDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepKinematicPropertyDefinitionRepresentation)) {
            return;
        }
            StepKinematicPropertyDefinitionRepresentation link = (StepKinematicPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendKinematicPropertyMechanismRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepKinematicPropertyMechanismRepresentation)) {
            return;
        }
            StepKinematicPropertyMechanismRepresentation link = (StepKinematicPropertyMechanismRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendKinematicPropertyRepresentationRelationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepKinematicPropertyRepresentationRelation)) {
            return;
        }
            StepKinematicPropertyRepresentationRelation link = (StepKinematicPropertyRepresentationRelation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendKinematicPropertyTopologyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepKinematicPropertyTopologyRepresentation)) {
            return;
        }
            StepKinematicPropertyTopologyRepresentation link = (StepKinematicPropertyTopologyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendResourcePropertyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepResourcePropertyRepresentation)) {
            return;
        }
            StepResourcePropertyRepresentation link = (StepResourcePropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendAttributeAssertionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepAttributeAssertion)) {
            return;
        }
            StepAttributeAssertion assertion = (StepAttributeAssertion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assertion, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assertion.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assertion.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendShapeDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShapeDefinitionRepresentation)) {
            return;
        }
            StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendApplicationProtocolDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApplicationProtocolDefinition)) {
            return;
        }
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition.application(), resolved, instanceIdsByTargetId);
    }


    private static void appendProductContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductContext)) {
            return;
        }
            StepProductContext productContext = (StepProductContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productContext.frameOfReference(), resolved, instanceIdsByTargetId);
    }


    private static void appendProductDefinitionContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductDefinitionContext)) {
            return;
        }
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext.frameOfReference(), resolved, instanceIdsByTargetId);
    }


    private static void appendApplicationContextTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepApplicationContext)) {
            return;
        }
            StepApplicationContext applicationContext = (StepApplicationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, applicationContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, applicationContext, resolved, instanceIdsByTargetId);
    }


    private static void appendProductRelatedProductCategoryTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductRelatedProductCategory)) {
            return;
        }
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relatedCategory, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relatedCategory, resolved, instanceIdsByTargetId);
            for (StepProduct product : relatedCategory.products()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            }
    }


    private static void appendProductDefinitionEffectivityDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepProductDefinitionEffectivity)) {
            return;
        }
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, effectivity.productDefinition(), resolved, instanceIdsByTargetId);
    }


    private static void appendRepresentationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRepresentationRelationship)) {
            return;
        }
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
    }


    private static void appendShapeRepresentationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepShapeRepresentationRelationship)) {
            return;
        }
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
    }


    private static void appendContextDependentShapeRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepContextDependentShapeRepresentation)) {
            return;
        }
            StepContextDependentShapeRepresentation shapeRepresentation = (StepContextDependentShapeRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shapeRepresentation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representationRelationship(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representedProductRelation(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendRepresentationRelationshipWithTransformationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepRepresentationRelationshipWithTransformation)) {
            return;
        }
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.transformationOperator(),
                    resolved,
                    instanceIdsByTargetId
            );
    }


    private static void appendBoxDomainTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBoxDomain)) {
            return;
        }
            StepBoxDomain boxDomain = (StepBoxDomain) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain.corner(), resolved, instanceIdsByTargetId);
    }


    private static void appendBooleanClippingResultTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBooleanClippingResult)) {
            return;
        }
            StepBooleanClippingResult result = (StepBooleanClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
    }


    private static void appendBooleanResultTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepBooleanResult)) {
            return;
        }
            StepBooleanResult result = (StepBooleanResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedMarkerTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedMarker)) {
            return;
        }
            StepPreDefinedMarker marker = (StepPreDefinedMarker) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
    }


    private static void appendPreDefinedPointMarkerSymbolTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!(definition instanceof StepPreDefinedPointMarkerSymbol)) {
            return;
        }
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
    }


    static void appendCarrierDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity carrier,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                carrier,
                instanceIdsByTargetId
        );
    }


    static void appendDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity relatedDefinition,
            String viaDefinitionType,
            int viaDefinitionId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (relatedDefinition instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) relatedDefinition;
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    usageId,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        }
        for (StepEntity target : collectSemanticTargets(relatedDefinition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    viaDefinitionType,
                    viaDefinitionId
            );
        }
    }


    static void appendNestedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity nestedDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (nestedDefinition == null) {
            return;
        }
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                instanceIdsByTargetId
        );
        appendSemanticDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                resolved,
                instanceIdsByTargetId
        );
    }


    static void appendRelationshipSemanticTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            String relationshipType,
            int relationshipId,
            StepEntity source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity target : collectSemanticTargets(source, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    relationshipType,
                    relationshipId
            );
        }
    }


    static void propagateCalloutTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepDraughtingCalloutRelationship relationship
    ) {
        List<PmiTargetPayload> relatingTargets = targetsByUsageId.get(relationship.relatingCallout().id());
        if (relatingTargets == null || relatingTargets.isEmpty()) {
            return;
        }
        List<PmiTargetPayload> relatedTargets = targetsByUsageId.computeIfAbsent(
                relationship.relatedCallout().id(),
                ignored -> new ArrayList<>()
        );
        for (PmiTargetPayload target : relatingTargets) {
            PmiTargetPayload propagated = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    target.instanceIds(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    target.viaUsageType(),
                    target.viaUsageId(),
                    target.viaDefinitionType(),
                    target.viaDefinitionId()
            );
            if (!relatedTargets.contains(propagated)) {
                relatedTargets.add(propagated);
            }
        }
    }


    static void appendIndirectPropertyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == definition.id()) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, propertyDefinition, instanceIdsByTargetId);
                appendPropertyRepresentationLinkTargets(
                        targetsByUsageId,
                        identifiedItem,
                        propertyDefinition,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendProductRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProduct product,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) candidate;
                if (relationship.relatingProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else if (candidate instanceof StepProductRelatedProductCategory
                    && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
                StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relatedCategory, relationshipTypeName(relatedCategory), relatedCategory.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendProductDefinitionFormationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinitionFormation formation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductDefinitionFormationRelationship)) {
                continue;
            }
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
            if (relationship.relatingFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendProductDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductDefinitionRelationship
                    || candidate instanceof StepNextAssemblyUsageOccurrence)) {
                continue;
            }
            if (candidate instanceof StepProductDefinitionRelationship) {
                StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) candidate;
                if (relationship.relatingProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else {
                StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) candidate;
                if (occurrence.relatingProductDefinition().id() == productDefinition.id()
                        || occurrence.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, occurrence, relationshipTypeName(occurrence), occurrence.id(), resolved, instanceIdsByTargetId);
                }
            }
        }
    }


    static void appendAttachedRepresentationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationship
                    && referencesRepresentation(((StepRepresentationRelationship) candidate).rep1(),
                            ((StepRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepRepresentationRelationship relationship = (StepRepresentationRelationship) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && referencesRepresentation(((StepRepresentationRelationshipWithTransformation) candidate).rep1(),
                            ((StepRepresentationRelationshipWithTransformation) candidate).rep2(), representation.id())) {
                StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed,
                        relationshipTypeName(transformed),
                        transformed.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed.transformationOperator(),
                        definitionTypeName(transformed.transformationOperator()),
                        transformed.transformationOperator().id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem1(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem2(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepShapeRepresentationRelationship
                    && referencesRepresentation(((StepShapeRepresentationRelationship) candidate).rep1(),
                            ((StepShapeRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static boolean referencesRepresentation(
            StepRepresentation rep1,
            StepRepresentation rep2,
            int representationId
    ) {
        return rep1.id() == representationId || rep2.id() == representationId;
    }


    static void appendPropertyDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepPropertyDefinitionRelationship)) {
                continue;
            }
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
            if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static StepRepresentation propertyRepresentationLinkRepresentation(StepEntity candidate, int propertyDefinitionId) {
        return usedRepresentationIfLinkMatches(candidate, propertyDefinitionId);
    }


    static void appendGeneralPropertyRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGeneralProperty generalProperty,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGeneralPropertyRelationship)) {
                continue;
            }
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
            if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendShapeAspectRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepShapeAspect shapeAspect,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepShapeAspectRelationship)) {
                continue;
            }
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) candidate;
            if (relationship.relatingShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendGroupRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGroup group,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGroupRelationship)) {
                continue;
            }
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
            if (relationship.relatingGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendDocumentRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepDocument document,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepDocumentRelationship)) {
                continue;
            }
            StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
            if (relationship.relatingDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static Set<StepEntity> collectTargetsReferencingEntity(
            int referencedId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return collectTargetsReferencingEntity(referencedId, resolved, visiting, new PmiEntityIndex(resolved));
    }

    private static Set<StepEntity> collectTargetsReferencingEntity(
            int referencedId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            PmiEntityIndex index
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepPropertyDefinition propertyDefinition : index.propertyDefinitionsReferencing(referencedId)) {
            targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting, index));
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectRepresentationTargetsFromRelationship(StepEntity relationship) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (relationship instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship representationRelationship = (StepRepresentationRelationship) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation representationRelationship = (StepRepresentationRelationshipWithTransformation) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship representationRelationship = (StepShapeRepresentationRelationship) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        }
        return Set.copyOf(targets);
    }


    public static String definitionTypeName(StepEntity definition) {
        return StepTypeNameResolver.definitionTypeName(definition);
    }


    static String relationshipTypeName(StepEntity relationship) {
        return StepTypeNameResolver.relationshipTypeName(relationship);
    }

    // ========================================================================
    static void appendApprovalDecorationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepApproval approval,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
                StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().person(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().organization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.role(),
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepApprovalDateTime
                    && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
                StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().dateComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent().zone(),
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendEffectivityRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepEffectivityRelationship)) {
                continue;
            }
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
            if (relationship.relatingEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendExternallyDefinedItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<Integer> linkedSourceIds = new LinkedHashSet<>();
        linkedSourceIds.add(source.id());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepExternalSourceRelationship)) {
                    continue;
                }
                StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
                int relatingId = relationship.relatingSource().id();
                int relatedId = relationship.relatedSource().id();
                if (linkedSourceIds.contains(relatingId) && linkedSourceIds.add(relatedId)) {
                    changed = true;
                }
                if (linkedSourceIds.contains(relatedId) && linkedSourceIds.add(relatingId)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepExternallyDefinedItem
                    && linkedSourceIds.contains(((StepExternallyDefinedItem) candidate).source().id())) {
                StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            }
        }
    }


    static void appendExternalSourceRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepExternalSourceRelationship)) {
                continue;
            }
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
            if (relationship.relatingSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
            if (relationship.relatedSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
        }
    }


    static void appendMappedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity mappingSource,
            StepEntity mappingTarget,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                mappingSource,
                instanceIdsByTargetId
        );
        if (mappingSource instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) mappingSource;
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        } else if (mappingSource instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) mappingSource;
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        }
        appendPlacementDefinitionTargets(targetsByUsageId, identifiedItem, mappingTarget, instanceIdsByTargetId);
    }


    static void appendOccurrenceRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepNextAssemblyUsageOccurrence occurrence,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrence.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendOrganizationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepOrganization organization,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepOrganizationRelationship)) {
                continue;
            }
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
            if (relationship.relatingOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendPointMarkerStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            int markerId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPointStyle && ((StepPointStyle) candidate).marker().id() == markerId) {
            StepPointStyle pointStyle = (StepPointStyle) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle.colour(),
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendProductCategoryRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductCategory category,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductCategoryRelationship)) {
                continue;
            }
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
            if (relationship.category().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.subCategory(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.subCategory().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.category(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }


    static void appendProductDefinitionEffectivityTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<String> linkedEffectivityNames = new LinkedHashSet<>();
        linkedEffectivityNames.add(effectivity.name());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepEffectivityRelationship)) {
                    continue;
                }
                StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
                String relatingName = relationship.relatingEffectivity().name();
                String relatedName = relationship.relatedEffectivity().name();
                if (linkedEffectivityNames.contains(relatingName) && linkedEffectivityNames.add(relatedName)) {
                    changed = true;
                }
                if (linkedEffectivityNames.contains(relatedName) && linkedEffectivityNames.add(relatingName)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionEffectivity
                    && linkedEffectivityNames.contains(((StepProductDefinitionEffectivity) candidate).effectivityId())) {
                StepProductDefinitionEffectivity productDefinitionEffectivity = (StepProductDefinitionEffectivity) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        productDefinitionEffectivity,
                        definitionTypeName(productDefinitionEffectivity),
                        productDefinitionEffectivity.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendProductDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinition.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendProductDefinitionShapeRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinitionShape productDefinitionShape,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepShapeDefinitionRepresentation
                    && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
                StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        link,
                        relationshipTypeName(link),
                        link.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        link.usedRepresentation(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepContextDependentShapeRepresentation
                    && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
                StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        contextDependent,
                        relationshipTypeName(contextDependent),
                        contextDependent.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                if (contextDependent.representationRelationship() != null) {
                    appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                            identifiedItem.id(),
                            contextDependent.representationRelationship(),
                            relationshipTypeName(contextDependent.representationRelationship()),
                            contextDependent.representationRelationship().id(),
                            resolved,
                            instanceIdsByTargetId
                    );
                    if (contextDependent.representationRelationship() instanceof StepRepresentationRelationshipWithTransformation) {
                        StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) contextDependent.representationRelationship();
                        appendDefinitionRelationshipTargets(
                                targetsByUsageId,
                                identifiedItem.id(),
                                transformed.transformationOperator(),
                                definitionTypeName(transformed.transformationOperator()),
                                transformed.transformationOperator().id(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem1(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem2(),
                                resolved,
                                instanceIdsByTargetId
                        );
                    }
                }
            }
        }
    }


    static void appendSplineCurveControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<StepCartesianPoint> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepCartesianPoint controlPoint : controlPoints) {
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    controlPoint,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }


    static void appendSplineSurfaceControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<List<StepCartesianPoint>> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (List<StepCartesianPoint> row : controlPoints) {
            appendSplineCurveControlPointTargets(
                    targetsByUsageId,
                    identifiedItem,
                    row,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }


    static Set<StepEntity> collectTargetsForApprovalStatus(
            int statusId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApproval
                    && ((StepApproval) candidate).status().id() == statusId) {
                StepApproval approval = (StepApproval) candidate;
                targets.addAll(collectSemanticTargets(approval, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForAssignedUncertainty(
            int uncertaintyId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUncertaintyAssignedContext
                    && ((StepGlobalUncertaintyAssignedContext) candidate).uncertainties().stream().anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().uncertainties().stream()
                            .anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForDateRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateAssignment
                    && ((StepAppliedDateAssignment) candidate).role().id() == roleId) {
                StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForDateTimeRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateTimeAssignment
                    && ((StepAppliedDateTimeAssignment) candidate).role().id() == roleId) {
                StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForRepresentationContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentation
                    && ((StepRepresentation) candidate).context() != null
                    && ((StepRepresentation) candidate).context().id() == contextId) {
                StepRepresentation representation = (StepRepresentation) candidate;
                targets.add(representation);
            } else if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == contextId) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForApprovalRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).role().id() == roleId) {
                StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForAssignedUnit(
            int unitId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUnitAssignedContext
                    && ((StepGlobalUnitAssignedContext) candidate).units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForCertificationType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepCertification
                    && ((StepCertification) candidate).kind().id() == kindId) {
                StepCertification certification = (StepCertification) candidate;
                targets.addAll(collectSemanticTargets(certification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForClassificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepClassificationAssignment
                    && ((StepClassificationAssignment) candidate).role().id() == roleId) {
                StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment
                    && ((StepAppliedClassificationAssignment) candidate).role().id() == roleId) {
                StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForContractType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepContract
                    && ((StepContract) candidate).kind().id() == kindId) {
                StepContract contract = (StepContract) candidate;
                targets.addAll(collectSemanticTargets(contract, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForCurveFont(
            int curveFontId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForCurveFont(curveFontId, resolved, visiting, StepPmiTargetBuilder::collectSemanticTargets);
    }


    static Set<StepEntity> collectTargetsForDocumentType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDocument
                    && ((StepDocument) candidate).kind().id() == kindId) {
                StepDocument document = (StepDocument) candidate;
                targets.addAll(collectSemanticTargets(document, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForGlobalUncertaintyContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForGlobalUnitContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForIdentificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepIdentificationAssignment
                    && ((StepIdentificationAssignment) candidate).role().id() == roleId) {
                StepIdentificationAssignment assignment = (StepIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedIdentificationAssignment
                    && ((StepAppliedIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepExternalIdentificationAssignment
                    && ((StepExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
                    && ((StepAppliedExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForItemDefinedTransformation(
            int transformationId,
            Map<Integer, StepEntity> resolved
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && ((StepRepresentationRelationshipWithTransformation) candidate).transformationOperator().id() == transformationId) {
                StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) candidate;
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForOccurrence(
            int occurrenceId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrenceId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepOrganizationAssignment
                    && ((StepOrganizationAssignment) candidate).role().id() == roleId) {
                StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedOrganizationAssignment
                    && ((StepAppliedOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForPersonAndOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPersonAndOrganizationAssignment
                    && ((StepPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
                    && ((StepAppliedPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForPointMarker(
            int markerId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForPointMarker(markerId, resolved, visiting, StepPmiTargetBuilder::collectSemanticTargets);
    }


    static Set<StepEntity> collectTargetsForProductDefinition(
            int productDefinitionId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinitionId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForSecurityLevel(
            int levelId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepSecurityClassification
                    && ((StepSecurityClassification) candidate).securityLevel().id() == levelId) {
                StepSecurityClassification classification = (StepSecurityClassification) candidate;
                targets.addAll(collectSemanticTargets(classification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }


    static Set<StepEntity> collectTargetsForStyleColour(
            int colourId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForStyleColour(colourId, resolved, visiting, StepPmiTargetBuilder::collectSemanticTargets);
    }


    static void appendPlacementDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity placement,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                placement,
                instanceIdsByTargetId
        );
        if (placement instanceof StepAxis1Placement) {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.location(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.axis(),
                    instanceIdsByTargetId
            );
        } else if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D axis2Placement2D = (StepAxis2Placement2D) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement2D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement2D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement2D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        } else if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D axis2Placement3D = (StepAxis2Placement3D) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement3D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement3D.axis() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.axis(),
                        instanceIdsByTargetId
                );
            }
            if (axis2Placement3D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        }
    }


    static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPropertyRepresentationLinkTargets(
                targetsByUsageId,
                identifiedItem.id(),
                propertyDefinition,
                resolved,
                instanceIdsByTargetId
        );
        for (StepEntity candidate : resolved.values()) {
            StepRepresentation usedRepresentation = propertyRepresentationLinkRepresentation(candidate, propertyDefinition.id());
            if (usedRepresentation != null) {
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        usedRepresentation,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }


    static Set<StepEntity> collectSemanticTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return collectSemanticTargets(entity, resolved, visiting, new PmiEntityIndex(resolved));
    }

    private static Set<StepEntity> collectSemanticTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            PmiEntityIndex index
    ) {
        if (entity == null || !visiting.add(entity.id())) {
            return Set.of();
        }
        Set<StepEntity> targets = new LinkedHashSet<>();
        dispatchSemanticTargets(targets, entity, resolved, visiting, index);
        visiting.remove(entity.id());
        return Set.copyOf(targets);
    }

    /**
     * Dispatch table behind collectSemanticTargets.
     *
     * Replaces a 316-branch if/else-if {@code instanceof} chain. The order below is
     * load bearing: {@code instanceof} also matches subtypes and the original chain
     * was "first match wins", so entries keep their original relative order.
     *
     * Each branch body was moved verbatim into a handler, so behaviour is
     * unchanged -- the handlers mutate the caller's target set instead of
     * returning one, matching what the branches did.
     *
     * The 34 leaf types that are themselves semantic targets (the old OR-ed
     * {@code instanceof} block) are now the frozen {@code SELF_TARGET_TYPES} list,
     * checked as phase 1 inside {@code dispatchSemanticTargets} (add the entity)
     * before this table runs as phase 2 (add related targets). Keeping them out of
     * this table matters: it is "first match wins" and its handlers add related
     * targets, so a leaf type folded in as a rule would return and silently drop
     * the entity itself -- StepFaceEntity especially, a marker interface whose
     * implementors are also matched by the StepTopologicalRepresentationItem rule.
     */
    @FunctionalInterface
    private interface SemanticTargetHandler {
        void handle(
                Set<StepEntity> targets,
                StepEntity entity,
                Map<Integer, StepEntity> resolved,
                Set<Integer> visiting,
                PmiEntityIndex index
        );
    }

    private record SemanticTargetRule(Class<?> type, SemanticTargetHandler handler) {
        boolean matches(StepEntity entity) {
            return type.isInstance(entity);
        }
    }

    private static SemanticTargetRule semanticRule(Class<?> type, SemanticTargetHandler handler) {
        return new SemanticTargetRule(type, handler);
    }

    private static final List<SemanticTargetRule> SEMANTIC_TARGET_RULES = List.of(
            semanticRule(StepPropertyDefinition.class, (targets, entity, resolved, visiting, index) -> {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) entity;
            targets.addAll(collectSemanticTargets(propertyDefinition.definition(), resolved, visiting, index));
            for (StepEntity candidate : index.propertyDefinitionLinks()) {
            if (candidate instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
            if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting, index));
            }
            if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting, index));
            }
            continue;
            }
            StepRepresentation usedRepresentation = usedRepresentationIfLinkMatches(candidate, propertyDefinition.id());
            if (usedRepresentation != null) {
            targets.add(usedRepresentation);
            }
            }
            }),
            semanticRule(StepDescriptiveRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepValueRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepValueRepresentationItem item = (StepValueRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepMeasureRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(item.unit(), resolved, visiting, index));
            }),
            semanticRule(StepMeasureWithUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepMeasureWithUnit measure = (StepMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting, index));
            }),
            semanticRule(StepTypedMeasureWithUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting, index));
            }),
            semanticRule(StepUncertaintyMeasureWithUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting, index));
            targets.addAll(collectTargetsForAssignedUncertainty(measure.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting, index));
            }),
            semanticRule(StepCartesianPoint.class, (targets, entity, resolved, visiting, index) -> {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting, index));
            }),
            semanticRule(StepDirection.class, (targets, entity, resolved, visiting, index) -> {
            StepDirection direction = (StepDirection) entity;
            targets.addAll(collectTargetsReferencingEntity(direction.id(), resolved, visiting, index));
            }),
            semanticRule(StepVector.class, (targets, entity, resolved, visiting, index) -> {
            StepVector vector = (StepVector) entity;
            targets.addAll(collectSemanticTargets(vector.orientation(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(vector.id(), resolved, visiting, index));
            }),
            semanticRule(StepAxis1Placement.class, (targets, entity, resolved, visiting, index) -> {
            StepAxis1Placement placement = (StepAxis1Placement) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting, index));
            }),
            semanticRule(StepAxis2Placement2D.class, (targets, entity, resolved, visiting, index) -> {
            StepAxis2Placement2D placement = (StepAxis2Placement2D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting, index));
            if (placement.refDirection() != null) {
            targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting, index));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting, index));
            }),
            semanticRule(StepAxis2Placement3D.class, (targets, entity, resolved, visiting, index) -> {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting, index));
            if (placement.axis() != null) {
            targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting, index));
            }
            if (placement.refDirection() != null) {
            targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting, index));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting, index));
            }),
            semanticRule(StepAddress.class, (targets, entity, resolved, visiting, index) -> {
            StepAddress address = (StepAddress) entity;
            targets.addAll(collectTargetsReferencingEntity(address.id(), resolved, visiting, index));
            }),
            semanticRule(StepCharacterizedObject.class, (targets, entity, resolved, visiting, index) -> {
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) entity;
            targets.addAll(collectTargetsReferencingEntity(characterizedObject.id(), resolved, visiting, index));
            }),
            semanticRule(StepPoint.class, (targets, entity, resolved, visiting, index) -> {
            StepPoint point = (StepPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting, index));
            }),
            semanticRule(StepPointSet.class, (targets, entity, resolved, visiting, index) -> {
            StepPointSet pointSet = (StepPointSet) entity;
            targets.addAll(collectSemanticTargets(pointSet.points(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(pointSet.id(), resolved, visiting, index));
            }),
            semanticRule(StepPolyline.class, (targets, entity, resolved, visiting, index) -> {
            StepPolyline polyline = (StepPolyline) entity;
            targets.addAll(collectSemanticTargets(polyline.points(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(polyline.id(), resolved, visiting, index));
            }),
            semanticRule(StepProfileDef.class, (targets, entity, resolved, visiting, index) -> {
            StepProfileDef profile = (StepProfileDef) entity;
            if (profile.position() != null) {
            targets.addAll(collectSemanticTargets(profile.position(), resolved, visiting, index));
            }
            targets.addAll(collectSemanticTargets(profile.curves(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(profile.id(), resolved, visiting, index));
            }),
            semanticRule(StepLine.class, (targets, entity, resolved, visiting, index) -> {
            StepLine line = (StepLine) entity;
            targets.addAll(collectSemanticTargets(line.point(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(line.vector(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(line.id(), resolved, visiting, index));
            }),
            semanticRule(StepCircle.class, (targets, entity, resolved, visiting, index) -> {
            StepCircle circle = (StepCircle) entity;
            targets.addAll(collectSemanticTargets(circle.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(circle.id(), resolved, visiting, index));
            }),
            semanticRule(StepEllipse.class, (targets, entity, resolved, visiting, index) -> {
            StepEllipse ellipse = (StepEllipse) entity;
            targets.addAll(collectSemanticTargets(ellipse.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(ellipse.id(), resolved, visiting, index));
            }),
            semanticRule(StepCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepCurve curve = (StepCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepBoundedCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepBoundedCurve curve = (StepBoundedCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepConicCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepConicCurve curve = (StepConicCurve) entity;
            targets.addAll(collectSemanticTargets(curve.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepBSplineCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepBSplineCurve curve = (StepBSplineCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepBezierCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepBezierCurve curve = (StepBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepBSplineCurveWithKnots.class, (targets, entity, resolved, visiting, index) -> {
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepRationalBSplineCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepPiecewiseBezierCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepUniformCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepUniformCurve curve = (StepUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepQuasiUniformCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepOffsetCurve2D.class, (targets, entity, resolved, visiting, index) -> {
            StepOffsetCurve2D curve = (StepOffsetCurve2D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepOffsetCurve3D.class, (targets, entity, resolved, visiting, index) -> {
            StepOffsetCurve3D curve = (StepOffsetCurve3D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curve.refDirection(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedCurve curve = (StepOrientedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curveElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepTrimmedCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepTrimmedCurve curve = (StepTrimmedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting, index));
            for (StepValue trim : curve.trim1()) {
            if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
            StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
            targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting, index));
            }
            }
            for (StepValue trim : curve.trim2()) {
            if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
            StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
            targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting, index));
            }
            }
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceCurve curve = (StepSurfaceCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepSeamCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepSeamCurve curve = (StepSeamCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepPcurve.class, (targets, entity, resolved, visiting, index) -> {
            StepPcurve curve = (StepPcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepCompositeCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepCompositeCurve curve = (StepCompositeCurve) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepCompositeCurveOnSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepCompositeCurveSegment.class, (targets, entity, resolved, visiting, index) -> {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) entity;
            targets.addAll(collectSemanticTargets(segment.parentCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(segment.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepSurface surface = (StepSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepBoundedSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepBoundedSurface surface = (StepBoundedSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepBSplineSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepBSplineSurface surface = (StepBSplineSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepBezierSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepBezierSurface surface = (StepBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepBSplineSurfaceWithKnots.class, (targets, entity, resolved, visiting, index) -> {
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
            resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepRationalBSplineSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
            resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepPiecewiseBezierSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepUniformSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepUniformSurface surface = (StepUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepQuasiUniformSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepPlane.class, (targets, entity, resolved, visiting, index) -> {
            StepPlane plane = (StepPlane) entity;
            targets.addAll(collectSemanticTargets(plane.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting, index));
            }),
            semanticRule(StepCylindricalSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepCylindricalSurface surface = (StepCylindricalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepConicalSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepConicalSurface surface = (StepConicalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepToroidalSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepToroidalSurface surface = (StepToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceOfLinearExtrusion.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(surface.extrusionAxis(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceOfRevolution.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(surface.axisPosition(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepRectangularTrimmedSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepCurveBoundedSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(surface.boundaries(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedSurface surface = (StepOrientedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.surfaceElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepOffsetSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepOffsetSurface surface = (StepOffsetSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepSphericalSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepSphericalSurface surface = (StepSphericalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepDegenerateToroidalSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepDegenerateToroidalSurface surface = (StepDegenerateToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting, index));
            }),
            semanticRule(StepShellBasedSurfaceModel.class, (targets, entity, resolved, visiting, index) -> {
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.shells(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepFaceBasedSurfaceModel.class, (targets, entity, resolved, visiting, index) -> {
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.faceSets(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceModel.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceModel model = (StepSurfaceModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepSolidModel.class, (targets, entity, resolved, visiting, index) -> {
            StepSolidModel model = (StepSolidModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepGeometricCurveSet.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricCurveSet set = (StepGeometricCurveSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting, index));
            }),
            semanticRule(StepGeometricSet.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricSet set = (StepGeometricSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting, index));
            }),
            semanticRule(StepBoxDomain.class, (targets, entity, resolved, visiting, index) -> {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            targets.addAll(collectSemanticTargets(boxDomain.corner(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(boxDomain.id(), resolved, visiting, index));
            }),
            semanticRule(StepDimensionalExponents.class, (targets, entity, resolved, visiting, index) -> {
            StepDimensionalExponents exponents = (StepDimensionalExponents) entity;
            targets.addAll(collectTargetsReferencingEntity(exponents.id(), resolved, visiting, index));
            }),
            semanticRule(StepDegeneratePcurve.class, (targets, entity, resolved, visiting, index) -> {
            StepDegeneratePcurve curve = (StepDegeneratePcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting, index));
            }),
            semanticRule(StepHalfSpaceSolid.class, (targets, entity, resolved, visiting, index) -> {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            targets.addAll(collectSemanticTargets(halfSpaceSolid.baseSurface(), resolved, visiting, index));
            if (halfSpaceSolid.enclosure() != null) {
            targets.addAll(collectSemanticTargets(halfSpaceSolid.enclosure(), resolved, visiting, index));
            }
            targets.addAll(collectTargetsReferencingEntity(halfSpaceSolid.id(), resolved, visiting, index));
            }),
            semanticRule(StepVertex.class, (targets, entity, resolved, visiting, index) -> {
            StepVertex vertex = (StepVertex) entity;
            targets.addAll(collectTargetsReferencingEntity(vertex.id(), resolved, visiting, index));
            }),
            semanticRule(StepVertexPoint.class, (targets, entity, resolved, visiting, index) -> {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            targets.addAll(collectSemanticTargets(vertexPoint.point(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(vertexPoint.id(), resolved, visiting, index));
            }),
            semanticRule(StepEdge.class, (targets, entity, resolved, visiting, index) -> {
            StepEdge edge = (StepEdge) entity;
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting, index));
            }),
            semanticRule(StepConnectedEdgeSet.class, (targets, entity, resolved, visiting, index) -> {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            targets.addAll(collectSemanticTargets(edgeSet.edges(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(edgeSet.id(), resolved, visiting, index));
            }),
            semanticRule(StepEdgeBasedWireframeModel.class, (targets, entity, resolved, visiting, index) -> {
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepPolyLoop.class, (targets, entity, resolved, visiting, index) -> {
            StepPolyLoop loop = (StepPolyLoop) entity;
            targets.addAll(collectSemanticTargets(loop.polygon(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting, index));
            }),
            semanticRule(StepLoop.class, (targets, entity, resolved, visiting, index) -> {
            StepLoop loop = (StepLoop) entity;
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting, index));
            }),
            semanticRule(StepEdgeLoop.class, (targets, entity, resolved, visiting, index) -> {
            StepEdgeLoop loop = (StepEdgeLoop) entity;
            targets.addAll(collectSemanticTargets(loop.edges(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting, index));
            }),
            semanticRule(StepVertexLoop.class, (targets, entity, resolved, visiting, index) -> {
            StepVertexLoop loop = (StepVertexLoop) entity;
            targets.addAll(collectSemanticTargets(loop.loopVertex(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting, index));
            }),
            semanticRule(com.minicad.step.model.StepFaceBound.class, (targets, entity, resolved, visiting, index) -> {
            com.minicad.step.model.StepFaceBound faceBound = (com.minicad.step.model.StepFaceBound) entity;
            targets.addAll(collectSemanticTargets(faceBound.loop(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(faceBound.id(), resolved, visiting, index));
            }),
            semanticRule(StepFace.class, (targets, entity, resolved, visiting, index) -> {
            StepFace face = (StepFace) entity;
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting, index));
            }),
            semanticRule(StepAdvancedFace.class, (targets, entity, resolved, visiting, index) -> {
            StepAdvancedFace face = (StepAdvancedFace) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting, index));
            }),
            semanticRule(StepFaceSurface.class, (targets, entity, resolved, visiting, index) -> {
            StepFaceSurface face = (StepFaceSurface) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting, index));
            }),
            semanticRule(StepOpenShell.class, (targets, entity, resolved, visiting, index) -> {
            StepOpenShell shell = (StepOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfacedOpenShell.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedOpenShell.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.openShellElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting, index));
            }),
            semanticRule(StepClosedShell.class, (targets, entity, resolved, visiting, index) -> {
            StepClosedShell shell = (StepClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedClosedShell.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.closedShellElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting, index));
            }),
            semanticRule(StepConnectedFaceSet.class, (targets, entity, resolved, visiting, index) -> {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting, index));
            }),
            semanticRule(StepConnectedFaceSubSet.class, (targets, entity, resolved, visiting, index) -> {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(faceSet.parentFaceSet(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedEdge.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedEdge edge = (StepOrientedEdge) entity;
            targets.addAll(collectSemanticTargets(edge.edgeElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedFace.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedFace face = (StepOrientedFace) entity;
            targets.addAll(collectSemanticTargets(face.faceElement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting, index));
            }),
            semanticRule(StepPath.class, (targets, entity, resolved, visiting, index) -> {
            StepPath path = (StepPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting, index));
            }),
            semanticRule(StepOpenPath.class, (targets, entity, resolved, visiting, index) -> {
            StepOpenPath path = (StepOpenPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting, index));
            }),
            semanticRule(StepSubpath.class, (targets, entity, resolved, visiting, index) -> {
            StepSubpath subpath = (StepSubpath) entity;
            targets.addAll(collectSemanticTargets(subpath.edges(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(subpath.parentPath(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(subpath.id(), resolved, visiting, index));
            }),
            semanticRule(StepOrientedPath.class, (targets, entity, resolved, visiting, index) -> {
            StepOrientedPath path = (StepOrientedPath) entity;
            targets.addAll(collectSemanticTargets(path.pathElement(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting, index));
            }),
            semanticRule(StepWireShell.class, (targets, entity, resolved, visiting, index) -> {
            StepWireShell wireShell = (StepWireShell) entity;
            targets.addAll(collectSemanticTargets(wireShell.loops(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(wireShell.id(), resolved, visiting, index));
            }),
            semanticRule(StepVertexShell.class, (targets, entity, resolved, visiting, index) -> {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            targets.addAll(collectSemanticTargets(vertexShell.extent(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(vertexShell.id(), resolved, visiting, index));
            }),
            semanticRule(StepShellBasedWireframeModel.class, (targets, entity, resolved, visiting, index) -> {
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting, index));
            }),
            semanticRule(StepSubedge.class, (targets, entity, resolved, visiting, index) -> {
            StepSubedge subedge = (StepSubedge) entity;
            targets.addAll(collectSemanticTargets(subedge.start(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(subedge.end(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(subedge.parentEdge(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(subedge.id(), resolved, visiting, index));
            }),
            semanticRule(StepCartesianTransformationOperator.class, (targets, entity, resolved, visiting, index) -> {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) entity;
            if (transformation.axis1() != null) {
            targets.addAll(collectSemanticTargets(transformation.axis1(), resolved, visiting, index));
            }
            if (transformation.axis2() != null) {
            targets.addAll(collectSemanticTargets(transformation.axis2(), resolved, visiting, index));
            }
            if (transformation.axis3() != null) {
            targets.addAll(collectSemanticTargets(transformation.axis3(), resolved, visiting, index));
            }
            targets.addAll(collectSemanticTargets(transformation.localOrigin(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(transformation.id(), resolved, visiting, index));
            }),
            semanticRule(StepGeometricReplica.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            targets.addAll(collectSemanticTargets(replica.parent(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(replica.transformation(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(replica.id(), resolved, visiting, index));
            }),
            semanticRule(StepSweptAreaSolid.class, (targets, entity, resolved, visiting, index) -> {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptArea(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(solid.position(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(solid.sweepReference(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepSweptDiskSolid.class, (targets, entity, resolved, visiting, index) -> {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepComplexClippingResult.class, (targets, entity, resolved, visiting, index) -> {
            StepComplexClippingResult solid = (StepComplexClippingResult) entity;
            targets.addAll(collectSemanticTargets(solid.firstOperand(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(solid.secondOperand(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepSolidReplica.class, (targets, entity, resolved, visiting, index) -> {
            StepSolidReplica solid = (StepSolidReplica) entity;
            targets.addAll(collectSemanticTargets(solid.parentSolid(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(solid.transformation(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepManifoldSolidBrep.class, (targets, entity, resolved, visiting, index) -> {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepBrepWithVoids.class, (targets, entity, resolved, visiting, index) -> {
            StepBrepWithVoids solid = (StepBrepWithVoids) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(solid.voids(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepBooleanClippingResult.class, (targets, entity, resolved, visiting, index) -> {
            StepBooleanClippingResult result = (StepBooleanClippingResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting, index));
            }),
            semanticRule(StepBooleanResult.class, (targets, entity, resolved, visiting, index) -> {
            StepBooleanResult result = (StepBooleanResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting, index));
            }),
            semanticRule(StepCsgSolid.class, (targets, entity, resolved, visiting, index) -> {
            StepCsgSolid solid = (StepCsgSolid) entity;
            targets.addAll(collectSemanticTargets(solid.treeRootExpression(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting, index));
            }),
            semanticRule(StepCsgPrimitive.class, (targets, entity, resolved, visiting, index) -> {
            StepCsgPrimitive primitive = (StepCsgPrimitive) entity;
            targets.addAll(collectSemanticTargets(primitive.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(primitive.id(), resolved, visiting, index));
            }),
            semanticRule(StepRepresentationContext.class, (targets, entity, resolved, visiting, index) -> {
            StepRepresentationContext context = (StepRepresentationContext) entity;
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
            }),
            semanticRule(StepGeometricRepresentationContext.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) entity;
            if (context.globalUnitAssignedContext() != null) {
            targets.addAll(collectSemanticTargets(context.globalUnitAssignedContext(), resolved, visiting, index));
            }
            if (context.globalUncertaintyAssignedContext() != null) {
            targets.addAll(collectSemanticTargets(context.globalUncertaintyAssignedContext(), resolved, visiting, index));
            }
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
            }),
            semanticRule(StepAbstractVariable.class, (targets, entity, resolved, visiting, index) -> {
            StepAbstractVariable variable = (StepAbstractVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting, index));
            }),
            semanticRule(StepScalarVariable.class, (targets, entity, resolved, visiting, index) -> {
            StepScalarVariable variable = (StepScalarVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting, index));
            }),
            semanticRule(StepRowVariable.class, (targets, entity, resolved, visiting, index) -> {
            StepRowVariable variable = (StepRowVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting, index));
            }),
            semanticRule(StepForwardChainingRulePremise.class, (targets, entity, resolved, visiting, index) -> {
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting, index));
            }),
            semanticRule(StepBackChainingRuleBody.class, (targets, entity, resolved, visiting, index) -> {
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting, index));
            }),
            semanticRule(StepApplicationContext.class, (targets, entity, resolved, visiting, index) -> {
            StepApplicationContext applicationContext = (StepApplicationContext) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApplicationProtocolDefinition
            && ((StepApplicationProtocolDefinition) candidate).application().id() == applicationContext.id()) {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) candidate;
            targets.addAll(collectSemanticTargets(protocolDefinition, resolved, visiting, index));
            } else if (candidate instanceof StepProductContext
            && ((StepProductContext) candidate).frameOfReference().id() == applicationContext.id()) {
            StepProductContext productContext = (StepProductContext) candidate;
            targets.addAll(collectSemanticTargets(productContext, resolved, visiting, index));
            } else if (candidate instanceof StepProductDefinitionContext
            && ((StepProductDefinitionContext) candidate).frameOfReference().id() == applicationContext.id()) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) candidate;
            targets.addAll(collectSemanticTargets(productDefinitionContext, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepApplicationProtocolDefinition.class, (targets, entity, resolved, visiting, index) -> {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) entity;
            targets.addAll(collectSemanticTargets(protocolDefinition.application(), resolved, visiting, index));
            }),
            semanticRule(StepProductContext.class, (targets, entity, resolved, visiting, index) -> {
            StepProductContext productContext = (StepProductContext) entity;
            targets.addAll(collectSemanticTargets(productContext.frameOfReference(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProduct
            && ((StepProduct) candidate).frameOfReference().stream().anyMatch(context -> context.id() == productContext.id())) {
            StepProduct product = (StepProduct) candidate;
            targets.addAll(collectSemanticTargets(product, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepProductDefinitionContext.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) entity;
            targets.addAll(collectSemanticTargets(productDefinitionContext.frameOfReference(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinition
            && ((StepProductDefinition) candidate).frameOfReference().id() == productDefinitionContext.id()) {
            StepProductDefinition productDefinition = (StepProductDefinition) candidate;
            targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepGeneralProperty.class, (targets, entity, resolved, visiting, index) -> {
            StepGeneralProperty generalProperty = (StepGeneralProperty) entity;
            targets.addAll(collectTargetsReferencingEntity(generalProperty.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
            if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting, index));
            }
            if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepDocument.class, (targets, entity, resolved, visiting, index) -> {
            StepDocument document = (StepDocument) entity;
            targets.addAll(collectTargetsReferencingEntity(document.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDocumentReference
            && ((StepDocumentReference) candidate).assignedDocument().id() == document.id()) {
            StepDocumentReference reference = (StepDocumentReference) candidate;
            targets.addAll(collectSemanticTargets(reference, resolved, visiting, index));
            } else if (candidate instanceof StepAppliedDocumentReference
            && ((StepAppliedDocumentReference) candidate).assignedDocument().id() == document.id()) {
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) candidate;
            targets.addAll(collectSemanticTargets(reference, resolved, visiting, index));
            } else if (candidate instanceof StepDocumentRelationship) {
            StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
            if (relationship.relatingDocument().id() == document.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting, index));
            }
            if (relationship.relatedDocument().id() == document.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepDocumentUsageConstraint.class, (targets, entity, resolved, visiting, index) -> {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) entity;
            targets.addAll(collectSemanticTargets(documentUsageConstraint.source(), resolved, visiting, index));
            }),
            semanticRule(StepGroup.class, (targets, entity, resolved, visiting, index) -> {
            StepGroup group = (StepGroup) entity;
            targets.addAll(collectTargetsReferencingEntity(group.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGroupAssignment
            && ((StepGroupAssignment) candidate).assignedGroup().id() == group.id()) {
            StepGroupAssignment assignment = (StepGroupAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting, index));
            } else if (candidate instanceof StepAppliedGroupAssignment
            && ((StepAppliedGroupAssignment) candidate).assignedGroup().id() == group.id()) {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepClassificationAssignment
            && ((StepClassificationAssignment) candidate).assignedClass().id() == group.id()) {
            StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting, index));
            } else if (candidate instanceof StepAppliedClassificationAssignment
            && ((StepAppliedClassificationAssignment) candidate).assignedClass().id() == group.id()) {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
            if (relationship.relatingGroup().id() == group.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting, index));
            }
            if (relationship.relatedGroup().id() == group.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepOrganization.class, (targets, entity, resolved, visiting, index) -> {
            StepOrganization organization = (StepOrganization) entity;
            targets.addAll(collectTargetsReferencingEntity(organization.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedOrganizationAssignment
            && ((StepAppliedOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepOrganizationAssignment
            && ((StepOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting, index));
            } else if (candidate instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
            if (relationship.relatingOrganization().id() == organization.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting, index));
            }
            if (relationship.relatedOrganization().id() == organization.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepProductCategory.class, (targets, entity, resolved, visiting, index) -> {
            StepProductCategory productCategory = (StepProductCategory) entity;
            targets.addAll(collectTargetsReferencingEntity(productCategory.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
            if (relationship.category().id() == productCategory.id()) {
            targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting, index));
            }
            if (relationship.subCategory().id() == productCategory.id()) {
            targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting, index));
            }
            } else if (candidate instanceof StepProductRelatedProductCategory
            && ((StepProductRelatedProductCategory) candidate).id() == productCategory.id()) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
            targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepProductRelatedProductCategory.class, (targets, entity, resolved, visiting, index) -> {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) entity;
            targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting, index));
            }),
            semanticRule(StepProduct.class, (targets, entity, resolved, visiting, index) -> {
            StepProduct product = (StepProduct) entity;
            targets.addAll(collectTargetsReferencingEntity(product.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionFormation
            && ((StepProductDefinitionFormation) candidate).ofProduct().id() == product.id()) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) candidate;
            targets.addAll(collectSemanticTargets(formation, resolved, visiting, index));
            } else if (candidate instanceof StepProductRelatedProductCategory
            && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
            targets.addAll(collectSemanticTargets(relatedCategory, resolved, visiting, index));
            } else if (candidate instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) candidate;
            if (relationship.relatingProduct().id() == product.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting, index));
            }
            if (relationship.relatedProduct().id() == product.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepProductDefinitionFormation.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) entity;
            targets.addAll(collectTargetsReferencingEntity(formation.id(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(formation.ofProduct(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinition
            && ((StepProductDefinition) candidate).formation().id() == formation.id()) {
            StepProductDefinition productDefinition = (StepProductDefinition) candidate;
            targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting, index));
            } else if (candidate instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
            if (relationship.relatingFormation().id() == formation.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting, index));
            }
            if (relationship.relatedFormation().id() == formation.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepProductDefinitionEffectivity.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) entity;
            targets.addAll(collectSemanticTargets(effectivity.productDefinition(), resolved, visiting, index));
            }),
            semanticRule(StepEffectivity.class, (targets, entity, resolved, visiting, index) -> {
            StepEffectivity effectivity = (StepEffectivity) entity;
            targets.addAll(collectTargetsReferencingEntity(effectivity.id(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
            if (relationship.relatingEffectivity().id() == effectivity.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting, index));
            }
            if (relationship.relatedEffectivity().id() == effectivity.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepCalendarDate.class, (targets, entity, resolved, visiting, index) -> {
            StepCalendarDate calendarDate = (StepCalendarDate) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDateAssignment
            && ((StepDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
            StepDateAssignment assignment = (StepDateAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment, resolved, visiting, index));
            } else if (candidate instanceof StepAppliedDateAssignment
            && ((StepAppliedDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepDateAndTime
            && ((StepDateAndTime) candidate).dateComponent().id() == calendarDate.id()) {
            StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
            targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepDateAndTime.class, (targets, entity, resolved, visiting, index) -> {
            StepDateAndTime dateAndTime = (StepDateAndTime) entity;
            targets.addAll(collectSemanticTargets(dateAndTime.dateComponent(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(dateAndTime.timeComponent(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDateTimeAssignment
            && ((StepDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment, resolved, visiting, index));
            } else if (candidate instanceof StepAppliedDateTimeAssignment
            && ((StepAppliedDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepApprovalDateTime
            && ((StepApprovalDateTime) candidate).dateTime().id() == dateAndTime.id()) {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
            targets.addAll(collectSemanticTargets(approvalDateTime, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepLocalTime.class, (targets, entity, resolved, visiting, index) -> {
            StepLocalTime localTime = (StepLocalTime) entity;
            targets.addAll(collectSemanticTargets(localTime.zone(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDateAndTime
            && ((StepDateAndTime) candidate).timeComponent().id() == localTime.id()) {
            StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
            targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepCoordinatedUniversalTimeOffset.class, (targets, entity, resolved, visiting, index) -> {
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepLocalTime
            && ((StepLocalTime) candidate).zone().id() == zone.id()) {
            StepLocalTime localTime = (StepLocalTime) candidate;
            targets.addAll(collectSemanticTargets(localTime, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepDateAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepDateAssignment assignment = (StepDateAssignment) entity;
            targets.addAll(collectTargetsForDateRole(assignment.role().id(), resolved, visiting));
            }),
            semanticRule(StepDateTimeAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) entity;
            targets.addAll(collectTargetsForDateTimeRole(assignment.role().id(), resolved, visiting));
            }),
            semanticRule(StepPerson.class, (targets, entity, resolved, visiting, index) -> {
            StepPerson person = (StepPerson) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPersonAndOrganization
            && ((StepPersonAndOrganization) candidate).person().id() == person.id()) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) candidate;
            targets.addAll(collectSemanticTargets(personAndOrganization, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepApprovalStatus.class, (targets, entity, resolved, visiting, index) -> {
            StepApprovalStatus status = (StepApprovalStatus) entity;
            targets.addAll(collectTargetsForApprovalStatus(status.id(), resolved, visiting));
            }),
            semanticRule(StepSecurityClassificationLevel.class, (targets, entity, resolved, visiting, index) -> {
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) entity;
            targets.addAll(collectTargetsForSecurityLevel(level.id(), resolved, visiting));
            }),
            semanticRule(StepContractType.class, (targets, entity, resolved, visiting, index) -> {
            StepContractType kind = (StepContractType) entity;
            targets.addAll(collectTargetsForContractType(kind.id(), resolved, visiting));
            }),
            semanticRule(StepCertificationType.class, (targets, entity, resolved, visiting, index) -> {
            StepCertificationType kind = (StepCertificationType) entity;
            targets.addAll(collectTargetsForCertificationType(kind.id(), resolved, visiting));
            }),
            semanticRule(StepApprovalRole.class, (targets, entity, resolved, visiting, index) -> {
            StepApprovalRole role = (StepApprovalRole) entity;
            targets.addAll(collectTargetsForApprovalRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepOrganizationRole.class, (targets, entity, resolved, visiting, index) -> {
            StepOrganizationRole role = (StepOrganizationRole) entity;
            targets.addAll(collectTargetsForOrganizationRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepPersonAndOrganizationRole.class, (targets, entity, resolved, visiting, index) -> {
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) entity;
            targets.addAll(collectTargetsForPersonAndOrganizationRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepClassificationRole.class, (targets, entity, resolved, visiting, index) -> {
            StepClassificationRole role = (StepClassificationRole) entity;
            targets.addAll(collectTargetsForClassificationRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepDateRole.class, (targets, entity, resolved, visiting, index) -> {
            StepDateRole role = (StepDateRole) entity;
            targets.addAll(collectTargetsForDateRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepDateTimeRole.class, (targets, entity, resolved, visiting, index) -> {
            StepDateTimeRole role = (StepDateTimeRole) entity;
            targets.addAll(collectTargetsForDateTimeRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepIdentificationRole.class, (targets, entity, resolved, visiting, index) -> {
            StepIdentificationRole role = (StepIdentificationRole) entity;
            targets.addAll(collectTargetsForIdentificationRole(role.id(), resolved, visiting));
            }),
            semanticRule(StepDocumentType.class, (targets, entity, resolved, visiting, index) -> {
            StepDocumentType kind = (StepDocumentType) entity;
            targets.addAll(collectTargetsForDocumentType(kind.id(), resolved, visiting));
            }),
            semanticRule(StepApproval.class, (targets, entity, resolved, visiting, index) -> {
            StepApproval approval = (StepApproval) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedApprovalAssignment
            && ((StepAppliedApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepApprovalAssignment
            && ((StepApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
            StepApprovalAssignment assignment = (StepApprovalAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting, index));
            } else if (candidate instanceof StepApprovalPersonOrganization
            && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
            StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
            targets.addAll(collectSemanticTargets(personOrganization.personOrganization(), resolved, visiting, index));
            } else if (candidate instanceof StepApprovalDateTime
            && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
            targets.addAll(collectSemanticTargets(approvalDateTime.dateTime(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepSecurityClassification.class, (targets, entity, resolved, visiting, index) -> {
            StepSecurityClassification classification = (StepSecurityClassification) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedSecurityClassificationAssignment
            && ((StepAppliedSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepSecurityClassificationAssignment
            && ((StepSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepContract.class, (targets, entity, resolved, visiting, index) -> {
            StepContract contract = (StepContract) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedContractAssignment
            && ((StepAppliedContractAssignment) candidate).assignedContract().id() == contract.id()) {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepContractAssignment
            && ((StepContractAssignment) candidate).assignedContract().id() == contract.id()) {
            StepContractAssignment assignment = (StepContractAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepCertification.class, (targets, entity, resolved, visiting, index) -> {
            StepCertification certification = (StepCertification) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedCertificationAssignment
            && ((StepAppliedCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepCertificationAssignment
            && ((StepCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
            StepCertificationAssignment assignment = (StepCertificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepPersonAndOrganization.class, (targets, entity, resolved, visiting, index) -> {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
            && ((StepAppliedPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepPersonAndOrganizationAssignment
            && ((StepPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepLanguage.class, (targets, entity, resolved, visiting, index) -> {
            StepLanguage language = (StepLanguage) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedLanguageAssignment
            && ((StepAppliedLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepLanguageAssignment
            && ((StepLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
            StepLanguageAssignment assignment = (StepLanguageAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepExternalIdentificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.source(), resolved, visiting, index));
            }),
            semanticRule(StepExternalSource.class, (targets, entity, resolved, visiting, index) -> {
            StepExternalSource source = (StepExternalSource) entity;
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepExternallyDefinedItem
            && ((StepExternallyDefinedItem) candidate).source().id() == source.id()) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
            targets.addAll(collectSemanticTargets(item, resolved, visiting, index));
            } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
            && ((StepAppliedExternalIdentificationAssignment) candidate).source().id() == source.id()) {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            } else if (candidate instanceof StepExternalSourceRelationship) {
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
            if (relationship.relatingSource().id() == source.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting, index));
            }
            if (relationship.relatedSource().id() == source.id()) {
            targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting, index));
            }
            }
            }
            }),
            semanticRule(StepExternallyDefinedItem.class, (targets, entity, resolved, visiting, index) -> {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(item.source(), resolved, visiting, index));
            }),
            semanticRule(StepGeneralPropertyRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting, index));
            }),
            semanticRule(StepApprovalAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepApprovalAssignment assignment = (StepApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting, index));
            }),
            semanticRule(StepClassificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepClassificationAssignment assignment = (StepClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting, index));
            }),
            semanticRule(StepGroupAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepGroupAssignment assignment = (StepGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting, index));
            }),
            semanticRule(StepSecurityClassificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting, index));
            }),
            semanticRule(StepContractAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepContractAssignment assignment = (StepContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting, index));
            }),
            semanticRule(StepCertificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepCertificationAssignment assignment = (StepCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting, index));
            }),
            semanticRule(StepPersonAndOrganizationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting, index));
            }),
            semanticRule(StepOrganizationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting, index));
            }),
            semanticRule(StepLanguageAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepLanguageAssignment assignment = (StepLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting, index));
            }),
            semanticRule(StepDocumentReference.class, (targets, entity, resolved, visiting, index) -> {
            StepDocumentReference reference = (StepDocumentReference) entity;
            targets.addAll(collectSemanticTargets(reference.assignedDocument(), resolved, visiting, index));
            }),
            semanticRule(StepPresentationLayerAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedItems(), resolved, visiting, index));
            }),
            semanticRule(StepApprovalPersonOrganization.class, (targets, entity, resolved, visiting, index) -> {
            StepApprovalPersonOrganization approvalPersonOrganization = (StepApprovalPersonOrganization) entity;
            targets.addAll(collectSemanticTargets(approvalPersonOrganization.authorizedApproval(), resolved, visiting, index));
            }),
            semanticRule(StepApprovalDateTime.class, (targets, entity, resolved, visiting, index) -> {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) entity;
            targets.addAll(collectSemanticTargets(approvalDateTime.datedApproval(), resolved, visiting, index));
            }),
            semanticRule(StepItemDefinedTransformation.class, (targets, entity, resolved, visiting, index) -> {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            targets.addAll(collectTargetsForItemDefinedTransformation(transformation.id(), resolved));
            }),
            semanticRule(StepExternalSourceRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting, index));
            }),
            semanticRule(StepDocumentRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepDocumentRelationship relationship = (StepDocumentRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting, index));
            }),
            semanticRule(StepGroupRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepGroupRelationship relationship = (StepGroupRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting, index));
            }),
            semanticRule(StepOrganizationRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting, index));
            }),
            semanticRule(StepProductCategoryRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting, index));
            }),
            semanticRule(StepProductRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepProductRelationship relationship = (StepProductRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting, index));
            }),
            semanticRule(StepProductDefinitionFormationRelationship.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting, index));
            }),
            semanticRule(StepEffectivityRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting, index));
            }),
            semanticRule(StepRepresentationRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }),
            semanticRule(StepRepresentationRelationshipWithTransformation.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }),
            semanticRule(StepShapeRepresentationRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }),
            semanticRule(StepGeometricItemSpecificUsage.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            }),
            semanticRule(StepChainBasedGeometricItemSpecificUsage.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting, index));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
            }),
            semanticRule(StepItemIdentifiedRepresentationUsage.class, (targets, entity, resolved, visiting, index) -> {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            }),
            semanticRule(StepChainBasedItemIdentifiedRepresentationUsage.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting, index));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
            }),
            semanticRule(StepPlacedTarget.class, (targets, entity, resolved, visiting, index) -> {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingModelItemAssociation.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingModelItemAssociationWithPlaceholder.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.annotationPlaceholder(), resolved, visiting, index));
            }),
            semanticRule(StepPmiRequirementItemAssociation.class, (targets, entity, resolved, visiting, index) -> {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting, index));
            }),
            semanticRule(StepMechanicalDesignRequirementItemAssociation.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting, index));
            }),
            semanticRule(StepStyledItem.class, (targets, entity, resolved, visiting, index) -> {
            StepStyledItem styledItem = (StepStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting, index));
            }),
            semanticRule(StepOverRidingStyledItem.class, (targets, entity, resolved, visiting, index) -> {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(styledItem.overRiddenStyle(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting, index));
            }),
            semanticRule(StepMappedItem.class, (targets, entity, resolved, visiting, index) -> {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            targets.addAll(collectSemanticTargets(mappedItem.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(mappedItem.mappingTarget(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(mappedItem.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationCurveOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationFillArea.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            targets.addAll(collectSemanticTargets(fillArea.boundaries(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(fillArea.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationFillAreaOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.fillStyleTarget(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationPlaceholderOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationPlane.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationPlane plane = (StepAnnotationPlane) entity;
            targets.addAll(collectSemanticTargets(plane.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(plane.item(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(plane.elements(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationPointOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationSymbolOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationSubfigureOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationTextOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.position(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingAnnotationOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepDimensionCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepDimensionCurve occurrence = (StepDimensionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepLeaderCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepLeaderCurve occurrence = (StepLeaderCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepProjectionCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepProjectionCurve occurrence = (StepProjectionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepTerminatorSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.styles(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(symbol.item(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(symbol.annotatedCurve(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingCallout.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            targets.addAll(collectSemanticTargets(callout.contents(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingCalloutRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingCallout(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedCallout(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationOccurrenceRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingAnnotationOccurrence(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedAnnotationOccurrence(), resolved, visiting, index));
            }),
            semanticRule(StepRepresentationMap.class, (targets, entity, resolved, visiting, index) -> {
            StepRepresentationMap mapping = (StepRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting, index));
            }),
            semanticRule(StepSymbolRepresentationMap.class, (targets, entity, resolved, visiting, index) -> {
            StepSymbolRepresentationMap mapping = (StepSymbolRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationText.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            targets.addAll(collectSemanticTargets(annotationText.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(annotationText.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepAnnotationTextCharacter.class, (targets, entity, resolved, visiting, index) -> {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepUserDefinedCurveFont.class, (targets, entity, resolved, visiting, index) -> {
            StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) entity;
            targets.addAll(collectSemanticTargets(curveFont.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curveFont.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepUserDefinedMarker.class, (targets, entity, resolved, visiting, index) -> {
            StepUserDefinedMarker marker = (StepUserDefinedMarker) entity;
            targets.addAll(collectSemanticTargets(marker.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(marker.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepUserDefinedTerminatorSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.mappingSource(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(symbol.mappingTarget(), resolved, visiting, index));
            }),
            semanticRule(StepPresentationStyleAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.styles(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(assignment.id(), resolved, visiting, index));
            }),
            semanticRule(StepFillAreaStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyle.styles(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepFillAreaStyleColour.class, (targets, entity, resolved, visiting, index) -> {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyleColour.colour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyleColour.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleFillArea.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) entity;
            targets.addAll(collectSemanticTargets(style.fillStyle(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepCharacterGlyphStyleStroke.class, (targets, entity, resolved, visiting, index) -> {
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) entity;
            targets.addAll(collectSemanticTargets(style.strokeStyle(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepCharacterGlyphStyleOutline.class, (targets, entity, resolved, visiting, index) -> {
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepCharacterGlyphStyleOutlineWithCharacteristics.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(style.characteristics(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedCurveFont.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedCurveFont curveFont = (StepPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
            }),
            semanticRule(StepDraughtingPreDefinedCurveFont.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingPreDefinedCurveFont curveFont = (StepDraughtingPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
            }),
            semanticRule(StepPreDefinedMarker.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedMarker marker = (StepPreDefinedMarker) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
            }),
            semanticRule(StepPreDefinedPointMarkerSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
            }),
            semanticRule(StepColour.class, (targets, entity, resolved, visiting, index) -> {
            StepColour colour = (StepColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting, index));
            }),
            semanticRule(StepColourSpecification.class, (targets, entity, resolved, visiting, index) -> {
            StepColourSpecification colour = (StepColourSpecification) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting, index));
            }),
            semanticRule(StepColourRgb.class, (targets, entity, resolved, visiting, index) -> {
            StepColourRgb colour = (StepColourRgb) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting, index));
            }),
            semanticRule(StepConversionBasedUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepConversionBasedUnit unit = (StepConversionBasedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting, index));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepConversionBasedUnitWithOffset.class, (targets, entity, resolved, visiting, index) -> {
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting, index));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepDerivedUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepDerivedUnit unit = (StepDerivedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.elements(), resolved, visiting, index));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepDerivedUnitElement.class, (targets, entity, resolved, visiting, index) -> {
            StepDerivedUnitElement element = (StepDerivedUnitElement) entity;
            targets.addAll(collectSemanticTargets(element.unit(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(element.id(), resolved, visiting, index));
            }),
            semanticRule(StepNamedUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepNamedUnit unit = (StepNamedUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepSiUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepSiUnit unit = (StepSiUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepContextDependentUnit.class, (targets, entity, resolved, visiting, index) -> {
            StepContextDependentUnit unit = (StepContextDependentUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting, index));
            }),
            semanticRule(StepGlobalUncertaintyAssignedContext.class, (targets, entity, resolved, visiting, index) -> {
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.uncertainties(), resolved, visiting, index));
            targets.addAll(collectTargetsForGlobalUncertaintyContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting, index));
            }),
            semanticRule(StepGlobalUnitAssignedContext.class, (targets, entity, resolved, visiting, index) -> {
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.units(), resolved, visiting, index));
            targets.addAll(collectTargetsForGlobalUnitContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting, index));
            }),
            semanticRule(StepRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepRepresentationItem item = (StepRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepGeometricRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepTopologicalRepresentationItem.class, (targets, entity, resolved, visiting, index) -> {
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedColour.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedColour colour = (StepPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            }),
            semanticRule(StepDraughtingPreDefinedColour.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            }),
            semanticRule(StepCurveStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            targets.addAll(collectSemanticTargets(curveStyle.curveFont(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(curveStyle.colour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(curveStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepPointStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            targets.addAll(collectSemanticTargets(pointStyle.marker(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(pointStyle.colour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(pointStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepSymbolColour.class, (targets, entity, resolved, visiting, index) -> {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            targets.addAll(collectSemanticTargets(symbolColour.colour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(symbolColour.id(), resolved, visiting, index));
            }),
            semanticRule(StepSymbolStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            targets.addAll(collectSemanticTargets(symbolStyle.styleOfSymbol(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(symbolStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyleForDefinedFont.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyleForDefinedFont textStyle = (StepTextStyleForDefinedFont) entity;
            targets.addAll(collectSemanticTargets(textStyle.textColour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyle textStyle = (StepTextStyle) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyleWithSpacing.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyleWithSpacing textStyle = (StepTextStyleWithSpacing) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyleWithJustification.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyleWithJustification textStyle = (StepTextStyleWithJustification) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyleWithBoxCharacteristics.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyleWithBoxCharacteristics textStyle = (StepTextStyleWithBoxCharacteristics) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepTextStyleWithMirror.class, (targets, entity, resolved, visiting, index) -> {
            StepTextStyleWithMirror textStyle = (StepTextStyleWithMirror) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(textStyle.mirrorPlacement(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleBoundary.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleParameterLine.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleSegmentationCurve.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleSilhouette.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleControlGrid.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceSideStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceSideStyle sideStyle = (StepSurfaceSideStyle) entity;
            targets.addAll(collectSemanticTargets(sideStyle.styles(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(sideStyle.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleUsage.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) entity;
            targets.addAll(collectSemanticTargets(usage.style(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(usage.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleTransparent.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleReflectanceAmbient.class, (targets, entity, resolved, visiting, index) -> {
            StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleReflectanceAmbientDiffuse.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepSurfaceStyleReflectanceAmbientDiffuseSpecular.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            targets.addAll(collectSemanticTargets(style.specularColour(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedSurfaceSideStyle.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedTextFont.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedTextFont textFont = (StepPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting, index));
            }),
            semanticRule(StepDraughtingPreDefinedTextFont.class, (targets, entity, resolved, visiting, index) -> {
            StepDraughtingPreDefinedTextFont textFont = (StepDraughtingPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedTerminatorSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedDimensionSymbol.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedGeometricalToleranceSymbol.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting, index));
            }),
            semanticRule(StepPreDefinedItem.class, (targets, entity, resolved, visiting, index) -> {
            StepPreDefinedItem item = (StepPreDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting, index));
            }),
            semanticRule(StepDescriptionAttribute.class, (targets, entity, resolved, visiting, index) -> {
            StepDescriptionAttribute descriptionAttribute = (StepDescriptionAttribute) entity;
            targets.addAll(collectSemanticTargets(descriptionAttribute.describedItem(), resolved, visiting, index));
            }),
            semanticRule(StepNameAttribute.class, (targets, entity, resolved, visiting, index) -> {
            StepNameAttribute nameAttribute = (StepNameAttribute) entity;
            targets.addAll(collectSemanticTargets(nameAttribute.namedItem(), resolved, visiting, index));
            }),
            semanticRule(StepIdAttribute.class, (targets, entity, resolved, visiting, index) -> {
            StepIdAttribute idAttribute = (StepIdAttribute) entity;
            targets.addAll(collectSemanticTargets(idAttribute.identifiedItem(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedNameAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedIdentificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedExternalIdentificationAssignment.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedGroupAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedClassificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedDateAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedDateTimeAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedApprovalAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedSecurityClassificationAssignment.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedDocumentReference.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedDocumentReference assignment = (StepAppliedDocumentReference) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedContractAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedCertificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedPersonAndOrganizationAssignment.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedOrganizationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAppliedLanguageAssignment.class, (targets, entity, resolved, visiting, index) -> {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting, index));
            }),
            semanticRule(StepAttributeAssertion.class, (targets, entity, resolved, visiting, index) -> {
            StepAttributeAssertion attributeAssertion = (StepAttributeAssertion) entity;
            targets.add(attributeAssertion.usedRepresentation());
            targets.addAll(collectSemanticTargets(attributeAssertion.definition(), resolved, visiting, index));
            }),
            semanticRule(StepIdentificationAssignment.class, (targets, entity, resolved, visiting, index) -> {
            // Pure assignment metadata without item references contributes no target by itself.
            }),
            semanticRule(StepNameAssignment.class, (targets, entity, resolved, visiting, index) -> {
            // Pure assignment metadata without item references contributes no target by itself.
            }),
            semanticRule(StepShapeDefinitionRepresentation.class, (targets, entity, resolved, visiting, index) -> {
            StepShapeDefinitionRepresentation shapeDefinitionRepresentation = (StepShapeDefinitionRepresentation) entity;
            targets.add(shapeDefinitionRepresentation.usedRepresentation());
            targets.addAll(collectSemanticTargets(shapeDefinitionRepresentation.definition(), resolved, visiting, index));
            }),
            semanticRule(StepContextDependentShapeRepresentation.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(contextDependent.representationRelationship()));
            targets.addAll(collectSemanticTargets(contextDependent.representedProductRelation(), resolved, visiting, index));
            }),
            semanticRule(StepProductDefinitionShape.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) entity;
            targets.addAll(collectSemanticTargets(productDefinitionShape.definition(), resolved, visiting, index));
            for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepShapeDefinitionRepresentation
            && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
            StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
            targets.add(link.usedRepresentation());
            } else if (candidate instanceof StepContextDependentShapeRepresentation
            && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
            StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
            targets.addAll(collectSemanticTargets(contextDependent, resolved, visiting, index));
            } else if (candidate instanceof StepShapeAspect
            && ((StepShapeAspect) candidate).ofShape().id() == productDefinitionShape.id()) {
            StepShapeAspect shapeAspect = (StepShapeAspect) candidate;
            targets.addAll(collectSemanticTargets(shapeAspect, resolved, visiting, index));
            } else if (candidate instanceof StepShapeAspectOccurrence
            && ((StepShapeAspectOccurrence) candidate).ofShape().id() == productDefinitionShape.id()) {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) candidate;
            targets.addAll(collectSemanticTargets(occurrence, resolved, visiting, index));
            }
            }
            }),
            semanticRule(StepProductDefinition.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinition productDefinition = (StepProductDefinition) entity;
            targets.addAll(collectTargetsForProductDefinition(productDefinition.id(), resolved, visiting));
            }),
            semanticRule(StepNextAssemblyUsageOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.relatedProductDefinition(), resolved, visiting, index));
            targets.addAll(collectTargetsForOccurrence(occurrence.id(), resolved, visiting));
            }),
            semanticRule(StepProductDefinitionRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProductDefinition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedProductDefinition(), resolved, visiting, index));
            }),
            semanticRule(StepProductDefinitionRelationshipRelationship.class, (
                    targets,
                    entity,
                    resolved,
                    visiting,
                    index
            ) -> {
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relating(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.related(), resolved, visiting, index));
            }),
            semanticRule(StepPropertyDefinitionRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting, index));
            }),
            semanticRule(StepShapeAspectOccurrence.class, (targets, entity, resolved, visiting, index) -> {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.definition(), resolved, visiting, index));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting, index));
            }),
            semanticRule(StepShapeAspect.class, (targets, entity, resolved, visiting, index) -> {
            StepShapeAspect shapeAspect = (StepShapeAspect) entity;
            targets.addAll(collectTargetsReferencingEntity(shapeAspect.id(), resolved, visiting, index));
            }),
            semanticRule(StepShapeAspectRelationship.class, (targets, entity, resolved, visiting, index) -> {
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingShapeAspect(), resolved, visiting, index));
            targets.addAll(collectSemanticTargets(relationship.relatedShapeAspect(), resolved, visiting, index));
            })
    );

    /**
     * The 34 geometric/topological types that are themselves semantic targets.
     * Replaces the standalone 34-type OR block that used to precede the
     * SEMANTIC_TARGET_RULES dispatch inside collectSemanticTargets.
     *
     * It is kept separate from SEMANTIC_TARGET_RULES on purpose: that table is
     * "first match wins" and its handlers add RELATED targets, so folding these
     * leaf types in as rules would make the first match return and silently drop
     * the entity itself. StepFaceEntity in particular is a marker interface whose
     * implementors are also matched by the StepTopologicalRepresentationItem
     * supertype rule, so they must keep getting both the entity and its related
     * targets. Instead this list is checked as phase 1 (add the entity) before the
     * related-target table runs as phase 2 -- identical to the old two-pass order.
     *
     * Order is frozen in src/test/resources/pmi-semantic-selftarget-order.txt.
     */
    private static final List<Class<?>> SELF_TARGET_TYPES = List.of(
            StepFaceEntity.class,
            StepEdgeCurve.class,
            StepPath.class,
            StepOpenPath.class,
            StepSubpath.class,
            StepOrientedPath.class,
            StepConnectedEdgeSet.class,
            StepPointSet.class,
            StepGeometricSet.class,
            StepGeometricCurveSet.class,
            StepOpenShell.class,
            StepSurfacedOpenShell.class,
            StepOrientedOpenShell.class,
            StepClosedShell.class,
            StepOrientedClosedShell.class,
            StepWireShell.class,
            StepVertexShell.class,
            StepEdgeLoop.class,
            StepPolyLoop.class,
            StepConnectedFaceSet.class,
            StepConnectedFaceSubSet.class,
            StepFaceBasedSurfaceModel.class,
            StepShellBasedSurfaceModel.class,
            StepEdgeBasedWireframeModel.class,
            StepShellBasedWireframeModel.class,
            StepManifoldSolidBrep.class,
            StepBrepWithVoids.class,
            StepSweptAreaSolid.class,
            StepSolidReplica.class,
            StepCsgSolid.class,
            StepCsgPrimitive.class,
            StepBooleanResult.class,
            StepBooleanClippingResult.class,
            StepRepresentation.class
    );

    private static boolean isSelfTarget(StepEntity entity) {
        for (Class<?> type : SELF_TARGET_TYPES) {
            if (type.isInstance(entity)) {
                return true;
            }
        }
        return false;
    }

    private static void dispatchSemanticTargets(
            Set<StepEntity> targets,
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            PmiEntityIndex index
    ) {
        if (isSelfTarget(entity)) {
            targets.add(entity);
        }
        for (SemanticTargetRule rule : SEMANTIC_TARGET_RULES) {
            if (rule.matches(entity)) {
                rule.handler().handle(targets, entity, resolved, visiting, index);
                return;
            }
        }
    }



    static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }


    static Map<Integer, List<String>> buildInstanceIdsByTargetId(AssemblyData assembly) {
        Map<Integer, RepresentationPayload> representationsById = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        Map<Integer, Set<String>> targetInstances = new LinkedHashMap<>();
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                targetInstances.computeIfAbsent(representationId, ignored -> new LinkedHashSet<>()).add(instance.id());
                RepresentationPayload representation = representationsById.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    targetInstances.computeIfAbsent(face.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
                for (EdgePayload edge : representation.edges()) {
                    targetInstances.computeIfAbsent(edge.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
            }
        }
        Map<Integer, List<String>> byTargetId = new LinkedHashMap<>();
        for (Map.Entry<Integer, Set<String>> entry : targetInstances.entrySet()) {
            byTargetId.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(byTargetId);
    }


    static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, viaRelationshipType, viaRelationshipId, null, null);
    }


    static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        appendPmiTarget(
                targetsByUsageId,
                usageId,
                target,
                instanceIdsByTargetId,
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                null,
                null
        );
    }


    static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId,
            String viaDefinitionType,
            Integer viaDefinitionId
    ) {
        PmiTargetPayload payload = new PmiTargetPayload(
                target.id(),
                PmiTargetHelper.pmiTargetType(target),
                PmiTargetHelper.pmiTargetName(target),
                List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), List.of())),
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                viaDefinitionType,
                viaDefinitionId
        );
        List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(usageId, ignored -> new ArrayList<>());
        if (!targets.contains(payload)) {
            targets.add(payload);
        }
    }


    static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        if (!StepPmiPayloadBuilder.isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        appendPmiTarget(targetsByUsageId, identifiedItem.id(), representation, instanceIdsByTargetId, null, null, viaUsageType, viaUsageId);
    }


    /**
     * The property-definition link types whose {@code definition().id()} match routes the
     * used representation in as a PMI target. All twelve carry the same
     * (definition, usedRepresentation) shape, so one table covers them all.
     */
    private record PropertyRepresentationLinkRule(
            Class<?> type,
            Function<StepEntity, StepPropertyDefinition> definition,
            Function<StepEntity, StepRepresentation> usedRepresentation
    ) {
        boolean matches(StepEntity candidate) {
            return type.isInstance(candidate);
        }
    }

    private static <T extends StepEntity> PropertyRepresentationLinkRule propertyLinkRule(
            Class<T> type,
            Function<T, StepPropertyDefinition> definition,
            Function<T, StepRepresentation> usedRepresentation
    ) {
        return new PropertyRepresentationLinkRule(
                type,
                candidate -> definition.apply(type.cast(candidate)),
                candidate -> usedRepresentation.apply(type.cast(candidate))
        );
    }

    private static final List<PropertyRepresentationLinkRule> PROPERTY_REPRESENTATION_LINK_RULES = List.of(
            propertyLinkRule(StepPropertyDefinitionRepresentation.class,
                    StepPropertyDefinitionRepresentation::definition, StepPropertyDefinitionRepresentation::usedRepresentation),
            propertyLinkRule(StepAttributeAssertion.class,
                    StepAttributeAssertion::definition, StepAttributeAssertion::usedRepresentation),
            propertyLinkRule(StepActionPropertyRepresentation.class,
                    StepActionPropertyRepresentation::definition, StepActionPropertyRepresentation::usedRepresentation),
            propertyLinkRule(StepContactRatioRepresentation.class,
                    StepContactRatioRepresentation::definition, StepContactRatioRepresentation::usedRepresentation),
            propertyLinkRule(StepKinematicPropertyDefinitionRepresentation.class,
                    StepKinematicPropertyDefinitionRepresentation::definition, StepKinematicPropertyDefinitionRepresentation::usedRepresentation),
            propertyLinkRule(StepKinematicPropertyMechanismRepresentation.class,
                    StepKinematicPropertyMechanismRepresentation::definition, StepKinematicPropertyMechanismRepresentation::usedRepresentation),
            propertyLinkRule(StepKinematicPropertyRepresentationRelation.class,
                    StepKinematicPropertyRepresentationRelation::definition, StepKinematicPropertyRepresentationRelation::usedRepresentation),
            propertyLinkRule(StepKinematicPropertyTopologyRepresentation.class,
                    StepKinematicPropertyTopologyRepresentation::definition, StepKinematicPropertyTopologyRepresentation::usedRepresentation),
            propertyLinkRule(StepResourcePropertyRepresentation.class,
                    StepResourcePropertyRepresentation::definition, StepResourcePropertyRepresentation::usedRepresentation),
            propertyLinkRule(StepForwardChainingRulePremise.class,
                    StepForwardChainingRulePremise::definition, StepForwardChainingRulePremise::usedRepresentation),
            propertyLinkRule(StepBackChainingRuleBody.class,
                    StepBackChainingRuleBody::definition, StepBackChainingRuleBody::usedRepresentation),
            propertyLinkRule(StepPlacedDatumTargetFeature.class,
                    StepPlacedDatumTargetFeature::definition, StepPlacedDatumTargetFeature::usedRepresentation)
    );

    /**
     * Returns the used representation of {@code candidate} when it is one of the
     * property-definition link types and its {@code definition().id()} matches
     * {@code definitionId}; otherwise null. Semantics of the former else-if
     * chains: first matching link type wins, and a definition mismatch yields
     * no target.
     */
    private static StepRepresentation usedRepresentationIfLinkMatches(StepEntity candidate, int definitionId) {
        for (PropertyRepresentationLinkRule rule : PROPERTY_REPRESENTATION_LINK_RULES) {
            if (!rule.matches(candidate)) {
                continue;
            }
            if (rule.definition().apply(candidate).id() != definitionId) {
                return null;
            }
            return rule.usedRepresentation().apply(candidate);
        }
        return null;
    }

    static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            for (PropertyRepresentationLinkRule rule : PROPERTY_REPRESENTATION_LINK_RULES) {
                if (!rule.matches(candidate)) {
                    continue;
                }
                if (rule.definition().apply(candidate).id() != propertyDefinition.id()) {
                    continue;
                }
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        rule.usedRepresentation().apply(candidate),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(candidate),
                        candidate.id()
                );
            }
        }
    }


    static Set<StepEntity> collectSemanticTargets(
            List<? extends StepEntity> entities,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity entity : entities) {
            targets.addAll(collectSemanticTargets(entity, resolved, visiting));
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectSemanticTargets(
            List<? extends StepEntity> entities,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            PmiEntityIndex index
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity entity : entities) {
            targets.addAll(collectSemanticTargets(entity, resolved, visiting, index));
        }
        return Set.copyOf(targets);
    }


    static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepSymbolRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }

}
