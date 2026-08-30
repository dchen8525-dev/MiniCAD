"""Aggregate jacoco.csv into a package/class level coverage report.

Usage:
    python tools/coverage_report.py                 # package level, top 15 by missed
    python tools/coverage_report.py --pkg <substr>  # class level inside a package
    python tools/coverage_report.py --class <Name>  # one class, by simple name
"""
import csv
import sys

CSV = "target/site/jacoco/jacoco.csv"


def load():
    rows = []
    with open(CSV, newline="", encoding="utf-8") as f:
        reader = csv.reader(f)
        next(reader)
        for row in reader:
            if "$" in row[2]:  # anonymous / inner classes double-count
                continue
            try:
                rows.append((row[1], row[2], int(row[7]), int(row[8])))
            except (ValueError, IndexError):
                continue
    return rows


def rate(missed, covered):
    total = missed + covered
    return 100.0 * covered / total if total else 0.0


def main():
    args = sys.argv[1:]
    rows = load()
    tm = sum(r[2] for r in rows)
    tc = sum(r[3] for r in rows)
    print(f"TOTAL  missed={tm}  covered={tc}  rate={rate(tm, tc):.2f}%")

    if "--class" in args:
        name = args[args.index("--class") + 1]
        for pkg, cls, lm, lc in rows:
            if cls == name:
                print(f"{pkg}.{cls:45s} missed={lm:5d} covered={lc:5d} rate={rate(lm, lc):6.2f}%")
        return

    if "--pkg" in args:
        sub = args[args.index("--pkg") + 1]
        agg = {}
        for pkg, cls, lm, lc in rows:
            if sub in pkg:
                agg[cls] = (lm, lc)
        for cls, (lm, lc) in sorted(agg.items(), key=lambda kv: -kv[1][0]):
            print(f"{cls:50s} missed={lm:5d} covered={lc:5d} rate={rate(lm, lc):6.2f}%")
        return

    agg = {}
    for pkg, cls, lm, lc in rows:
        a = agg.setdefault(pkg, [0, 0])
        a[0] += lm
        a[1] += lc
    print()
    print(f"{'PACKAGE':45s} {'missed':>7s} {'covered':>8s} {'rate':>7s}")
    for pkg, (lm, lc) in sorted(agg.items(), key=lambda kv: -kv[1][0])[:15]:
        print(f"{pkg:45s} {lm:7d} {lc:8d} {rate(lm, lc):6.2f}%")


if __name__ == "__main__":
    main()
