package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal parser for the STEP DATA section.
 */
public final class StepParser {

    private static final String HEADER_SECTION = "HEADER;";
    private static final String DATA_SECTION = "DATA;";
    private static final String ENDSEC = "ENDSEC;";
    private static final BigInteger MAX_SUPPORTED_ENTITY_ID = BigInteger.valueOf(Integer.MAX_VALUE);

    private final StepTokenizer tokenizer;
    private StepToken current;
    private int lastEntityIdPosition;

    private StepParser(String sectionText) {
        this.tokenizer = new StepTokenizer(sectionText);
        this.current = tokenizer.next();
    }

    private StepParser(String input, int start, int end) {
        this.tokenizer = new StepTokenizer(input, start, end);
        this.current = tokenizer.next();
    }

    /**
     * Parses a STEP file into raw entity instances.
     *
     * @param input STEP text
     * @return parsed file
     */
    public static StepFile parse(String input) {
        return parseSections(input);
    }

    private StepFile parseFile() {
        List<StepEntityInstance> entities = new ArrayList<>();
        Map<Integer, Integer> idPositions = new LinkedHashMap<>();
        while (current.type() != StepTokenType.EOF) {
            StepEntityInstance entity = parseEntityInstance();
            Integer previousPosition = idPositions.putIfAbsent(entity.id(), lastEntityIdPosition);
            if (previousPosition != null) {
                throw new StepParseException("duplicate entity id #" + entity.id()
                        + " at position " + lastEntityIdPosition
                        + "; first declared at position " + previousPosition);
            }
            entities.add(entity);
        }
        return new StepFile(entities);
    }

    private List<StepHeaderEntry> parseHeaderEntries() {
        List<StepHeaderEntry> entries = new ArrayList<>();
        while (current.type() != StepTokenType.EOF) {
            String name = expect(StepTokenType.IDENTIFIER, "expected header entry name").text();
            expect(StepTokenType.LPAREN, "expected '(' after header entry name");
            List<StepValue> parameters = parseParameterList();
            expect(StepTokenType.RPAREN, "expected ')' after header entry parameters");
            expect(StepTokenType.SEMICOLON, "expected ';' after header entry");
            entries.add(new StepHeaderEntry(name, parameters));
        }
        return List.copyOf(entries);
    }

    private StepEntityInstance parseEntityInstance() {
        expect(StepTokenType.HASH, "expected '#'");
        StepToken idToken = expect(StepTokenType.INTEGER, "expected entity id");
        int id = parseEntityId(idToken, "entity id");
        lastEntityIdPosition = idToken.position();
        expect(StepTokenType.EQUALS, "expected '='");
        List<StepEntityDefinition> definitions = current.type() == StepTokenType.LPAREN
                ? parseComplexEntity()
                : List.of(parseEntityDefinition());
        expect(StepTokenType.SEMICOLON, "expected ';' after entity instance");
        return new StepEntityInstance(id, definitions);
    }

    private List<StepEntityDefinition> parseComplexEntity() {
        int openPosition = expect(StepTokenType.LPAREN, "expected '(' to open complex entity").position();
        List<StepEntityDefinition> definitions = new ArrayList<>();
        while (current.type() != StepTokenType.RPAREN) {
            if (current.type() == StepTokenType.EOF) {
                throw new StepParseException("unterminated complex entity opened at position " + openPosition);
            }
            definitions.add(parseEntityDefinition());
        }
        expect(StepTokenType.RPAREN, "expected ')' to close complex entity");
        return List.copyOf(definitions);
    }

    private StepEntityDefinition parseEntityDefinition() {
        String name = expect(StepTokenType.IDENTIFIER, "expected entity name").text();
        expect(StepTokenType.LPAREN, "expected '(' after entity name");
        List<StepValue> parameters = parseParameterList();
        expect(StepTokenType.RPAREN, "expected ')' after entity parameters");
        return new StepEntityDefinition(name, parameters);
    }

