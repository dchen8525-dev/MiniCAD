"""Remove StepEntityResolver's registry-only pass-through delegates.

The facade carries ~633 methods of the shape

    StepFoo resolveFoo(StepEntityInstance instance) {
      return fooResolver.resolveFoo(instance);
    }

Most exist for exactly one reason: a registry needs a `StepEntityResolver::resolveFoo`
method reference to put in its dispatch table. A second, smaller group is called as
`resolver.resolveFoo(...)` from inside the family resolvers -- those are real facade
API (a family resolver resolving a nested reference must re-enter the facade for its
cycle detection and memoization), so they stay.

This script rewrites the first group: registries bind straight to the family resolver
(resolver.<field>.<method>(instance)) and the delegate is deleted.

Usage:
    python tools/eliminate_delegates.py --dry-run   # report only
    python tools/eliminate_delegates.py             # apply
"""
import re
import glob
import os
import sys

RESOLVER = "src/main/java/com/minicad/step/semantic/StepEntityResolver.java"
REGISTRY_GLOB = "src/main/java/com/minicad/step/semantic/*Registry*.java"

METHOD_HEAD = re.compile(r"^  (?P<ret>[A-Za-z_][\w<>\[\],.?\s]*?)\s(?P<name>\w+)\((?P<args>[^)]*)\)\s*\{$")
DELEGATE_BODY = re.compile(r"^    return (?P<field>\w+)\.(?P<method>\w+)\((?P<call>[^;]*)\);$")
FIELD_DECL = re.compile(r"^  private final (?P<type>\w+) (?P<name>\w+);$")


def parse_delegates(lines):
    """name -> list of variants; each a dict with field/target/arity/start/end."""
    out = {}
    i, n = 0, len(lines)
    while i < n:
        m = METHOD_HEAD.match(lines[i])
        if not m:
            i += 1
            continue
        j = i + 1
        body = []
        while j < n and lines[j] != "  }":
            body.append(lines[j])
            j += 1
        meaningful = [b for b in body if b.strip() and not b.strip().startswith("//")]
        if len(meaningful) == 1:
            d = DELEGATE_BODY.match(meaningful[0])
            if d:
                args = [a.strip().split()[-1] for a in m.group("args").split(",") if a.strip()]
                call = [c.strip() for c in d.group("call").split(",") if c.strip()]
                if args == call:
                    out.setdefault(m.group("name"), []).append({
                        "field": d.group("field"),
                        "target": d.group("method"),
                        "args": args,
                        "arity": len(args),
                        "start": i,
                        "end": j,          # index of the closing "  }"
                    })
        i = j + 1
    return out


def doc_block_start(lines, head):
    """Include a javadoc block only if it abuts the method head (no blank line)."""
    if head == 0:
        return head
    prev = lines[head - 1]
    if not prev.rstrip().endswith("*/"):
        return head
    k = head - 1
    while k >= 0:
        s = lines[k].strip()
        if s.startswith("/**"):
            return k
        if s.startswith("*") or s.startswith("*/"):
            k -= 1
            continue
        return head          # something unexpected in between; leave it alone
    return head


def main():
    dry = "--dry-run" in sys.argv
    lines = open(RESOLVER, encoding="utf-8").read().split("\n")
    delegates = parse_delegates(lines)

    # Names the registries bind via method reference.
    registry_refs = {}
    for path in sorted(glob.glob(REGISTRY_GLOB)):
        for name in re.findall(r"StepEntityResolver::(\w+)", open(path, encoding="utf-8").read()):
            registry_refs.setdefault(name, 0)
            registry_refs[name] += 1

    # Names the family resolvers call back through the facade.
    callbacks = set()
    for path in glob.glob("src/**/*.java", recursive=True):
        if os.path.basename(path) == "StepEntityResolver.java":
            continue
        callbacks |= set(re.findall(r"\bresolver\.(\w+)\(", open(path, encoding="utf-8").read()))

    plan = {}
    for name, variants in delegates.items():
        if name not in registry_refs or name in callbacks:
            continue
        unary = [v for v in variants if v["arity"] == 1]
        if not unary:
            continue
        plan[name] = unary[0]

    total_sites = sum(registry_refs[n] for n in plan)
    print(f"delegates parsed        : {len(delegates)}")
    print(f"registry-only removals  : {len(plan)} methods, {total_sites} call sites")
    print(f"kept (facade API/dead)  : {len(delegates) - len(plan)} methods")

    if dry:
        for name in sorted(plan)[:5]:
            print(f"  {name} -> resolver.{plan[name]['field']}.{plan[name]['target']}(instance)")
        print("dry run: nothing written")
        return

    # 1. Rewrite the registries.
    def sub(m):
        v = plan.get(m.group(1))
        if v is None:
            return m.group(0)
        return f"(resolver, instance) -> resolver.{v['field']}.{v['target']}(instance)"

    rewritten_files = 0
    replaced = 0
    for path in sorted(glob.glob(REGISTRY_GLOB)):
        text = open(path, encoding="utf-8").read()
        original = text
        new_text, hits = re.subn(r"StepEntityResolver::(\w+)", sub, text)
        if hits:
            open(path, "w", encoding="utf-8", newline="\n").write(new_text)
            rewritten_files += 1
            replaced += hits
    print(f"rewrote {replaced} method references across {rewritten_files} registry files")

    # 2. Drop the delegate methods (from the bottom up so indices stay valid).
    removal_lines = set()
    for name, v in plan.items():
        start = doc_block_start(lines, v["start"])
        removal_lines.update(range(start, v["end"] + 1))
    kept = [ln for i, ln in enumerate(lines) if i not in removal_lines]
    print(f"removed {len(removal_lines)} lines from StepEntityResolver")

    # 3. The rewritten registries reach the family resolvers directly, so the
    #    fields can no longer be private.
    used_fields = {v["field"] for v in plan.values()}
    opened = 0
    for i, ln in enumerate(kept):
        m = FIELD_DECL.match(ln)
        if m and m.group("name") in used_fields:
            kept[i] = f"  final {m.group('type')} {m.group('name')};"
            opened += 1
    print(f"made {opened} family-resolver fields package-private")

    open(RESOLVER, "w", encoding="utf-8", newline="\n").write("\n".join(kept))
    print("done -- run the build to verify")


if __name__ == "__main__":
    main()
