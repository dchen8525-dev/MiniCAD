package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EVENT_RECORD.
 * An event record entity.
 *
 * @param id STEP instance id
 * @param name event name
 * @param eventType event variance type
 * @param eventSource event variance source reference
 * @param eventTime event variance occurrence time
 * @param eventDetails event variance details
 * @param eventProcessed event variance processed flag
 * @param eventStatus event variance status
 */
public record StepEventRecord(
    int id,
    String name,
    String eventType,
    StepEntity eventSource,
    StepEntity eventTime,
    List<String> eventDetails,
    boolean eventProcessed,
    String eventStatus) implements StepEntity {
}