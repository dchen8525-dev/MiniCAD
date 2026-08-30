#!/usr/bin/env bash
# Computes the live-method closure of StepCadSurfaceBuilder from the entry
# points StepCadBuilder actually calls, and lists unreachable (dead) methods.
#
# Usage (repo root):  bash tools/analyze_surface_builder_closure.sh
#
# Deletion protocol (next step after reviewing the DEAD list):
#   1. Delete each dead method's full block in descending line order.
#   2. mvn -q compile  — over-deletion fails here (missing symbols).
#   3. Re-run this script: newly orphaned methods appear; repeat until stable.
#   4. mvn -B verify, then commit.
set -e
F=src/main/java/com/minicad/step/syntax/../semantic/StepCadSurfaceBuilder.java

# Method inventory: "name declarationLine"
grep -nE "^    (private|public|static).*\(" "$F" | sed 's/[(].*//' \
  | awk -F: '{n=$2; sub(/^.* /,"",n); if (n ~ /^[a-zA-Z_][a-zA-Z0-9_]*$/) print n" "$1}' > /tmp/inv.txt

# Live seeds: the only methods StepCadBuilder calls via surfaceBuilder.*
live="buildPlane buildCylindricalSurface buildConicalSurface buildToroidalSurface buildToroidalSurfaceFromSpecifiedBends buildDegenerateToroidalSurface"

changed=1
while [ "$changed" = 1 ]; do
  changed=0
  for m in $live; do
    line=$(awk -v n="$m" '$1==n{print $2}' /tmp/inv.txt)
    [ -z "$line" ] && continue
    end=$(awk -v l="$line" '$1>l{print $1; exit}' /tmp/inv.txt)
    end=${end:-$(( $(wc -l < "$F") + 1 ))}
    body=$(sed -n "${line},$((end - 1))p" "$F")
    for cand in $(awk '{print $1}' /tmp/inv.txt); do
      if echo "$body" | grep -qw "$cand" && ! echo "$live" | grep -qw "$cand"; then
        live="$live $cand"
        changed=1
      fi
    done
  done
done

echo "=== LIVE ($(echo $live | wc -w) methods) ==="
for m in $live; do echo "  $m"; done
echo ""
echo "=== DEAD (declared but not reachable from the seeds) ==="
cut -d' ' -f1 /tmp/inv.txt | while read n; do
  echo "$live" | grep -qw "$n" || echo "  $n"
done
echo ""
echo "Caveats: multi-line signatures whose name sits on the '(' line are handled;"
echo "verify the DEAD list with 'grep -rn \"<name>\" src/' before deleting - methods"
echo "referenced from StepCadBuilder's own dead dispatch copies must stay out of the closure."
