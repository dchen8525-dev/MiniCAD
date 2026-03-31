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
        return read(Files.readAllBytes(path));
    }

    static String read(byte[] bytes) throws IOException {
        CharacterCodingException lastFailure = null;
        for (Charset charset : CHARSET_FALLBACKS) {
            try {
                return decode(bytes, charset);
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
}
