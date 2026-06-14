#!/usr/bin/env python3
"""Comprehensive JDK 21->11 converter. Handles all remaining patterns."""
import re
from pathlib import Path

SRC = Path('D:/work/MiniCAD/src')

# ---- brace counter (ignores strings) ----
def count_braces(s):
    o = c = 0; in_s = False; esc = False
    for ch in s:
        if esc: esc = False; continue
        if ch == '\\': esc = True; continue
        if ch == '"': in_s = not in_s; continue
        if in_s: continue
        if ch == '{': o += 1
        elif ch == '}': c += 1
    return o, c

# ---- split by comma respecting <> ----
def split_by_comma(s):
    res, cur, depth = [], [], 0
    for ch in s:
        if ch == '<': depth += 1; cur.append(ch)
        elif ch == '>': depth -= 1; cur.append(ch)
        elif ch == ',' and depth == 0: res.append(''.join(cur)); cur = []
        else: cur.append(ch)
    if cur: res.append(''.join(cur))
    return res

# ---- record -> class ----
def fix_records(content):
    def conv(m):
        vis, name, params = m.group(1), m.group(2), m.group(3)
        fields = []
        for p in split_by_comma(params):
            p = p.strip()
            if not p: continue
            pp = p.rsplit(None, 1)
            if len(pp) == 2: fields.append((pp[0].strip(), pp[1].strip()))
        L = []
        L.append('%s static final class %s {' % (vis, name))
        for ft, fn in fields: L.append('    private final %s %s;' % (ft, fn))
        L.append('')
        pd = ', '.join('%s %s' % (ft, fn) for ft, fn in fields)
        L.append('    %s(%s) {' % (name, pd))
        for _, fn in fields: L.append('        this.%s = %s;' % (fn, fn))
        L.append('    }')
        L.append('')
        for ft, fn in fields: L.append('    public %s %s() { return %s; }' % (ft, fn, fn))
        L.append('')
        return '\n'.join(L)
    return re.sub(r'(private|public)\s+record\s+(\w+)\s*\(([^)]*)\)\s*\{', conv, content)

