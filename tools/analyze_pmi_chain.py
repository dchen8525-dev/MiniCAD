"""Analyse an if/else-if instanceof chain for table-driven conversion.

Two shapes have to be handled, and mixing them up silently loses branches:

  multi-line    `} else if (entity instanceof X) {`  ... body on later lines
  single-line   `} else if (entity instanceof X) { body }`   <- the whole branch
                sits on one line, so "line ends with {" is NOT a reliable
                terminator; the first `{` on the line is.

Reports the branch count, branches that group several types with `||`, types
matched more than once (unreachable in a first-match-wins chain), and body-level
`return` statements (which cannot move into a void handler lambda unchanged).

Usage:
    python tools/analyze_pmi_chain.py <file> <method-start-line> [--json out.json]
"""
import json
import re
import sys

# After the leading "}" of a "} else if (...)" line is stripped the remainder
# starts with "else if (", so all of "if (", "else if (" and "} else if (" match.
HEADER_RE = re.compile(r"^(\}\s*else\s+|else\s+)?if \(")


def method_span(lines, start):
    """Return (open_idx, close_idx) 0-based for the method starting at `start` (1-based)."""
    i = start - 1
    while "{" not in lines[i]:
        i += 1
    open_idx = i
    # Brace counting is unreliable across ~2000 lines: braces inside comments or
    # string literals skew the depth. The next member declaration is unambiguous,
    # and the method's closing "    }" is the last non-empty line before it.
    member_re = re.compile(r"^    (static|private|public|protected|final|@)")
    for j in range(open_idx + 1, len(lines)):
        if member_re.match(lines[j]):
            k = j - 1
            while k > open_idx and not lines[k].strip():
                k -= 1
            return open_idx, k
    return open_idx, len(lines) - 1


def top_level_branches(body):
    """Locate branch headers at depth 0 and capture each branch's body.

    The depth check discounts the leading "}" of a "} else if (...)" line,
    otherwise every continuation branch is missed and the whole chain collapses
    into a single branch.
    """
    depth = 0
    depth_after = []
    headers = []
    cur = None
    for k, line in enumerate(body):
        st = line.strip()
        lead = len(st) - len(st.lstrip("}"))
        core = st[lead:].strip()
        eff = depth - lead

        if eff == 0 and HEADER_RE.match(core) and "instanceof" in core:
            cur = {"header": k, "parts": [core], "open": None}
            headers.append(cur)
        elif cur is not None and cur["open"] is None:
            cur["parts"].append(st)

        # Run the "{" check in the same pass: a single-line branch already
        # carries its "{" on the header line, so deferring this to the next
        # iteration would never see it.
        if cur is not None and cur["open"] is None:
            joined = " ".join(cur["parts"])
            if "{" in joined:
                cut = joined.index("{")
                cond = joined[:cut]
                rest = joined[cut + 1:].strip()
                cur["types"] = re.findall(r"instanceof ((?:\w+\.)*\w+)", cond)
                cur["cond"] = cond
                if rest.endswith("}"):
                    cur["single_line"] = True
                    txt = rest[:-1].strip()
                    cur["body_lines"] = [txt] if txt else []
                else:
                    cur["single_line"] = False
                cur["open"] = k
                cur = None

        depth += line.count("{") - line.count("}")
        depth_after.append(depth)

    for idx, h in enumerate(headers):
        if h.get("single_line"):
            continue
        if idx + 1 < len(headers):
            end = headers[idx + 1]["header"]
        else:
            # The last branch is followed by the method's own tail
            # (visiting.remove / return), so stop where the block closes
            # instead of swallowing it.
            end = len(body)
            for j in range(h["header"] + 1, len(body)):
                if depth_after[j] == 0:
                    end = j
                    break
        h["body_lines"] = [l.strip() for l in body[h["open"] + 1:end] if l.strip()]
    return headers


def main() -> None:
    path = sys.argv[1]
    start = int(sys.argv[2])
    out_json = sys.argv[sys.argv.index("--json") + 1] if "--json" in sys.argv else None

    lines = open(path, encoding="utf-8").read().splitlines()
    open_idx, close_idx = method_span(lines, start)
    # When `start` is itself the chain's first `if` line, that line must stay in
    # the body -- dropping it shifts the depth baseline by one and every
    # continuation branch is then missed.
    if re.search(r"\bif \(", lines[start - 1]):
        body = lines[open_idx:close_idx]
    else:
        body = lines[open_idx + 1:close_idx]
    headers = top_level_branches(body)

    print(f"method lines {start}..{close_idx + 1}  (body {len(body)} lines)")
    print(f"top-level branches: {len(headers)}")

    missing = [h for h in headers if "types" not in h]
    if missing:
        print(f"  !! {len(missing)} header(s) never resolved to a '{{':")
        for h in missing[:5]:
            print(f"     line {start + 1 + h['header']}: {' '.join(h['parts'])[:100]}")
    for h in headers:
        h.setdefault("types", [])

    single = [h for h in headers if len(h["types"]) == 1]
    multi = [h for h in headers if len(h["types"]) > 1]
    oneline = [h for h in headers if h.get("single_line")]
    print(f"  single-type: {len(single)}   multi-type (||): {len(multi)}")
    print(f"  single-line branches: {len(oneline)}   multi-line: {len(headers) - len(oneline)}")
    for h in multi:
        print(f"     {len(h['types'])} types: {', '.join(h['types'][:8])}{' ...' if len(h['types']) > 8 else ''}")

    seen = {}
    dupes = []
    for h in headers:
        for t in h["types"]:
            if t in seen:
                dupes.append(t)
            else:
                seen[t] = True
    print(f"  distinct types: {len(seen)}")
    print(f"  unreachable duplicate types: {len(dupes)}")
    if dupes:
        print(f"     {sorted(set(dupes))}")

    returns = [h for h in headers if any("return" in b for b in h["body_lines"])]
    print(f"  branches containing 'return': {len(returns)}")
    for h in returns[:5]:
        print(f"     {h['types']} -> {[b[:60] for b in h['body_lines'] if 'return' in b]}")

    if out_json:
        payload = [
            {
                "types": h["types"],
                "single_line": h.get("single_line", False),
                "body": h["body_lines"],
                "line": start + 1 + h["header"],
            }
            for h in headers
        ]
        with open(out_json, "w", encoding="utf-8") as f:
            json.dump(payload, f, indent=1)
        print(f"wrote {out_json} ({len(payload)} branches)")


if __name__ == "__main__":
    main()
