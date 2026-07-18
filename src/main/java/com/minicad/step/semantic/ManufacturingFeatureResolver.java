package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import java.util.List;

/**
 * Resolver for manufacturing feature entities in STEP files.
 * Handles resolution of pockets, holes, slots, grooves, and other machining features.
 */
class ManufacturingFeatureResolver {

    private final StepEntityResolver resolver;

    ManufacturingFeatureResolver(StepEntityResolver resolver) {
        this.resolver = resolver;
    }

    StepPocket resolvePocket(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "POCKET");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepPocket(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 1),
                StepEntity.class,
                "POCKET profile must reference a profile"),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 3),
                StepEntity.class,
                "POCKET direction must reference a direction"),
            resolver.stringValue(instance, definition, 4));
    }

    StepBore resolveBore(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "BORE");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepBore(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 1),
                StepEntity.class,
                "BORE profile must reference a profile"),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 3),
                StepEntity.class,
                "BORE direction must reference a direction"));
    }

    StepCounterboreHole resolveCounterboreHole(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "COUNTERBORE_HOLE");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepCounterboreHole(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 1),
                StepEntity.class,
                "COUNTERBORE_HOLE through_hole must reference a hole"),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.optionalNumberValue(instance, definition, 3));
    }

    StepCountersinkHole resolveCountersinkHole(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "COUNTERSINK_HOLE");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepCountersinkHole(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.requireEntity(
                resolver.referenceId(instance, definition, 1),
                StepEntity.class,
                "COUNTERSINK_HOLE through_hole must reference a hole"),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.optionalNumberValue(instance, definition, 3));
    }

    StepRound resolveRound(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "ROUND");
        resolver.requireParameterCount(instance, definition, 4);
        List<StepEntity> edges =
            resolver.entityReferenceList(
                instance, definition, 2,
                "ROUND edges must contain entity references");
        return new StepRound(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            edges,
            resolver.optionalNumberValue(instance, definition, 3));
    }

    StepGroove resolveGroove(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "GROOVE");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepGroove(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)));
    }

    StepHole resolveHole(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "HOLE");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepHole(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)),
            resolver.stringValue(instance, definition, 4));
    }

    StepSlot resolveSlot(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "SLOT");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepSlot(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)),
            resolver.optionalNumberValue(instance, definition, 4));
    }

    StepStud resolveStud(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "STUD");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepStud(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)));
    }

    StepProtrusion resolveProtrusion(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "PROTRUSION");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepProtrusion(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)),
            resolver.optionalNumberValue(instance, definition, 4));
    }

    StepCutout resolveCutout(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CUTOUT");
        resolver.requireParameterCount(instance, definition, 5);
        return new StepCutout(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)));
    }

    StepDepression resolveDepression(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "DEPRESSION");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepDepression(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.optionalNumberValue(instance, definition, 2),
            resolver.resolve(resolver.referenceId(instance, definition, 3)),
            resolver.optionalNumberValue(instance, definition, 4));
    }

    StepCircularPattern resolveCircularPattern(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "CIRCULAR_PATTERN");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepCircularPattern(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.resolve(resolver.referenceId(instance, definition, 2)),
            resolver.optionalNumberValue(instance, definition, 3),
            resolver.optionalIntegerValue(instance, definition, 4));
    }

    StepLinearPattern resolveLinearPattern(StepEntityInstance instance) {
        StepEntityDefinition definition = resolver.definition(instance, "LINEAR_PATTERN");
        resolver.requireParameterCount(instance, definition, 6);
        return new StepLinearPattern(
            instance.id(),
            resolver.stringValue(instance, definition, 0),
            resolver.resolve(resolver.referenceId(instance, definition, 1)),
            resolver.resolve(resolver.referenceId(instance, definition, 2)),
            resolver.optionalNumberValue(instance, definition, 3),
            resolver.optionalIntegerValue(instance, definition, 4));
    }
}