# ---- main line-by-line transformer ----
def transform(lines):
    out = []; i = 0; ecs = []; bd = 0
    while i < len(lines):
        line = lines[i]; s = line.strip()
        ind = re.match(r'^(\s*)', line).group(1)

        # insert pending extra } for compound instanceof
        while ecs and ecs[-1][0] == bd and s == '}':
            _, ei = ecs.pop(); out.append(ei + '    }')

        # --- return switch (expr) { ---
        m = re.match(r'^(\s*)return\s+switch\s*\((.+?)\)\s*\{', line)
        if m:
            si, sv = m.group(1), m.group(2).strip()
            cases, ei = parse_cases(lines, i+1)
            if cases is not None:
                emit_ie(out, si, sv, cases, True)
                oc, cc = count_braces(line)
                bd += oc - cc - 1  # -1 for skipped };
                i = ei + 1; continue

        # --- Type var = switch (expr) {  OR  var = switch (expr) { ---
        m = re.match(r'^(\s*)(.*?)\s*=\s*switch\s*\((.+?)\)\s*\{', line)
        if m:
            si = m.group(1); decl = m.group(2).strip(); sv = m.group(3).strip()
            vn = decl.split()[-1] if ' ' in decl else decl
            cases, ei = parse_cases(lines, i+1)
            if cases is not None:
                out.append('%s%s;' % (si, decl))
                emit_ie(out, si, sv, cases, False, vn)
                oc, cc = count_braces(line)
                bd += oc - cc - 1
                i = ei + 1; continue

        # --- Arrow cases in switch statements ---
        # case '"' -> expr;
        m = re.match(r"^(\s*)case '(\\\\|\\.|.)' -> (.+);$", line)
        if m:
            ci, esc, body = m.group(1), m.group(2), m.group(3)
            out.append("%scase '%s': %s; break;" % (ci, esc, body))
            oc, cc = count_braces(line); bd += oc - cc; i += 1; continue

        # case "FOO" -> expr;
        m = re.match(r'^(\s*)case "([^"]*)" -> (.+);$', line)
        if m:
            ci, lbl, body = m.group(1), m.group(2), m.group(3)
            out.append('%scase "%s": %s; break;' % (ci, lbl, body))
            oc, cc = count_braces(line); bd += oc - cc; i += 1; continue

        # default -> {
        m = re.match(r'^(\s*)default\s*->\s*\{', line)
        if m:
            ci = m.group(1)
            out.append('%sdefault:' % ci)
            j = i+1; bd2 = 1
            while j < len(lines) and bd2 > 0:
                for ch in lines[j]:
                    if ch == '{': bd2 += 1
                    elif ch == '}': bd2 -= 1
                if bd2 > 0: out.append(lines[j])
                j += 1
            out.append('%s    break;' % ci)
            bd += 0  # net braces consumed by the block = 0 (open=1, close=1, but close replaced by break)
            i = j; continue

        # --- compound instanceof else-if ---
        m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*\{', line)
        if m:
            ci,ex,tn,vn = m.group(1),m.group(2),m.group(3),m.group(4)
            cond = m.group(5).strip().rstrip('{').strip()
            out.append('%s} else if (%s instanceof %s) {' % (ci,ex,tn))
            out.append('%s    %s %s = (%s) %s;' % (ci,tn,vn,tn,ex))
            out.append('%s    if (%s) {' % (ci,cond))
            ecs.append((bd,ci))
            oc,cc=count_braces(line); bd+=oc-cc; i+=1; continue

        # --- simple instanceof else-if ---
        m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
        if m:
            ci,ex,tn,vn = m.group(1),m.group(2),m.group(3),m.group(4)
            out.append('%s} else if (%s instanceof %s) {' % (ci,ex,tn))
            out.append('%s    %s %s = (%s) %s;' % (ci,tn,vn,tn,ex))
            oc,cc=count_braces(line); bd+=oc-cc; i+=1; continue

        # --- compound instanceof if ---
        m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*\{', line)
        if m:
            ci,ex,tn,vn = m.group(1),m.group(2),m.group(3),m.group(4)
            cond = m.group(5).strip().rstrip('{').strip()
            out.append('%sif (%s instanceof %s) {' % (ci,ex,tn))
            out.append('%s    %s %s = (%s) %s;' % (ci,tn,vn,tn,ex))
            out.append('%s    if (%s) {' % (ci,cond))
            ecs.append((bd,ci))
            oc,cc=count_braces(line); bd+=oc-cc; i+=1; continue

        # --- simple instanceof if ---
        m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
        if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
            ci,ex,tn,vn = m.group(1),m.group(2),m.group(3),m.group(4)
            out.append('%sif (%s instanceof %s) {' % (ci,ex,tn))
            out.append('%s    %s %s = (%s) %s;' % (ci,tn,vn,tn,ex))
            oc,cc=count_braces(line); bd+=oc-cc; i+=1; continue

        # --- negated instanceof guard ---
        m = re.match(r'^(\s*)if\s*\(!\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\)\s*\{', line)
        if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
            ci,ex,tn,vn = m.group(1),m.group(2),m.group(3),m.group(4)
            out.append('%sif (!(%s instanceof %s)) {' % (ci,ex,tn))
            j=i+1; bd2=1
            while j<len(lines) and bd2>0:
                o2,c2=count_braces(lines[j]); bd2+=o2-c2; j+=1
            for k in range(i+1,j-1): out.append(lines[k])
            out.append('%s}' % ci)
            out.append('%s%s %s = (%s) %s;' % (ci,tn,vn,tn,ex))
            i=j; continue

        # --- yield -> return ---
        if 'yield ' in s:
            line = re.sub(r'\byield\b', 'return', line)

        oc,cc=count_braces(line); bd+=oc-cc
        out.append(line); i+=1

    while ecs: _,ei=ecs.pop(); out.append(ei+'}')
    return out

# ---- switch expression case parser ----
def parse_cases(lines, start):
    cases=[]; i=start; cur=None; bd=1
    while i<len(lines):
        line=lines[i]; s=line.strip()
        if bd==0:
            if cur: cases.append(cur)
            return cases,i
        oc, cc = count_braces(line)
        bd += oc - cc
        if bd==0:
            if cur: cases.append(cur)
            return cases,i
        # arrow block case
        m=re.match(r'^\s+case\s+(.+?)\s*->\s*\{',line)
        if m:
            if cur: cases.append(cur)
            lbl=m.group(1).strip(); body=[]; bd2=1; i+=1
            while i<len(lines) and bd2>0:
                for ch in lines[i]:
                    if ch=='{': bd2+=1
                    elif ch=='}': bd2-=1
                if bd2>0: body.append(lines[i])
                i+=1
            cur={'label':lbl,'body':body,'is_block':True,'is_default':False}
            bd -= 1; continue
        # arrow simple case
        m=re.match(r'^\s+case\s+(.+?)\s*->\s*(.+);$',line)
        if m:
            if cur: cases.append(cur)
            cases.append({'label':m.group(1).strip(),'body':[m.group(2).strip()],'is_block':False,'is_default':False})
            cur=None; i+=1; continue
        # default arrow block
        m=re.match(r'^\s+default\s*->\s*\{',line)
        if m:
            if cur: cases.append(cur)
            body=[]; bd2=1; i+=1
            while i<len(lines) and bd2>0:
                for ch in lines[i]:
                    if ch=='{': bd2+=1
                    elif ch=='}': bd2-=1
                if bd2>0: body.append(lines[i])
                i+=1
            cases.append({'label':'default','body':body,'is_block':True,'is_default':True})
            cur=None; bd -= 1; continue
        # default arrow simple
        m=re.match(r'^\s+default\s*->\s*(.+);$',line)
        if m:
            if cur: cases.append(cur)
            cases.append({'label':'default','body':[m.group(1).strip()],'is_block':False,'is_default':True})
            cur=None; i+=1; continue
        if cur: cur['body'].append(line)
        i+=1
    if cur: cases.append(cur)
    return None,i

# ---- emit if-else chain from parsed cases ----
def emit_ie(out, ind, sv, cases, is_ret=True, rv=None):
    for idx,c in enumerate(cases):
        lbl,body,ib,id_ = c['label'],c['body'],c['is_block'],c['is_default']
        if id_:
            out.append('%s} else {' % ind)
        else:
            m=re.match(r'^([\w.]+)\s+(\w+)$', lbl)
            if m:
                tn,vn = m.group(1),m.group(2)
                if idx==0: out.append('%sif (%s instanceof %s) {' % (ind,sv,tn))
                else: out.append('%s} else if (%s instanceof %s) {' % (ind,sv,tn))
                out.append('%s    %s %s = (%s) %s;' % (ind,tn,vn,tn,sv))
            else:
                if idx==0: out.append('%sif (%s.equals(%s)) {' % (ind,lbl,sv))
                else: out.append('%s} else if (%s.equals(%s)) {' % (ind,lbl,sv))
        if ib:
            for bl in body:
                bls = bl.strip()
                bls = re.sub(r'\byield\b', 'return', bls)
                out.append('%s    %s' % (ind, bls))
        else:
            expr = body[0]
            if is_ret: out.append('%s    return %s;' % (ind, expr))
            elif rv: out.append('%s    %s = %s;' % (ind, rv, expr))
            else: out.append('%s    %s;' % (ind, expr))
    out.append('%s}' % ind)

# ---- post-fixes ----
def post_fix(content):
    # boolean compound instanceof that weren't caught by line-by-line
    replacements = [
        ('|| surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());',
         '|| (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()));'),
        ('|| element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName());',
         '|| (element instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName()));'),
        ('|| point instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName());',
         '|| (point instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) point).entityName()));'),
        ('|| placement instanceof StepCartesianPoint point && point.coordinates().size() == 2;',
         '|| (placement instanceof StepCartesianPoint && ((StepCartesianPoint) placement).coordinates().size() == 2);'),
    ]
    for old, new in replacements:
        content = content.replace(old, new)
    return content

# ---- process one file ----
def process_file(path):
    content = path.read_text(encoding='utf-8')
    orig = content
    content = fix_records(content)
    lines = content.split('\n')
    lines = transform(lines)
    content = '\n'.join(lines)
    content = post_fix(content)
    if content != orig:
        path.write_text(content, encoding='utf-8')
        return True
    return False

def main():
    files = sorted(SRC.rglob('*.java'))
    print('Scanning %d files...' % len(files))
    n = 0
    for f in files:
        if process_file(f):
            n += 1
            print('  Fixed: %s' % f.relative_to(SRC))
    print('Done. %d files modified.' % n)

if __name__ == '__main__':
    main()
