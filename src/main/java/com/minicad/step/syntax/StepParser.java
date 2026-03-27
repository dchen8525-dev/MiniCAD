package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal parser for the STEP DATA section.
 */
public final class StepParser {

    private static final String HEADER_SECTION = "HEADER;";
    private static final String DATA_SECTION = "DATA;";
    private static final String ENDSEC = "ENDSEC;";

    private final StepTokenizer tokenizer;
    private StepToken current;

    private StepParser(String sectionText) {
        this.tokenizer = new StepTokenizer(sectionText);
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
        while (current.type() != StepTokenType.EOF) {
            entities.add(parseEntityInstance());
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
        int id = parseInteger(expect(StepTokenType.INTEGER, "expected entity id"));
        expect(StepTokenType.EQUALS, "expected '='");
        List<StepEntityDefinition> definitions = current.type() == StepTokenType.LPAREN
                ? parseComplexEntity()
                : List.of(parseEntityDefinition());
        expect(StepTokenType.SEMICOLON, "expected ';' after entity instance");
        return new StepEntityInstance(id, definitions);
    }

    private List<StepEntityDefinition> parseComplexEntity() {
        expect(StepTokenType.LPAREN, "expected '(' to open complex entity");
        List<StepEntityDefinition> definitions = new ArrayList<>();
        while (current.type() != StepTokenType.RPAREN) {
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
            case IDENTIFIER -> parseTypedValue();
            default -> throw new StepParseException(
                    "unexpected token " + current.type() + " at position " + current.position()
            );
        };
    }

    private StepValue.TypedValue parseTypedValue() {
        String typeName = expect(StepTokenType.IDENTIFIER, "expected typed value name").text();
        expect(StepTokenType.LPAREN, "expected '(' after typed value name");
        StepValue wrapped = parseValue();
        expect(StepTokenType.RPAREN, "expected ')' after typed value payload");
        return new StepValue.TypedValue(typeName, wrapped);
    }

    private StepValue.ReferenceValue parseReference() {
        consume();
        StepToken idToken = expect(StepTokenType.INTEGER, "expected referenced entity id");
        return new StepValue.ReferenceValue(parseInteger(idToken));
    }

    private StepValue.NumberValue parseNumber() {
        StepToken token = consume();
        return new StepValue.NumberValue(Double.parseDouble(token.text()), token.text());
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

    private static int parseInteger(StepToken token) {
        try {
            return Integer.parseInt(token.text());
        } catch (NumberFormatException ex) {
            throw new StepParseException("invalid integer '" + token.text() + "' at position " + token.position());
        }
    }

    private static StepFile parseSections(String input) {
        if (input == null || input.isBlank()) {
            throw new StepParseException("STEP input must not be blank");
        }

        String upper = input.toUpperCase();
        int headerStart = upper.indexOf(HEADER_SECTION);
        List<StepHeaderEntry> headerEntries = List.of();
        if (headerStart >= 0) {
            int headerContentStart = headerStart + HEADER_SECTION.length();
            int headerEnd = upper.indexOf(ENDSEC, headerContentStart);
            if (headerEnd < 0) {
                throw new StepParseException("missing ENDSEC for HEADER section");
            }
            headerEntries = new StepParser(input.substring(headerContentStart, headerEnd)).parseHeaderEntries();
        }

        int dataStart = upper.indexOf(DATA_SECTION);
        if (dataStart < 0) {
            throw new StepParseException("missing DATA section");
        }
        int contentStart = dataStart + DATA_SECTION.length();
        int endSec = upper.indexOf(ENDSEC, contentStart);
        if (endSec < 0) {
            throw new StepParseException("missing ENDSEC for DATA section");
        }
        StepFile dataFile = new StepParser(input.substring(contentStart, endSec)).parseFile();
        return new StepFile(headerEntries, dataFile.entities());
    }
}
