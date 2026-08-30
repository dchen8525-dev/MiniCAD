package com.minicad.step.syntax;

import com.minicad.common.StepParseException;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Chunked DATA-section parsing: slices the file into the prologue (everything
 * before the DATA marker) and one statement per entity, keeping only a single
 * small token buffer and parse tree alive at a time (measured 610MB -> 150MB
 * parse-phase peak on an 8.7MB / 93k-entity file).
 *
 * <p>Every structure this path does not handle — and any per-chunk parse
 * failure — yields {@code null}, meaning "fall back to the whole-file parse"
 * in {@link StepAntlrBridge}, which then produces the authoritative error
 * diagnostics.
 */
final class StepChunkedFileParser {

    private StepChunkedFileParser() {
    }

    static StepFile tryParseInChunks(String stepText) {
        List<int[]> statements = splitTopLevelStatements(stepText);
        if (statements == null) {
            return null;
        }
        int dataStatement = -1;
        for (int i = 0; i < statements.size(); i++) {
            if (isKeywordStatement(stepText, statements.get(i), "DATA")) {
                dataStatement = i;
                break;
            }
        }
        if (dataStatement < 0) {
            return null; // no DATA section; the whole-file path handles this precisely
        }
        int endSecStatement = -1;
        for (int i = dataStatement + 1; i < statements.size(); i++) {
            if (isKeywordStatement(stepText, statements.get(i), "ENDSEC")) {
                endSecStatement = i;
                break;
            }
        }
        if (endSecStatement < 0) {
            return null; // unterminated DATA section; whole-file parse reports it
        }
        for (int i = endSecStatement + 1; i < statements.size(); i++) {
            int[] region = statements.get(i);
            if (!isKeywordStatement(stepText, region, "END-ISO-10303-21")
                    && !isBlankOrCommentRegion(stepText, region[0], region[1])) {
                return null; // content after ENDSEC is out of scope for the chunked path
            }
        }

        int prologueEnd = statements.get(dataStatement)[0];
        List<StepHeaderEntry> headerEntries = new ArrayList<>();
        if (prologueEnd > 0) {
            StepAntlrParser.FilePrologueContext prologue =
                    parseRule(stepText.substring(0, prologueEnd), StepAntlrParser::filePrologue);
            if (prologue == null || prologue.headerSection() == null) {
                return null;
            }
            for (StepAntlrParser.HeaderEntryContext entryCtx : prologue.headerSection().headerEntry()) {
                headerEntries.add(StepAntlrBridge.convertHeaderEntry(entryCtx));
            }
        }

        List<int[]> entityStatements = statements.subList(dataStatement + 1, endSecStatement);
        List<StepEntityInstance> entities = new ArrayList<>(entityStatements.size());
        Map<Integer, Integer> entityIdPositions = new HashMap<>();
        for (int[] statement : entityStatements) {
            StepAntlrParser.EntityInstanceStatementContext parsed =
                    parseRule(stepText.substring(statement[0], statement[1]), StepAntlrParser::entityInstanceStatement);
            if (parsed == null) {
                return null; // malformed entity: whole-file parse owns the diagnostics
            }
            StepAntlrParser.EntityInstanceContext entityCtx = parsed.entityInstance();
            StepEntityInstance entity;
            try {
                entity = StepAntlrBridge.convertEntityInstance(entityCtx);
            } catch (StepParseException ex) {
                // Conversion errors (non-finite numbers, bad ids, ...) report
                // chunk-relative positions; the whole-file parse reports the
                // absolute ones the tests and users see.
                return null;
            }
            int absolutePosition = statement[0] + entityCtx.getStart().getStartIndex();
            Integer firstPosition = entityIdPositions.putIfAbsent(entity.id(), absolutePosition);
            if (firstPosition != null) {
                throw new StepParseException("duplicate entity id #" + entity.id()
                        + " at position " + absolutePosition + "; first declared at position " + firstPosition);
            }
            entities.add(entity);
        }
        return new StepFile(headerEntries, entities);
    }

    /** Statement text regions [start, end) terminated by a top-level ';' — null when quotes/comments are unbalanced. */
    private static List<int[]> splitTopLevelStatements(String text) {
        List<int[]> statements = new ArrayList<>();
        boolean inString = false;
        boolean inComment = false;
        int start = 0;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char ch = text.charAt(i);
            if (inComment) {
                if (ch == '*' && i + 1 < length && text.charAt(i + 1) == '/') {
                    inComment = false;
                    i += 1;
                }
                continue;
            }
            if (inString) {
                if (ch == '\'') {
                    if (i + 1 < length && text.charAt(i + 1) == '\'') {
                        i += 1;
                    } else {
                        inString = false;
                    }
                }
                continue;
            }
            if (ch == '\'') {
                inString = true;
                continue;
            }
            if (ch == '/' && i + 1 < length && text.charAt(i + 1) == '*') {
                inComment = true;
                i += 1;
                continue;
            }
            if (ch == ';') {
                statements.add(new int[] {start, i + 1});
                start = i + 1;
            }
        }
        if (inString || inComment) {
            return null;
        }
        if (start < length) {
            statements.add(new int[] {start, length});
        }
        return statements;
    }

    /** True when the statement region is exactly the keyword (upper-case, as the grammar tokens require) plus whitespace. */
    private static boolean isKeywordStatement(String text, int[] statement, String keyword) {
        int i = statement[0];
        int end = statement[1] - 1; // position of the terminating ';'
        while (i < end && Character.isWhitespace(text.charAt(i))) {
            i += 1;
        }
        return i + keyword.length() == end && text.regionMatches(i, keyword, 0, keyword.length());
    }

    /** True when the region holds nothing but whitespace and comments. */
    private static boolean isBlankOrCommentRegion(String text, int start, int end) {
        boolean inComment = false;
        int i = start;
        while (i < end) {
            char ch = text.charAt(i);
            if (inComment) {
                if (ch == '*' && i + 1 < end && text.charAt(i + 1) == '/') {
                    inComment = false;
                    i += 1;
                }
            } else if (ch == '/' && i + 1 < end && text.charAt(i + 1) == '*') {
                inComment = true;
                i += 1;
            } else if (!Character.isWhitespace(ch)) {
                return false;
            }
            i += 1;
        }
        return true;
    }

    /** SLL + bail parse of a single small rule; null when the source does not parse. */
    private static <T extends ParserRuleContext> T parseRule(String source, Function<StepAntlrParser, T> startRule) {
        CharStream input = CharStreams.fromString(source);
        StepAntlrLexer lexer = new StepAntlrLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StepAntlrParser parser = new StepAntlrParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());
        try {
            return startRule.apply(parser);
        } catch (ParseCancellationException ex) {
            return null;
        }
    }
}
