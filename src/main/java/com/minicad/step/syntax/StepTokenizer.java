package com.minicad.step.syntax;

import com.minicad.common.StepParseException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Tokenizer for the STEP physical-file constructs consumed by {@link StepParser}.
 *
 * <p>It skips ISO 10303-21 block comments, decodes STEP string escapes, and
 * emits tokens for raw entity parameters. It is not a standalone EXPRESS lexer;
 * schema declarations and unsupported physical-file sections are intentionally
 * outside this syntax layer.</p>
 */
public final class StepTokenizer {

    private static final String HASH_TEXT = "#";
    private static final String EQUALS_TEXT = "=";
    private static final String LPAREN_TEXT = "(";
    private static final String RPAREN_TEXT = ")";
    private static final String COMMA_TEXT = ",";
    private static final String SEMICOLON_TEXT = ";";
    private static final String DOLLAR_TEXT = "$";
    private static final String STAR_TEXT = "*";

    private final String input;
    private final int end;
    private int index;

    /**
     * Creates a tokenizer.
     *
     * @param input source text
     */
    public StepTokenizer(String input) {
        this(input, 0, input.length());
    }

    public StepTokenizer(String input, int start, int end) {
        this.input = input;
        this.index = start;
        this.end = end;
    }

    /**
     * Returns the next token.
     *
     * @return next token
     */
    public StepToken next() {
        skipIgnored();
        if (index >= end) {
            return new StepToken(StepTokenType.EOF, "", index);
        }

        char c = input.charAt(index);
        return switch (c) {
            case '#' -> single(StepTokenType.HASH, HASH_TEXT);
            case '=' -> single(StepTokenType.EQUALS, EQUALS_TEXT);
            case '(' -> single(StepTokenType.LPAREN, LPAREN_TEXT);
            case ')' -> single(StepTokenType.RPAREN, RPAREN_TEXT);
            case ',' -> single(StepTokenType.COMMA, COMMA_TEXT);
            case ';' -> single(StepTokenType.SEMICOLON, SEMICOLON_TEXT);
            case '$' -> single(StepTokenType.DOLLAR, DOLLAR_TEXT);
            case '*' -> single(StepTokenType.STAR, STAR_TEXT);
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

    private StepToken single(StepTokenType type, String text) {
        int position = index;
        index++;
        return new StepToken(type, text, position);
    }

    private StepToken stringToken() {
        int start = index;
        index++;
        StringBuilder value = new StringBuilder();
        Charset stringCharset = StandardCharsets.ISO_8859_1;
        while (index < end) {
            char c = input.charAt(index);
            if (c == '\'') {
                if (index + 1 < end && input.charAt(index + 1) == '\'') {
                    value.append('\'');
                    index += 2;
                    continue;
                }
                index++;
                return new StepToken(StepTokenType.STRING, value.toString(), start);
            }
            if (c == '\\') {
                stringCharset = appendStringEscape(value, stringCharset, start);
                continue;
            }
            value.append(c);
            index++;
        }
        throw new StepParseException("unterminated string at position " + start);
    }

    private Charset appendStringEscape(StringBuilder value, Charset stringCharset, int stringStart) {
        int escapeStart = index;
        if (index + 1 >= end) {
            throw new StepParseException("malformed string escape at position " + escapeStart);
        }
        char escapeType = input.charAt(index + 1);
        if (escapeType == 'X' || escapeType == 'x') {
            return appendHexStringEscape(value, stringCharset, escapeStart, stringStart);
        }
        if (index + 2 >= end) {
            throw new StepParseException("malformed string escape at position " + escapeStart);
        }
        if (input.charAt(index + 2) != '\\') {
            throw new StepParseException("malformed string escape at position " + escapeStart);
        }
        index += 3;
        return switch (escapeType) {
            case 'S', 's' -> {
                appendSingleByteEscape(value, stringCharset, escapeStart);
                yield stringCharset;
            }
            case 'P', 'p' -> parseCodePageEscape(stringCharset, escapeStart);
            default -> throw new StepParseException("unsupported string escape '\\" + escapeType
                    + "\\' at position " + escapeStart);
        };
    }

    private Charset appendHexStringEscape(
            StringBuilder value, Charset stringCharset, int escapeStart, int stringStart) {
        if (index + 2 >= end) {
            throw new StepParseException("malformed \\X\\ string escape at position " + escapeStart);
        }
        char mode = input.charAt(index + 2);
        if (mode == '\\') {
            index += 3;
            int code = parseHexByte(escapeStart, "\\X\\");
            value.append(new String(new byte[] {(byte) code}, stringCharset));
            return stringCharset;
        }
        if (mode == '2' || mode == '4') {
            index += 2;
            return parseHexEscape(value, stringCharset, escapeStart, stringStart);
        }
        throw new StepParseException("malformed \\X\\ string escape at position " + escapeStart);
    }

    private void appendSingleByteEscape(StringBuilder value, Charset stringCharset, int escapeStart) {
        if (index >= end) {
            throw new StepParseException("malformed \\S\\ string escape at position " + escapeStart);
        }
        char encoded = input.charAt(index);
        if (encoded > 0x7F) {
            throw new StepParseException("malformed \\S\\ string escape at position " + escapeStart);
        }
        index++;
        value.append(new String(new byte[] {(byte) (encoded + 0x80)}, stringCharset));
    }

    private Charset parseCodePageEscape(Charset current, int escapeStart) {
        if (index >= end) {
            throw new StepParseException("malformed \\P\\ string escape at position " + escapeStart);
        }
        char page = Character.toUpperCase(input.charAt(index));
        index++;
        String charsetName = switch (page) {
            case 'A' -> "ISO-8859-1";
            case 'B' -> "ISO-8859-2";
            case 'C' -> "ISO-8859-3";
            case 'D' -> "ISO-8859-4";
            case 'E' -> "ISO-8859-5";
            case 'F' -> "ISO-8859-6";
            case 'G' -> "ISO-8859-7";
            case 'H' -> "ISO-8859-8";
            case 'I' -> "ISO-8859-9";
            default -> null;
        };
        if (charsetName == null || !Charset.isSupported(charsetName)) {
            throw new StepParseException("unsupported \\P\\ string escape code page '" + page
                    + "' at position " + escapeStart);
        }
        return Charset.forName(charsetName);
    }

    private Charset parseHexEscape(StringBuilder value, Charset stringCharset, int escapeStart, int stringStart) {
        if (index >= end) {
            throw new StepParseException("malformed \\X\\ string escape at position " + escapeStart);
        }
        char mode = input.charAt(index);
        if (mode == '2') {
            consumeLongHexModeBackslash(escapeStart, "\\X2\\");
            parseLongHexEscape(value, 4, escapeStart, stringStart);
            return stringCharset;
        }
        if (mode == '4') {
            consumeLongHexModeBackslash(escapeStart, "\\X4\\");
            parseLongHexEscape(value, 8, escapeStart, stringStart);
            return stringCharset;
        }
        int code = parseHexByte(escapeStart, "\\X\\");
        value.append(new String(new byte[] {(byte) code}, stringCharset));
        return stringCharset;
    }

    private void consumeLongHexModeBackslash(int escapeStart, String escapeName) {
        if (index + 1 >= end || input.charAt(index + 1) != '\\') {
            throw new StepParseException("malformed " + escapeName + " string escape at position " + escapeStart);
        }
        index += 2;
    }

    private int parseHexByte(int escapeStart, String escapeName) {
        if (index + 1 >= end || !isHex(input.charAt(index)) || !isHex(input.charAt(index + 1))) {
            throw new StepParseException("malformed " + escapeName + " string escape at position " + escapeStart);
        }
        int code = Integer.parseInt(input.substring(index, index + 2), 16);
        index += 2;
        return code;
    }

    private void parseLongHexEscape(StringBuilder value, int hexDigitsPerCodePoint, int escapeStart, int stringStart) {
        int digitsStart = index;
        while (index < end) {
            if (index + 3 < end
                    && input.charAt(index) == '\\'
                    && (input.charAt(index + 1) == 'X' || input.charAt(index + 1) == 'x')
                    && input.charAt(index + 2) == '0'
                    && input.charAt(index + 3) == '\\') {
                int hexDigitCount = index - digitsStart;
                if (hexDigitCount == 0 || hexDigitCount % hexDigitsPerCodePoint != 0) {
                    throw new StepParseException("malformed long string escape at position " + escapeStart);
                }
                appendLongHexCodePoints(value, digitsStart, index, hexDigitsPerCodePoint, escapeStart);
                index += 4;
                return;
            }
            if (!isHex(input.charAt(index))) {
                throw new StepParseException("malformed long string escape at position " + escapeStart);
            }
            index++;
        }
        throw new StepParseException("unterminated long string escape opened at position " + escapeStart
                + " in string at position " + stringStart);
    }

    private void appendLongHexCodePoints(
            StringBuilder value, int start, int limit, int hexDigitsPerCodePoint, int escapeStart) {
        for (int i = start; i < limit; i += hexDigitsPerCodePoint) {
            int codePoint = (int) Long.parseLong(input.substring(i, i + hexDigitsPerCodePoint), 16);
            if (!Character.isValidCodePoint(codePoint)) {
                throw new StepParseException("invalid Unicode code point in string escape at position " + escapeStart);
            }
            value.appendCodePoint(codePoint);
        }
    }

    private StepToken enumToken() {
        int start = index;
        index++;
        int valueStart = index;
        while (index < end && isIdentifierPart(input.charAt(index))) {
            index++;
        }
        if (index >= end || input.charAt(index) != '.') {
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
        while (index < end && Character.isDigit(input.charAt(index))) {
            index++;
            hasDigits = true;
        }
        boolean hasDecimal = false;
        if (index < end && input.charAt(index) == '.') {
            hasDecimal = true;
            index++;
            while (index < end && Character.isDigit(input.charAt(index))) {
                index++;
                hasDigits = true;
            }
        }
        if (!hasDigits) {
            throw new StepParseException("invalid number at position " + start);
        }
        if (index < end && (input.charAt(index) == 'E' || input.charAt(index) == 'e')) {
            hasDecimal = true;
            index++;
            if (index < end && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
                index++;
            }
            int exponentStart = index;
            while (index < end && Character.isDigit(input.charAt(index))) {
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
        while (index < end && isIdentifierPart(input.charAt(index))) {
            index++;
        }
        return new StepToken(StepTokenType.IDENTIFIER, input.substring(start, index), start);
    }

    private void skipIgnored() {
        while (index < end) {
            char c = input.charAt(index);
            if (Character.isWhitespace(c)) {
                index++;
                continue;
            }
            if (c == '/' && index + 1 < end && input.charAt(index + 1) == '*') {
                skipBlockComment();
                continue;
            }
            break;
        }
    }

    private void skipBlockComment() {
        int start = index;
        index += 2;
        while (index + 1 < end) {
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

    private static boolean isHex(char c) {
        return (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'F')
                || (c >= 'a' && c <= 'f');
    }
}