    private List<StepValue> parseParameterList() {
        List<StepValue> values = new ArrayList<>();
        if (current.type() == StepTokenType.RPAREN) {
            return values;
        }
        values.add(parseValue());
        while (current.type() == StepTokenType.COMMA) {
            consume();
            values.add(parseValue());
        }
        return values;
    }

    private StepValue parseValue() {
        return switch (current.type()) {
            case HASH -> parseReference();
            case INTEGER, NUMBER -> parseNumber();
            case STRING -> new StepValue.StringValue(consume().text());
            case ENUM -> new StepValue.EnumValue(consume().text());
            case STAR -> {
                consume();
                yield new StepValue.NotProvidedValue();
            }
            case DOLLAR -> {
                consume();
                yield new StepValue.OmittedValue();
            }
            case LPAREN -> parseList();
            case IDENTIFIER -> {
                rejectSpecialFloatingLiteral(current);
                yield parseTypedValue();
            }
            default -> throw new StepParseException(
                    "unexpected token " + current.type() + " at position " + current.position()
            );
        };
    }

    private StepValue.TypedValue parseTypedValue() {
        String typeName = expect(StepTokenType.IDENTIFIER, "expected typed value name").text();
        expect(StepTokenType.LPAREN, "expected '(' after typed value name");
        List<StepValue> values = new ArrayList<>();
        if (current.type() != StepTokenType.RPAREN) {
            values.add(parseValue());
            while (current.type() == StepTokenType.COMMA) {
                consume();
                values.add(parseValue());
            }
        }
        expect(StepTokenType.RPAREN, "expected ')' after typed value payload");
        StepValue wrapped = values.size() == 1
                ? values.getFirst()
                : new StepValue.ListValue(values);
        return new StepValue.TypedValue(typeName, wrapped);
    }

    private StepValue.ReferenceValue parseReference() {
        consume();
        StepToken idToken = expect(StepTokenType.INTEGER, "expected referenced entity id");
        return new StepValue.ReferenceValue(parseEntityId(idToken, "referenced entity id"));
    }

    private StepValue.NumberValue parseNumber() {
        StepToken token = consume();
        double value;
        try {
            value = Double.parseDouble(token.text());
        } catch (NumberFormatException ex) {
            throw new StepParseException("invalid number '" + token.text() + "' at position " + token.position());
        }
        if (!Double.isFinite(value)) {
            throw new StepParseException("non-finite number '" + token.text() + "' at position " + token.position());
        }
        return new StepValue.NumberValue(value, token.text());
    }

    private StepValue.ListValue parseList() {
        expect(StepTokenType.LPAREN, "expected '('");
        List<StepValue> values = new ArrayList<>();
        if (current.type() != StepTokenType.RPAREN) {
            values.add(parseValue());
            while (current.type() == StepTokenType.COMMA) {
                consume();
                values.add(parseValue());
            }
        }
        expect(StepTokenType.RPAREN, "expected ')' to close list");
        return new StepValue.ListValue(values);
    }

    private StepToken expect(StepTokenType type, String message) {
        if (current.type() != type) {
            throw new StepParseException(message + " at position " + current.position());
        }
        return consume();
    }

    private StepToken consume() {
        StepToken token = current;
        current = tokenizer.next();
        return token;
    }

    private static int parseEntityId(StepToken token, String label) {
        String text = token.text();
        if (text.startsWith("+")) {
            throw new StepParseException("invalid " + label + " '#" + text + "' at position " + token.position());
        }
        BigInteger value;
        try {
            value = new BigInteger(text);
        } catch (NumberFormatException ex) {
            throw new StepParseException("invalid " + label + " '#" + text + "' at position " + token.position());
        }
        if (value.signum() <= 0) {
            throw new StepParseException(label + " '#" + text + "' must be positive at position " + token.position());
        }
        if (value.compareTo(MAX_SUPPORTED_ENTITY_ID) > 0) {
            throw new StepParseException(label + " '#" + text
                    + "' exceeds supported maximum #2147483647 at position " + token.position());
        }
        return value.intValue();
    }

