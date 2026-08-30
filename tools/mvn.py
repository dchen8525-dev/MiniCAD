"""Run Maven for MiniCAD on Windows, where mvn/JAVA_HOME are not on PATH.

Usage:
    python tools/mvn.py compile
    python tools/mvn.py -o test
    python tools/mvn.py -o test -Dtest=SomeTest

Notes that cost real debugging time:
- JAVA_HOME must be injected through subprocess `env`. `set JAVA_HOME=... & mvn.cmd`
  puts the `set` in a background subshell under Git Bash and mvn never sees it.
- Capture bytes and decode manually; `text=True` dies on the Windows code page.
- Only the tail of the output is printed so surefire noise stays out of the way.
"""
import os
import subprocess
import sys
import glob

MAVEN_GLOB = os.path.expanduser(r"~\.m2\wrapper\dists\apache-maven-3.9.16\*\bin\mvn.cmd")
# Prefer a JDK that can still target `--release 17` (the pom's maven.compiler.release).
JDK_CANDIDATES = [
    r"C:\Users\admin\.jdks\openjdk-26.0.2.1",
    r"C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.1\jbr",
    r"C:\Users\admin\.jdks\ms-11.0.32.1",
]


def pick_jdk():
    for jdk in JDK_CANDIDATES:
        if os.path.exists(os.path.join(jdk, "bin", "javac.exe")):
            return jdk
    raise SystemExit("no usable JDK found; check JDK_CANDIDATES in tools/mvn.py")


def main():
    args = sys.argv[1:]
    mavens = sorted(glob.glob(MAVEN_GLOB))
    if not mavens:
        raise SystemExit("mvn.cmd not found under ~/.m2/wrapper")
    maven = mavens[0]

    env = os.environ.copy()
    env["JAVA_HOME"] = pick_jdk()

    cmd = f'"{maven}" ' + " ".join(args)
    proc = subprocess.run(cmd, shell=True, env=env, capture_output=True)

    def dec(b):
        return b.decode("utf-8", "replace") if b else ""

    out, err = dec(proc.stdout), dec(proc.stderr)
    blob = out + err

    # Print the interesting tail: results summary, then errors, then build status.
    marker = blob.find("[INFO] Results:")
    if marker != -1:
        print(blob[marker:])
        print("---- build log tail ----")
    lines = blob.splitlines()
    tail = [ln for ln in lines if ("ERROR" in ln or "BUILD" in ln or "Tests run:" in ln)]
    print("\n".join(tail[-80:]))
    print("EXIT_CODE", proc.returncode)
    return proc.returncode


if __name__ == "__main__":
    sys.exit(main())
