package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.step.semantic.StepParameterReader;
import com.minicad.step.syntax.StepValue;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guards the convergence of {@code unwrapTyped} onto {@link StepResolverValueHelpers}:
 * StepParameterReader kept a public duplicate of the same while-loop body until its
 * body became a one-line delegation. StepEntityResolver already delegated to the
 * helpers copy, so that was the canonical. Without a guard a re-sprouted copy would
 * silently pass (identical behavior until drift), so pin: the canonical still exists,
 * the public ParameterReader facade is intact, the call site is qualified, and no
 * local while-loop body has come back in StepParameterReader.
 *
 * <p>This must NOT grow to also merge {@code literalText}: that twin is deliberate
 * (StepParameterReader additionally formats ListValue) and is documented as such.
 */
class UnwrapTypedConvergenceTest {

    private static final Path PARAMETER_READER =
            Path.of("src/main/java/com/minicad/step/semantic/StepParameterReader.java");

    private static final Path VALUE_HELPERS =
            Path.of("src/main/java/com/minicad/step/semantic/StepResolverValueHelpers.java");

    @Test
    void canonicalStillDeclaresUnwrapTyped() throws Exception {
        Method method = Class.forName("com.minicad.step.semantic.StepResolverValueHelpers")
                .getDeclaredMethod("unwrapTyped", StepValue.class);
        assertTrue(Modifier.isStatic(method.getModifiers()), "unwrapTyped must be static");
    }

    @Test
    void publicFacadeStillExists() throws Exception {
        Method method = StepParameterReader.class.getDeclaredMethod("unwrapTyped", StepValue.class);
        assertTrue(Modifier.isPublic(method.getModifiers()), "StepParameterReader.unwrapTyped must stay public");
        assertTrue(Modifier.isStatic(method.getModifiers()), "StepParameterReader.unwrapTyped must stay static");
    }

    @Test
    void parameterReaderDelegatesAndItsLocalLoopIsGone() throws Exception {
        String text = read(PARAMETER_READER);
        assertTrue(text.contains("StepResolverValueHelpers.unwrapTyped(value)"),
                "StepParameterReader must delegate with a qualified target");
        assertFalse(text.contains("while (current instanceof StepValue.TypedValue)"),
                "StepParameterReader must not carry a local unwrap loop; see StepResolverValueHelpers");
    }

    @Test
    void helpersDeclaresTheSingleLoop() throws Exception {
        String text = read(VALUE_HELPERS);
        assertTrue(text.contains("while (current instanceof StepValue.TypedValue)"),
                "StepResolverValueHelpers must retain the single unwrap loop");
    }

    private static String read(Path path) throws Exception {
        return Files.readString(path, StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
