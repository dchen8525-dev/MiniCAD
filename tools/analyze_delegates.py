"""Measure StepEntityResolver's pass-through delegates before removing them.

Splits the delegates by *why they exist*, which decides how each can be removed:

  - registry-only : referenced only as `StepEntityResolver::name` in a registry.
                    These exist purely to feed the dispatch table and can be
                    deleted once the registries bind to the family resolver.
  - called back   : also invoked as `resolver.name(...)` from a family resolver.
                    These are real facade API -- a family resolver resolving a
                    nested reference has to re-enter the facade for its cycle
                    detection and memoization.
"""
import re
import glob
import os
from collections import Counter

RESOLVER = "src/main/java/com/minicad/step/semantic/StepEntityResolver.java"

METHOD_HEAD = re.compile(r"^  (?P<ret>[A-Za-z_][\w<>\[\],.?\s]*?)\s(?P<name>\w+)\((?P<args>[^)]*)\)\s*\{$")
DELEGATE_BODY = re.compile(r"^    return (?P<field>\w+)\.(?P<method>\w+)\((?P<call>[^;]*)\);$")


def parse_delegates(path):
    lines = open(path, encoding="utf-8").read().split("\n")
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
                    out[m.group("name")] = {
                        "field": d.group("field"),
                        "target": d.group("method"),
                        "args": args,
                        "arity": len(args),
                        "line": i + 1,
                    }
        i = j + 1
    return out


def main():
    delegates = parse_delegates(RESOLVER)
    print(f"pure pass-through delegates: {len(delegates)}")

    # Registry references.
    registry_refs = Counter()
    registry_files = sorted(glob.glob("src/main/java/com/minicad/step/semantic/*Registry*.java"))
    for path in registry_files:
        for name in re.findall(r"StepEntityResolver::(\w+)", open(path, encoding="utf-8").read()):
            registry_refs[name] += 1

    # Call-backs from every other class in the package.
    callback_refs = Counter()
    for path in glob.glob("src/main/java/com/minicad/step/semantic/*.java"):
        if os.path.basename(path) == "StepEntityResolver.java":
            continue
        text = open(path, encoding="utf-8").read()
        for name in re.findall(r"\bresolver\.(\w+)\(", text):
            callback_refs[name] += 1

    # Also: calls from other packages that hold a StepEntityResolver.
    external_refs = Counter()
    for path in glob.glob("src/main/java/com/minicad/**/*.java", recursive=True):
        if "/step/semantic/" in path.replace("\\", "/"):
            continue
        text = open(path, encoding="utf-8").read()
        for name in re.findall(r"\b\w*[Rr]esolver\.(\w+)\(", text):
            external_refs[name] += 1

    reg_only, called_back, both, neither = [], [], [], []
    for name in delegates:
        in_reg = name in registry_refs
        in_cb = name in callback_refs or name in external_refs
        if in_reg and in_cb:
            both.append(name)
        elif in_reg:
            reg_only.append(name)
        elif in_cb:
            called_back.append(name)
        else:
            neither.append(name)

    cb_sites = sum(callback_refs[n] + external_refs[n] for n in called_back + both)
    reg_sites = sum(registry_refs[n] for n in reg_only + both)

    print(f"\n-- referenced ONLY by registries (table plumbing) : {len(reg_only):4d} methods, "
          f"{sum(registry_refs[n] for n in reg_only):4d} call sites")
    print(f"-- referenced ONLY by resolver call-backs (API)   : {len(called_back):4d} methods, "
          f"{sum(callback_refs[n] + external_refs[n] for n in called_back):4d} call sites")
    print(f"-- referenced by BOTH                             : {len(both):4d} methods, "
          f"{reg_sites - sum(registry_refs[n] for n in reg_only)} registry + "
          f"{sum(callback_refs[n] + external_refs[n] for n in both)} callback sites")
    print(f"-- referenced by NEITHER (dead)                   : {len(neither):4d} methods")

    print(f"\nRemoving the registry-only set costs {sum(registry_refs[n] for n in reg_only)} rewrites.")
    print(f"Removing everything costs ~{reg_sites + cb_sites} rewrites "
          f"({reg_sites} registry + {cb_sites} callback), or the callback sites can stay put "
          f"if the facade keeps those methods.")

    if neither:
        print(f"\ndead delegates (no registry ref, no call-back) -- {len(neither)}:")
        for n in sorted(neither)[:30]:
            print(f"    {n}  ({delegates[n]['field']}.{delegates[n]['target']})")

    print("\ntop callback-heavy delegates (keep these as facade API):")
    for n in sorted(called_back + both, key=lambda x: -(callback_refs[x] + external_refs[x]))[:15]:
        print(f"    {n:45s} callback={callback_refs[n] + external_refs[n]:5d} registry={registry_refs[n]}")


if __name__ == "__main__":
    main()
