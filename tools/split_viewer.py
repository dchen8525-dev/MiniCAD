"""Split viewer.js into ES modules by slicing line ranges.

The code is moved verbatim -- never retyped -- so the refactor cannot introduce
transcription bugs. Each module is assembled from line ranges of the original
file; imports are then derived from which top-level names a module's body
references but does not own.

Shared mutable state is the one thing that genuinely cannot be sliced: six
variables are written from a module other than the one that reads them most
(interactiveObjects, selectedObject, pmiLabels, pmiVisible, edgeLinesVisible,
modelHasEdgeLines). ES modules let an importer read a live binding but not
assign to it, so those six move into an explicit viewState object whose name
makes every shared write greppable.

Usage:
    python tools/split_viewer.py --dry-run
    python tools/split_viewer.py
"""
import re
import os
import sys

SRC = "src/main/resources/static/viewer.js"
OUT_DIR = "src/main/resources/static/viewer"

# Names that move into the shared viewState object.
SHARED = [
    "interactiveObjects", "selectedObject", "pmiLabels",
    "pmiVisible", "edgeLinesVisible", "modelHasEdgeLines",
]

# module -> (description, [ (start, end) inclusive 1-based line ranges ])
MODULES = {
    "format": ("Text, number and box formatting helpers. No state, no DOM writes.", [
        (5, 7), (9, 12), (208, 223), (225, 240), (242, 252), (268, 282),
        (395, 403), (405, 408), (749, 759), (761, 772), (918, 920),
        (922, 925), (927, 932), (934, 936), (938, 940), (942, 944), (956, 958),
    ]),
    "log": ("Console logging with a shared prefix.", [
        (179, 179), (184, 186), (188, 190), (192, 194), (196, 198), (200, 206),
    ]),
    "bspline": ("B-spline basis evaluation and surface point/normal sampling.", [
        (946, 954), (960, 977), (979, 995), (997, 1019), (1021, 1032),
    ]),
    "matrix": ("Matrix/point conversion helpers shared by the geometry builders.", [
        (728, 735), (737, 747), (797, 799),
    ]),
    "parametric-geometry": (
        "Rebuilds parametric curve and face geometry from STEP surface payloads. "
        "The bulk of the original file, and self-contained: it needs only the "
        "B-spline and formatting helpers.", [
            (1034, 1073), (1075, 2056),
        ]),
    "scene": (
        "Three.js scene, camera, renderer, post-processing, controls and the "
        "render loop. The top-level resize()/requestRender() calls are safe: "
        "they only schedule an animation frame, so nothing crosses a module "
        "boundary during evaluation.", [
            (32, 34), (36, 50), (52, 56), (57, 121), (127, 131),
            (141, 160), (177, 177), (254, 266), (284, 359), (361, 363),
            (365, 386), (388, 389), (710, 726), (774, 795), (2285, 2313),
        ]),
    "ui-panels": ("Status, statistics, validation, product and units panels.", [
        (15, 21), (28, 30), (123, 125), (133, 139), (169, 169), (391, 393), (449, 453),
        (455, 476), (478, 507), (509, 528), (530, 544), (546, 573), (575, 602),
        (604, 640), (642, 669), (2117, 2123),
    ]),
    "assembly": ("Assembly tree rendering and per-instance visibility.", [
        (22, 22), (24, 25), (167, 168), (170, 171), (801, 840), (842, 897),
        (899, 916), (2202, 2205), (2207, 2214), (2216, 2225), (2275, 2283),
    ]),
    "pmi": ("PMI labels, their screen overlay and visibility.", [
        (23, 23), (27, 27), (2058, 2088), (2090, 2106), (2108, 2115),
        (2227, 2240), (2242, 2273),
    ]),
    "selection": ("Raycast picking, edge visibility and selected-object styling.", [
        (26, 26), (162, 163), (164, 164), (172, 172), (2125, 2128),
        (2130, 2138), (2140, 2154), (2156, 2164), (2166, 2200),
    ]),
    "model-io": ("File validation, preview requests, GLB parsing and model teardown.", [
        (14, 14), (178, 178), (180, 182), (410, 422), (424, 445), (671, 708),
        (2315, 2442), (2444, 2453), (2455, 2525), (2527, 2561), (2563, 2592),
    ]),
}

