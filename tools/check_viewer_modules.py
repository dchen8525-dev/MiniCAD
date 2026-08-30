"""Static link check for the split viewer modules.

The browser is the only place this code really runs, but the failures that a
module split actually causes are all static and all caught here without one:

  1. an import naming a symbol the target module does not export
  2. an import path that does not exist
  3. a reference to a top-level name the module neither owns nor imports
      (the classic "I forgot to move the import" breakage)
  4. a bare reference to one of the six shared-state names that were supposed
      to become viewState.<name>

Run this after tools/split_viewer.py.
"""
import re
import os
import sys
from collections import defaultdict

BASE = "src/main/resources/static"
ENTRY = os.path.join(BASE, "viewer.js")

IMPORT_RE = re.compile(r"import\s+(?:\*\s+as\s+(\w+)|\{([^}]*)\})\s+from\s+'([^']+)'")
EXPORT_RE = re.compile(r"^export\s+(?:(?:async\s+)?function\s+(\w+)|(?:const|let|var)\s+(\w+))",
                       re.MULTILINE)
EXPORT_LIST_RE = re.compile(r"^export\s*\{([^}]*)\}", re.MULTILINE)


def read(path):
    return open(path, encoding="utf-8", newline="").read()


def exported_names(text):
    names = set()
    for m in EXPORT_RE.finditer(text):
        names.add(m.group(1) or m.group(2))
    for m in EXPORT_LIST_RE.finditer(text):
        for part in m.group(1).split(","):
            part = part.strip()
            if part:
                names.add(part.split()[-1])
    return names


def local_names(text, skip_imports):
    """Top-level names declared in the file (imported or not)."""
    names = set()
    for ln in text.split("\n"):
        if ln[:1] in (" ", "\t"):
            continue
        m = re.match(r"^(?:export\s+)?(?:(?:async\s+)?function\s+(\w+)|(?:const|let|var)\s+(\w+))", ln)
        if m:
            names.add(m.group(1) or m.group(2))
    if skip_imports:
        for m in IMPORT_RE.finditer(text):
            if m.group(1):
                names.add(m.group(1))
            elif m.group(2):
                for part in m.group(2).split(","):
                    part = part.strip()
                    if part:
                        names.add(part.split()[-1])
    return names


def main():
    files = {}
    viewer_dir = os.path.join(BASE, "viewer")
    for name in sorted(os.listdir(viewer_dir)):
        if name.endswith(".js"):
            files[os.path.normpath(os.path.join(viewer_dir, name))] = os.path.join(viewer_dir, name)
    files[os.path.normpath(ENTRY)] = ENTRY

    def resolve(spec, from_file):
        """Resolve a module specifier. Bare specifiers ('three',
        'three/addons/...') are handled by the page's importmap, not by
        relative path resolution, so they are reported as external."""
        if not spec.startswith("."):
            return None
        return os.path.normpath(os.path.join(os.path.dirname(from_file), spec))

    exports = {p: exported_names(read(p)) for p in files}
    problems = []

    # 1/2. imports resolve, and every named import is actually exported
    for path in files:
        spec = os.path.relpath(path, BASE).replace("\\", "/")
        text = read(path)
        for m in IMPORT_RE.finditer(text):
            target, source = resolve(m.group(3), path), m.group(1)
            if target is None:               # bare specifier, importmap's job
                continue
            if target not in files:
                problems.append(f"{spec}: imports from unknown path {m.group(3)}")
                continue
            if source:                       # namespace import, nothing to check
                continue
            for part in m.group(2).split(","):
                sym = part.strip().split()[-1]
                if sym and sym not in exports[target]:
                    problems.append(f"{spec}: imports '{sym}' from {m.group(3)}, "
                                    f"which does not export it")

    # 3. every top-level identifier a module uses is declared or imported there
    all_names = set()
    for path in files.values():
        all_names |= local_names(read(path), skip_imports=False)

    for path in files:
        spec = os.path.relpath(path, BASE).replace("\\", "/")
        text = read(path)
        declared = local_names(text, skip_imports=True)
        # strip strings and comments so prose does not look like a reference
        body = re.sub(r"//[^\n]*", "", text)
        body = re.sub(r"`[^`]*`", "``", body, flags=re.S)
        body = re.sub(r"'[^'\n]*'", "''", body)
        body = re.sub(r'"[^"\n]*"', '""', body)
        for name in all_names:
            if name in declared:
                continue
            if re.search(r"\b" + re.escape(name) + r"\b", body):
                problems.append(f"{spec}: uses '{name}' but neither declares nor imports it")

    # 4. no bare shared-state references left behind
    shared = ["interactiveObjects", "selectedObject", "pmiLabels",
              "pmiVisible", "edgeLinesVisible", "modelHasEdgeLines"]
    for spec, path in files.items():
        if spec.endswith("/state.js"):
            continue
        body = re.sub(r"//[^\n]*", "", read(path))
        for name in shared:
            for m in re.finditer(r"(?<![\w.])\b" + re.escape(name) + r"\b(?!\s*:)", body):
                line = body[:m.start()].count("\n") + 1
                problems.append(f"{spec}:{line}: bare '{name}' should be viewState.{name}")

    print(f"checked {len(files)} files")
    if problems:
        print(f"\n{len(problems)} problem(s):")
        for p in problems[:60]:
            print("  " + p)
        sys.exit(1)
    print("no unresolved imports, no undeclared references, no stray shared-state reads")


if __name__ == "__main__":
    main()
