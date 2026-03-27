package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parsed STEP DATA section represented as raw entity instances.
 *
 * @param headerEntries optional HEADER section entries
 * @param entities entities in source order
 */
public record StepFile(List<StepHeaderEntry> headerEntries, List<StepEntityInstance> entities) {

    public StepFile(List<StepEntityInstance> entities) {
        this(List.of(), entities);
    }

    /**
     * Creates an immutable STEP file representation.
     */
    public StepFile {
        if (headerEntries == null) {
            throw new StepParseException("header entries must not be null");
        }
        if (entities == null) {
            throw new StepParseException("entities must not be null");
        }
        headerEntries = List.copyOf(headerEntries);
        entities = List.copyOf(entities);
        for (StepHeaderEntry headerEntry : headerEntries) {
            if (headerEntry == null) {
                throw new StepParseException("header entries must not contain null");
            }
        }
        for (StepEntityInstance entity : entities) {
            if (entity == null) {
                throw new StepParseException("entities must not contain null");
            }
        }
    }

    /**
     * Returns the entities indexed by id.
     *
     * @return ordered map from id to entity
     */
    public Map<Integer, StepEntityInstance> entitiesById() {
        Map<Integer, StepEntityInstance> byId = new LinkedHashMap<>();
        for (StepEntityInstance entity : entities) {
            StepEntityInstance previous = byId.put(entity.id(), entity);
            if (previous != null) {
                throw new StepParseException("duplicate entity id #" + entity.id());
            }
        }
        return byId;
    }
}