ENTRY_RANGE = (2594, 2714)

# Lines intentionally not carried over: the original three imports are replaced
# by per-module imports, the shared mutable declarations are re-declared in
# state.js, and the bootstrap call is re-issued by the new entry point.
DROPPED = set([1, 2, 3, 165, 166, 173, 174, 175, 176, 447])

THREE_IMPORTS = {
    "scene": ["import * as THREE from 'three';",
              "import { OrbitControls } from 'three/addons/controls/OrbitControls.js';",
              "import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';"],
    "parametric-geometry": ["import * as THREE from 'three';"],
    "selection": ["import * as THREE from 'three';"],
    "assembly": ["import * as THREE from 'three';"],
    "pmi": ["import * as THREE from 'three';"],
    "model-io": ["import * as THREE from 'three';",
                 "import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';"],
    "matrix": ["import * as THREE from 'three';"],
}

DECL = re.compile(r"^(export\s+)?(?:(?:async\s+)?function\s+(\w+)|(?:const|let|var)\s+(\w+))")


def check_coverage(total_lines):
    """Every non-blank line must land in exactly one module, or silently
    dropping a top-level statement (a scene.add, an event listener) would
    produce a viewer that loads but quietly does nothing."""
    covered = {}
    for name, (_, ranges) in MODULES.items():
        for start, end in ranges:
            for i in range(start, end + 1):
                covered.setdefault(i, []).append(name)
    for i in range(ENTRY_RANGE[0], ENTRY_RANGE[1] + 1):
        covered.setdefault(i, []).append("viewer.js")

    doubled = {i: m for i, m in covered.items() if len(m) > 1}
    missing = []
    for i in range(1, total_lines + 1):
        if i not in covered and i not in DROPPED and lines_of(i).strip():
            missing.append(i)
    return doubled, missing


def lines_of(i):
    return _SRC_LINES[i - 1]


def owned_names(body_lines):
    """Top-level declaration names inside a sliced body."""
    names = []
    for ln in body_lines:
        if ln[:1] in (" ", "\t", ""):
            continue
        m = DECL.match(ln)
        if m:
            names.append(m.group(2) or m.group(3))
    return names


def apply_shared_renames(text):
    """Rewrite bare references to shared state as viewState.<name>.

    Two positions are not references and must be left alone: an object literal
    key (`interactiveObjects: ...`) and a property access on something else
    (`foo.interactiveObjects`). Without those guards the rewrite turns valid
    object keys into syntax errors.
    """
    for name in SHARED:
        text = re.sub(
            r"(?<![\w.])\b" + re.escape(name) + r"\b(?!\s*:)",
            "viewState." + name,
            text,
        )
    return text


def add_exports(body_lines):
    out = []
    for ln in body_lines:
        if ln[:1] not in (" ", "\t") and ln:
            m = DECL.match(ln)
            if m and not m.group(1):
                ln = "export " + ln
        out.append(ln)
    return out


