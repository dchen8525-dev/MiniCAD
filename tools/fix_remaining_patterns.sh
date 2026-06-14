#!/bin/bash

# Script to fix remaining pattern matching instanceof errors
# Generated after initial compilation test

echo "Finding all pattern matching instanceof..."

# Find all pattern matching with variable binding
grep -rn 'instanceof [A-Z][A-Za-z0-9_]* [a-z_][a-z0-9_]*[) {]' src/main/java/com/minicad --include="*.java" | \
  grep -v "//" | \
  while read line; do
    file=$(echo "$line" | cut -d: -f1)
    linenum=$(echo "$line" | cut -d: -f2)
    echo "  $file:$linenum"
  done

echo ""
echo "Pattern types found:"
echo "  1. Simple: instanceof Type var -> instanceof Type + Type var = (Type)x"
echo "  2. Negation: !(instanceof Type var) -> !(instanceof Type) + Type var = (Type)x"
echo "  3. Boolean: instanceof Type var && condition -> instanceof Type && ((Type)x).method()"
echo ""
echo "Manual fix needed for each pattern based on context."
echo ""
echo "Recommended: Run mvn compile iteratively and fix errors one by one."