import com.minicad.step.syntax.StepParser;
import com.minicad.step.syntax.StepFile;
import com.minicad.common.StepParseException;

public class test_grammar_only {
    public static void main(String[] args) throws Exception {
        String[] files = {
            "examples/test.step",
            "examples/nested-assembly.step",
            "examples/cylindrical-trimmed-bspline-pcurve.step"
        };

        System.out.println("=== Grammar解析测试（不resolve） ===\n");

        for (String file : files) {
            try {
                String content = java.nio.file.Files.readString(java.nio.file.Path.of(file));
                StepFile parsed = StepParser.parse(content);
                System.out.println("✅ Grammar成功: " + file);
                System.out.println("   Entities parsed: " + parsed.entities().size());
            } catch (StepParseException e) {
                System.out.println("❌ Grammar失败: " + file);
                System.out.println("   Error: " + e.getMessage());
            }
            System.out.println();
        }
    }
}
