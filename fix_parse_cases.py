#!/usr/bin/env python3
"""Fix parse_cases bug in convert_all.py - block case braces."""
from pathlib import Path

p = Path('D:/work/MiniCAD/convert_all.py')
content = p.read_text(encoding='utf-8')

# Fix 1: Use count_braces in parse_cases instead of raw char counting
old1 = "        for ch in s:\n            if ch=='{': bd+=1\n            elif ch=='}': bd-=1"
new1 = "        oc, cc = count_braces(line)\n        bd += oc - cc"
if old1 in content:
    content = content.replace(old1, new1)
    print("Fixed brace counting in parse_cases")
else:
    print("WARNING: brace counting pattern not found")

# Fix 2: Decrement bd after arrow block case
old2 = "cur={'label':lbl,'body':body,'is_block':True,'is_default':False}; continue"
new2 = "cur={'label':lbl,'body':body,'is_block':True,'is_default':False}\n            bd -= 1; continue"
if old2 in content:
    content = content.replace(old2, new2)
    print("Fixed block case bd tracking")
else:
    print("WARNING: block case pattern not found")

# Fix 3: Decrement bd after default block case  
old3 = "cur=None; continue"
new3 = "cur=None; bd -= 1; continue"
# Be careful to only replace the one inside parse_cases (after default block)
# Find the context: after 'is_default':True} line
content = content.replace(
    "cases.append({'label':'default','body':body,'is_block':True,'is_default':True})\n            cur=None; continue",
    "cases.append({'label':'default','body':body,'is_block':True,'is_default':True})\n            cur=None; bd -= 1; continue"
)
print("Fixed default block bd tracking")

p.write_text(content, encoding='utf-8')
print("Done")
