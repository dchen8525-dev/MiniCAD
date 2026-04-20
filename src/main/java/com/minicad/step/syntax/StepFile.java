package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parsed STEP DATA section represented as raw entity instances.
 */
public final class StepFile {

    private final List<StepHeaderEntry> headerEntries;
    private final List<StepEntityInstance> entities;
    private volatile Map<Integer, StepEntityInstance> entitiesById;

    public StepFile(List<StepEntityInstance> entities) {
        this(List.of(), entities);
    }

    public StepFile(List<StepHeaderEntry> headerEntries, List<StepEntityInstance> entities) {
        if (headerEntries == null) {
            throw new StepParseException("header entries must not be null");
        }
        if (entities == null) {
            throw new StepParseException("entities must not be null");
        }
        this.headerEntries = List.copyOf(headerEntries);
        this.entities = List.copyOf(entities);
        for (StepHeaderEntry entry : this.headerEntries) {
            if (entry == null) {
                throw new StepParseException("header entries must not contain null");
            }
        }
        for (StepEntityInstance entity : this.entities) {
            if (entity == null) {
                throw new StepParseException("entities must not contain null");
            }
        }
    }

    public List<StepHeaderEntry> headerEntries() {
        return headerEntries;
    }

    public List<StepEntityInstance> entities() {
        return entities;
    }

    public Map<Integer, StepEntityInstance> entitiesById() {
        Map<Integer, StepEntityInstance> local = entitiesById;
        if (local == null) {
            local = buildEntitiesById();
            entitiesById = local;
        }
        return local;
    }

    private Map<Integer, StepEntityInstance> buildEntitiesById() {
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
