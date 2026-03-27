package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

/**
 * Minimal tokenizer for a restricted subset of STEP syntax.
 */
public final class StepTokenizer {

    private final String input;
    private int index;

    /**
     * Creates a tokenizer.
     *
     * @param input source text
     */
    public StepTokenizer(String input) {
        this.input = input;
    }

    /**
     * Returns the next token.
     *
     * @return next token
     */
    public StepToken next() {
        skipIgnored();
        if (index >= input.length()) {
            return new StepToken(StepTokenType.EOF, "", index);
        }

        char c = input.charAt(index);
        return switch (c) {
            case '#' -> single(StepTokenType.HASH);
            case '=' -> single(StepTokenType.EQUALS);
            case '(' -> single(StepTokenType.LPAREN);
            case ')' -> single(StepTokenType.RPAREN);
            case ',' -> single(StepTokenType.COMMA);
            case ';' -> single(StepTokenType.SEMICOLON);
            case '$' -> single(StepTokenType.DOLLAR);
            case '*' -> single(StepTokenType.STAR);
            case '\'' -> stringToken();
            case '.' -> enumToken();
            default -> {
                if (isNumberStart(c)) {
                    yield numberToken();
                }
                if (isIdentifierStart(c)) {
                    yield identifierToken();
                }
                throw new StepParseException("unexpected character '" + c + "' at position " + index);
            }
        };
    }

    private StepToken single(StepTokenType type) {
        int position = index;
        index++;
        return new StepToken(type, input.substring(position, index), position);
    }

    private StepToken stringToken() {
        int start = index;
        index++;
        StringBuilder value = new StringBuilder();
        while (index < input.length()) {
            char c = input.charAt(index);
            if (c == '\'') {
                if (index + 1 < input.length() && input.charAt(index + 1) == '\'') {
                    value.append('\'');
                    index += 2;
                    continue;
                }
                index++;
                return new StepToken(StepTokenType.STRING, value.toString(), start);
            }
            value.append(c);
            index++;
        }
        throw new StepParseException("unterminated string at position " + start);
    }

    private StepToken enumToken() {
        int start = index;
        index++;
        int valueStart = index;
        while (index < input.length() && isIdentifierPart(input.charAt(index))) {
            index++;
        }
        if (index >= input.length() || input.charAt(index) != '.') {
            throw new StepParseException("unterminated enum literal at position " + start);
        }
        String value = input.substring(valueStart, index);
        if (value.isEmpty()) {
            throw new StepParseException("empty enum literal at position " + start);
        }
        index++;
        return new StepToken(StepTokenType.ENUM, value, start);
    }

    private StepToken numberToken() {
        int start = index;
        if (input.charAt(index) == '+' || input.charAt(index) == '-') {
            index++;
        }
        boolean hasDigits = false;
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            index++;
            hasDigits = true;
        }
        boolean hasDecimal = false;
        if (index < input.length() && input.charAt(index) == '.') {
            hasDecimal = true;
            index++;
            while (index < input.length() && Character.isDigit(input.charAt(index))) {
                index++;
                hasDigits = true;
            }
        }
        if (!hasDigits) {
            throw new StepParseException("invalid number at position " + start);
        }
        if (index < input.length() && (input.charAt(index) == 'E' || input.charAt(index) == 'e')) {
            hasDecimal = true;
            index++;
            if (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
                index++;
            }
            int exponentStart = index;
            while (index < input.length() && Character.isDigit(input.charAt(index))) {
                index++;
            }
            if (exponentStart == index) {
                throw new StepParseException("invalid exponent at position " + start);
            }
        }
        String raw = input.substring(start, index);
        return new StepToken(hasDecimal ? StepTokenType.NUMBER : StepTokenType.INTEGER, raw, start);
    }

    private StepToken identifierToken() {
        int start = index;
        index++;
        while (index < input.length() && isIdentifierPart(input.charAt(index))) {
            index++;
        }
        return new StepToken(StepTokenType.IDENTIFIER, input.substring(start, index), start);
    }

    private void skipIgnored() {
        while (index < input.length()) {
            char c = input.charAt(index);
            if (Character.isWhitespace(c)) {
                index++;
                continue;
            }
            if (c == '/' && index + 1 < input.length() && input.charAt(index + 1) == '*') {
                skipBlockComment();
                continue;
            }
            break;
        }
    }

    private void skipBlockComment() {
        int start = index;
        index += 2;
        while (index + 1 < input.length()) {
            if (input.charAt(index) == '*' && input.charAt(index + 1) == '/') {
                index += 2;
                return;
            }
            index++;
        }
        throw new StepParseException("unterminated comment at position " + start);
    }

    private static boolean isNumberStart(char c) {
        return c == '+' || c == '-' || Character.isDigit(c);
    }

    private static boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private static boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}