    private static void rejectSpecialFloatingLiteral(StepToken token) {
        String text = token.text();
        if ("NAN".equalsIgnoreCase(text)
                || "INF".equalsIgnoreCase(text)
                || "INFINITY".equalsIgnoreCase(text)) {
            throw new StepParseException("invalid number '" + text + "' at position " + token.position());
        }
    }

    private static StepFile parseSections(String input) {
        if (input == null || input.isBlank()) {
            throw new StepParseException("STEP input must not be blank");
        }

        int headerStart = findKeywordOutsideStringsAndComments(input, HEADER_SECTION, 0);
        List<StepHeaderEntry> headerEntries = List.of();
        int dataSearchStart = 0;
        if (headerStart >= 0) {
            int headerContentStart = headerStart + HEADER_SECTION.length();
            int headerEnd = findEndsecOutsideStringsAndComments(input, headerContentStart);
            if (headerEnd < 0) {
                throw new StepParseException("missing ENDSEC for HEADER section");
            }
            headerEntries = new StepParser(input, headerContentStart, headerEnd).parseHeaderEntries();
            dataSearchStart = headerEnd + ENDSEC.length();
        }

        int dataStart = findKeywordOutsideStringsAndComments(input, DATA_SECTION, dataSearchStart);
        if (dataStart < 0) {
            throw new StepParseException("missing DATA section");
        }
        int contentStart = dataStart + DATA_SECTION.length();
        int endSec = findEndsecOutsideStringsAndComments(input, contentStart);
        if (endSec < 0) {
            throw new StepParseException("missing ENDSEC for DATA section");
        }
        int nextDataStart = findKeywordOutsideStringsAndComments(input, DATA_SECTION, endSec + ENDSEC.length());
        if (nextDataStart >= 0) {
            throw new StepParseException("multiple DATA sections are not supported");
        }
        StepFile dataFile = new StepParser(input, contentStart, endSec).parseFile();
        return new StepFile(headerEntries, dataFile.entities());
    }

    private static int findEndsecOutsideStringsAndComments(String input, int start) {
        return findKeywordOutsideStringsAndComments(input, ENDSEC, start);
    }

    private static int findKeywordOutsideStringsAndComments(String input, String keyword, int start) {
        int index = start;
        while (index < input.length()) {
            // Skip comments
            if (input.charAt(index) == '/' && index + 1 < input.length() && input.charAt(index + 1) == '*') {
                int commentEnd = input.indexOf("*/", index + 2);
                if (commentEnd < 0) {
                    throw new StepParseException("unterminated comment at position " + index);
                }
                index = commentEnd + 2;
                continue;
            }
            // Skip strings
            if (input.charAt(index) == '\'') {
                int stringStart = index;
                index++;
                boolean closed = false;
                while (index < input.length()) {
                    if (input.charAt(index) == '\'') {
                        if (index + 1 < input.length() && input.charAt(index + 1) == '\'') {
                            index += 2;
                            continue;
                        }
                        index++;
                        closed = true;
                        break;
                    }
                    index++;
                }
                if (!closed) {
                    throw new StepParseException("unterminated string at position " + stringStart);
                }
                continue;
            }
            if (isKeywordAt(input, keyword, index)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static boolean isKeywordAt(String input, String keyword, int index) {
        if (!input.regionMatches(true, index, keyword, 0, keyword.length())) {
            return false;
        }
        int before = index - 1;
        int after = index + keyword.length();
        return (before < 0 || !isSectionIdentifierPart(input.charAt(before)))
                && (after >= input.length() || !isSectionIdentifierPart(input.charAt(after)));
    }

    private static boolean isSectionIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}
