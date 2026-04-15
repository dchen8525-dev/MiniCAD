package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TECHNICAL_NOTE.
 * A technical note entity.
 *
 * @param id STEP instance id
 * @param name note name
 * @param noteType note type (calculation, analysis, comment)
 * @param noteContent note content text
 * @param noteAuthor note author reference
 * @param noteDate note date
 * @varianceSubject note variance subject/topic
 * @param noteStatus note status
 */
public record StepTechnicalNote(
    int id,
    String name,
    String noteType,
    String noteContent,
    StepEntity noteAuthor,
    StepEntity noteDate,
    String varianceSubject,
    String noteStatus) implements StepEntity {
}