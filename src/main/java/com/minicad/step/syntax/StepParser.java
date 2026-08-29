package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

/**
 * STEP parser entry point using ANTLR4-generated parser.
 *
 * This class provides the static parse() API that the existing codebase expects,
 * while delegating to ANTLR4 via StepAntlrBridge.
 */
public final class StepParser {

    private StepParser() {
    }

    /**
     * Parse STEP physical file text and return StepFile model.
     *
     * @param stepText STEP file content (ISO-10303-21 format)
     * @return StepFile with header entries and entity instances
     * @throws StepParseException if parsing fails
     */
    public static StepFile parse(String stepText) {
        return StepAntlrBridge.parse(stepText);
    }
}