def main():
    dry = "--dry-run" in sys.argv
    lines = open(SRC, encoding="utf-8", newline="").read().split("\n")
    global _SRC_LINES
    _SRC_LINES = lines
    # lines[i] is source line i+1
    def slice_range(start, end):
        return lines[start - 1:end]

    doubled, missing = check_coverage(len(lines))
    if doubled or missing:
        print("refusing to split -- the ranges do not tile the file:")
        for i, mods in sorted(doubled.items())[:10]:
            print(f"  line {i} claimed by {mods}")
        for i in missing[:20]:
            print(f"  line {i} unclaimed: {lines[i-1].strip()[:80]}")
        sys.exit(1)
    print("coverage check: every source line lands in exactly one module")

    # 1. Slice each module and record what it owns / references.
    modules = {}
    for name, (desc, ranges) in MODULES.items():
        body = []
        for start, end in ranges:
            body.extend(slice_range(start, end))
            body.append("")
        modules[name] = {"desc": desc, "body": body, "owns": set(owned_names(body))}

    owner_of = {}
    for name, m in modules.items():
        for n in m["owns"]:
            owner_of[n] = name
    for n in SHARED:
        owner_of[n] = "state"

    # 2. Derive imports from cross-module references.
    for name, m in modules.items():
        text = "\n".join(m["body"])
        needed = set()
        for other, owner in owner_of.items():
            if owner == name or owner == "state":
                continue
            if re.search(r"\b" + re.escape(other) + r"\b", text):
                needed.add((owner, other))
        uses_state = any(re.search(r"\b" + re.escape(s) + r"\b", text) for s in SHARED)
        m["imports"] = sorted(needed)
        m["uses_state"] = uses_state

    # 3. Report cycles among modules.
    print("module sizes (source lines):")
    for name in MODULES:
        n = sum(e - s + 1 for s, e in MODULES[name][1])
        print(f"  {name:22s} {n:5d}")
    entry_lines = ENTRY_RANGE[1] - ENTRY_RANGE[0] + 1
    print(f"  {'viewer.js (entry)':22s} {entry_lines:5d}")

    edges = {name: {owner for owner, _ in m["imports"]} for name, m in modules.items()}
    print("\nmodule dependency edges:")
    for name, deps in edges.items():
        if deps:
            print(f"  {name} <- {sorted(deps)}")

    if dry:
        print("\ndry run: nothing written")
        return

    # 4. Emit state.js.
    os.makedirs(OUT_DIR, exist_ok=True)
    state_src = '''/**
 * Mutable state shared across viewer modules.
 *
 * ES modules let an importer read a live binding but not assign to it, so any
 * variable written from a module other than the one declaring it has to live
 * somewhere both sides can write. Keeping all six here -- rather than scattered
 * across the modules that happen to touch them most -- makes every shared write
 * greppable, which is the main thing a reader needs from this file.
 */
export const viewState = {
    interactiveObjects: [],
    selectedObject: null,
    pmiLabels: [],
    pmiVisible: true,
    edgeLinesVisible: false,
    modelHasEdgeLines: false
};
'''
    open(os.path.join(OUT_DIR, "state.js"), "w", encoding="utf-8", newline="\n").write(state_src)

    # 5. Emit each module.
    for name, m in modules.items():
        body = "\n".join(m["body"])
        body = apply_shared_renames(body).split("\n")
        body = add_exports(body)
        text = "\n".join(body)

        header = [f"/** {m['desc']} */", ""]
        header.extend(THREE_IMPORTS.get(name, []))
        if m["uses_state"]:
            header.append("import { viewState } from './state.js';")
        by_owner = {}
        for owner, sym in m["imports"]:
            by_owner.setdefault(owner, []).append(sym)
        for owner in sorted(by_owner):
            syms = ", ".join(sorted(by_owner[owner]))
            header.append(f"import {{ {syms} }} from './{owner}.js';")
        if header[-1] != "":
            header.append("")

        out = "\n".join(header) + text.rstrip() + "\n"
        open(os.path.join(OUT_DIR, f"{name}.js"), "w", encoding="utf-8", newline="\n").write(out)

    # 6. Emit the new entry point.
    entry_body = slice_range(*ENTRY_RANGE)
    entry_text = apply_shared_renames("\n".join(entry_body))
    entry_header = [
        "/**",
        " * Viewer entry point: wires DOM events to the viewer modules and boots once.",
        " */",
        "import { viewState } from './viewer/state.js';",
        "import {",
        "    applyEdgeVisibility, pointer, raycaster, refreshRenderableStyle,",
        "    selectRenderable, toggleEdgesButton",
        "} from './viewer/selection.js';",
        "import { applyPmiVisibility, syncPmiTargetHighlight, togglePmiButton } from './viewer/pmi.js';",
        "import { setSelection, updateEdgeToggleButton } from './viewer/ui-panels.js';",
        "import {",
        "    isolateSelectionButton, selectedAssemblyGroup, showAllButton,",
        "    showAllInstances, showOnlyInstance",
        "} from './viewer/assembly.js';",
        "import { fileInput, handleSelectedFile, loadViewerConfig } from './viewer/model-io.js';",
        "import { camera, controls, renderer, sceneHost } from './viewer/scene.js';",
        "import { logJson } from './viewer/log.js';",
        "",
        "",
    ]
    open(SRC, "w", encoding="utf-8", newline="\n").write(
        "\n".join(entry_header) + entry_text.rstrip() + "\n\nloadViewerConfig();\n")

    print(f"\nwrote {len(modules) + 2} files into {OUT_DIR}/ and rewrote {SRC}")


if __name__ == "__main__":
    main()
