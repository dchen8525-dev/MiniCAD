package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTechnicalNote extends AbstractStepEntity {
    private final String noteType;
    private final String noteContent;
    private final StepEntity noteAuthor;
    private final StepEntity noteDate;
    private final String varianceSubject;
    private final String noteStatus;

    public StepTechnicalNote(int id, String name, String noteType, String noteContent, StepEntity noteAuthor, StepEntity noteDate, String varianceSubject, String noteStatus) {
        super(id, name);
        this.noteType = noteType;
        this.noteContent = noteContent;
        this.noteAuthor = noteAuthor;
        this.noteDate = noteDate;
        this.varianceSubject = varianceSubject;
        this.noteStatus = noteStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("noteType", noteType);
        state.put("noteContent", noteContent);
        state.put("noteAuthor", noteAuthor);
        state.put("noteDate", noteDate);
        state.put("varianceSubject", varianceSubject);
        state.put("noteStatus", noteStatus);
        return state;
    }
}
