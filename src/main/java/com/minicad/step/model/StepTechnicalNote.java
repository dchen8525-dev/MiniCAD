package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTechnicalNote implements StepEntity {
    private final int id;
    private final String name;
    private final String noteType;
    private final String noteContent;
    private final StepEntity noteAuthor;
    private final StepEntity noteDate;
    private final String varianceSubject;
    private final String noteStatus;

    public StepTechnicalNote(int id, String name, String noteType, String noteContent, StepEntity noteAuthor, StepEntity noteDate, String varianceSubject, String noteStatus) {
        this.id = id;
        this.name = name;
        this.noteType = noteType;
        this.noteContent = noteContent;
        this.noteAuthor = noteAuthor;
        this.noteDate = noteDate;
        this.varianceSubject = varianceSubject;
        this.noteStatus = noteStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNoteType() {
        return noteType;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public StepEntity getNoteAuthor() {
        return noteAuthor;
    }

    public StepEntity getNoteDate() {
        return noteDate;
    }

    public String getVarianceSubject() {
        return varianceSubject;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTechnicalNote that = (StepTechnicalNote) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(noteType, that.noteType) && Objects.equals(noteContent, that.noteContent) && Objects.equals(noteAuthor, that.noteAuthor) && Objects.equals(noteDate, that.noteDate) && Objects.equals(varianceSubject, that.varianceSubject) && Objects.equals(noteStatus, that.noteStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, noteType, noteContent, noteAuthor, noteDate, varianceSubject, noteStatus);
    }

    @Override
    public String toString() {
        return "StepTechnicalNote{" + "id=" + id + "name=" + name + "noteType=" + noteType + "noteContent=" + noteContent + "noteAuthor=" + noteAuthor + "noteDate=" + noteDate + "varianceSubject=" + varianceSubject + "noteStatus=" + noteStatus + "}";
    }
}