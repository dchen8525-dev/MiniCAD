package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal parser for the STEP DATA section.
 */
public final class StepParser {

    private final StepTokenizer tokenizer;
    private StepToken current;

    private StepParser(String input) {
        this.tokenizer = new StepTokenizer(extractDataSection(input));
        this.current = tokenizer.next();
    }

    /**
     * Parses a STEP file into raw entity instances.
     *
     * @param input STEP text
     * @return parsed file
     */
    public static StepFile parse(String input) {
        return new StepParser(input).parseFile();
    }

    private StepFile parseFile() {
        List<StepEntityInstance> entities = new ArrayList<>();
        while (current.type() != StepTokenType.EOF) {
            entities.add(parseEntityInstance());
        }
        return new StepFile(entities);
    }

    private StepEntityInstance parseEntityInstance() {
        expect(StepTokenType.HASH, "expected '#'");
        int id = parseInteger(expect(StepTokenType.INTEGER, "expected entity id"));
        expect(StepTokenType.EQUALS, "expected '='");
        String name = expect(StepTokenType.IDENTIFIER, "expected entity name").text();
        expect(StepTokenType.LPAREN, "expected '(' after entity name");
        List<StepValue> parameters = parseParameterList();
        expect(StepTokenType.RPAREN, "expected ')' after entity parameters");
        expect(StepTokenType.SEMICOLON, "expected ';' after entity instance");
        return new StepEntityInstance(id, name, parameters);
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
            case DOLLAR -> {
                consume();
                yield new StepValue.OmittedValue();
            }
            case LPAREN -> parseList();
            case IDENTIFIER -> throw new StepParseException(
                    "typed parameters are unsupported at position " + current.position()
            );
            default -> throw new StepParseException(
                    "unexpected token " + current.type() + " at position " + current.position()
            );
        };
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

    private static String extractDataSection(String input) {
        if (input == null || input.isBlank()) {
            throw new StepParseException("STEP input must not be blank");
        }

        String upper = input.toUpperCase();
        int dataStart = upper.indexOf("DATA;");
        if (dataStart < 0) {
            throw new StepParseException("missing DATA section");
        }
        int contentStart = dataStart + "DATA;".length();
        int endSec = upper.indexOf("ENDSEC;", contentStart);
        if (endSec < 0) {
            throw new StepParseException("missing ENDSEC for DATA section");
        }
        return input.substring(contentStart, endSec);
    }
}
