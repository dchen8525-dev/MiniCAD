"""Wrap over-long single-line registry.put calls into the file's multi-line style.

The delegate rewrite turned

    registry.put("X", StepEntityResolver::resolveX);

into

    registry.put("X", (resolver, instance) -> resolver.someResolver.resolveX(instance));

which for long entity names blows past any sane line width. This folds those
back into the three-line form the registries already use for lambda bodies.
"""
import re
import glob

PATTERN = re.compile(
    r"^(\s*)registry\.put\("
    r'(?P<name>"[A-Z0-9_]+"), '
    r"(?P<lambda>\(resolver, instance\) -> resolver\.\w+\.\w+\(instance\))\);$"
)
LIMIT = 150


def main():
    total = 0
    files = 0
    for path in sorted(glob.glob("src/main/java/com/minicad/step/semantic/*Registry*.java")):
        lines = open(path, encoding="utf-8", newline="").read().splitlines()
        out = []
        wrapped = 0
        for line in lines:
            m = PATTERN.match(line)
            if m and len(line) > LIMIT:
                indent, name, lam = m.group(1), m.group("name"), m.group("lambda")
                out.append(f"{indent}registry.put(")
                out.append(f"{indent}    {name},")
                out.append(f"{indent}    {lam});")
                wrapped += 1
            else:
                out.append(line)
        if wrapped:
            open(path, "w", encoding="utf-8", newline="").write("\n".join(out))
            total += wrapped
            files += 1
    print(f"wrapped {total} lines across {files} registry files")


if __name__ == "__main__":
    main()
