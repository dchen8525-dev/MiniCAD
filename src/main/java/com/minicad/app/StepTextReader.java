package com.minicad.app;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Loads STEP text from disk with conservative charset fallbacks for CAD exports.
 */
final class StepTextReader {

    private static final List<Charset> CHARSET_FALLBACKS = List.of(
            StandardCharsets.UTF_8,
            Charset.forName("GB18030"),
            StandardCharsets.ISO_8859_1
    );

    private StepTextReader() {
    }

    static String read(Path path) throws IOException {
        return readDecoded(Files.readAllBytes(path)).text();
    }

    static String read(byte[] bytes) throws IOException {
        return readDecoded(bytes).text();
    }

    static DecodedStepText readDecoded(byte[] bytes) throws IOException {
        CharacterCodingException lastFailure = null;
        for (Charset charset : CHARSET_FALLBACKS) {
            try {
                return new DecodedStepText(decode(bytes, charset), charset);
            } catch (CharacterCodingException ex) {
                lastFailure = ex;
            }
        }
        if (lastFailure != null) {
            throw lastFailure;
        }
        throw new IOException("failed to decode STEP bytes");
    }

    private static String decode(byte[] bytes, Charset charset) throws CharacterCodingException {
        CharsetDecoder decoder = charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes));
        return decoded.toString();
    }

    static final class DecodedStepText {
        private final String text;
        private final Charset charset;

        DecodedStepText(String text, Charset charset) {
            this.text = text;
            this.charset = charset;
        }

        String text() { return text; }
        Charset charset() { return charset; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DecodedStepText that = (DecodedStepText) o;
            return Objects.equals(text, that.text) && Objects.equals(charset, that.charset);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, charset);
        }

        @Override
        public String toString() {
            return "DecodedStepText{text=" + text + ", charset=" + charset + "}";
        }
    }
}
