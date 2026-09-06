"""Run Maven for MiniCAD on Windows, where mvn/JAVA_HOME are not on PATH.

Usage:
    python tools/mvn.py compile
    python tools/mvn.py -o test
    python tools/mvn.py -o test -Dtest=SomeTest

Notes that cost real debugging time:
- JAVA_HOME must be injected through subprocess `env`. `set JAVA_HOME=... & mvn.cmd`
  puts the `set` in a background subshell under Git Bash and mvn never sees it.
- Never capture Maven's output through a pipe (`capture_output=True` /
  `stdout=PIPE`). Maven can print more than the OS pipe buffer (~64 KiB) before
  exiting; it then blocks on write while Python blocks on read and the build
  hangs forever. Redirect to a file and read it back (see main()).
- Don't use `text=True`: the Windows code page dies on Maven's output
  (`0xc4 invalid continuation byte`); decode with `errors="replace"`.
- Only the tail of the output is printed so surefire noise stays out of the way.
"""
import os
import subprocess
import sys
import glob
import tempfile

MAVEN_GLOB = os.path.expanduser(r"~\.m2\wrapper\dists\apache-maven-3.9.16\*\bin\mvn.cmd")

# The pom's enforcer requires JDK [17,) and maven.compiler.release is 17, so the
# JDK we inject must be 17+. JDK_CANDIDATES used to be a hardcoded list of
# install paths; those rot on every IDE/JDK upgrade (openjdk-26.0.2.1 and
# "IntelliJ IDEA 2026.2.1" both disappeared, and the fallback ms-11.0.32.1 then
# tripped the enforcer with "Detected JDK version 11.0.32-1 is not in the
# allowed range [17,)"). Discover installs instead and pick by major version.
MIN_JDK_MAJOR = 17

# Directories to scan for JDK installs. Order only breaks ties between installs
# of the same major version.
JDK_SCAN_GLOBS = [
    os.path.expanduser(r"~\.jdks\*"),
    r"C:\Program Files\JetBrains\*\jbr",
    r"C:\Program Files\Java\*",
    r"C:\Program Files\Eclipse Adoptium\*",
]

# User-designated JDK (2026-09-06): the project builds on this one explicitly.
# Checked before discovery; discovery is only a fallback if it disappears.
JDK_PREFERRED = [
    r"C:\Users\admin\.jdks\ms-21.0.12.1",
]


def jdk_major(jdk_home):
    """Major version from <jdk>/release, or None if unreadable/unknown."""
    release = os.path.join(jdk_home, "release")
    if not os.path.exists(release):
        return None
    try:
        with open(release, "r", errors="replace") as handle:
            for line in handle:
                if line.startswith("JAVA_VERSION="):
                    raw = line.split("=", 1)[1].strip().strip('"')
                    # "21.0.12.1" and "1.8.0_402" both need normalising.
                    parts = raw.split(".")
                    if parts[0] == "1":
                        return int(parts[1]) if len(parts) > 1 else None
                    return int(parts[0])
    except (OSError, ValueError):
        return None
    return None


def discover_jdks():
    found = []
    for order, pattern in enumerate(JDK_SCAN_GLOBS):
        for candidate in sorted(glob.glob(pattern)):
            if not os.path.exists(os.path.join(candidate, "bin", "javac.exe")):
                continue
            found.append((order, candidate))
    return found


def pick_jdk():
    """Newest discovered JDK that satisfies the enforcer's [17,) range."""
    # The user-designated JDK wins whenever it is present and usable.
    for preferred in JDK_PREFERRED:
        major = jdk_major(preferred)
        if os.path.exists(os.path.join(preferred, "bin", "javac.exe")) \
                and major is not None and major >= MIN_JDK_MAJOR:
            return preferred

    usable = []
    for order, candidate in discover_jdks():
        major = jdk_major(candidate)
        if major is not None and major >= MIN_JDK_MAJOR:
            usable.append((major, -order, candidate))
    if not usable:
        raise SystemExit(
            "no JDK >= %d found; scanned %s. Check JDK_SCAN_GLOBS in tools/mvn.py"
            % (MIN_JDK_MAJOR, JDK_SCAN_GLOBS))
    # Highest major first; on a tie prefer the earlier scan glob.
    usable.sort(key=lambda item: (-item[0], item[1]))
    return usable[0][2]


def main():
    args = sys.argv[1:]
    mavens = sorted(glob.glob(MAVEN_GLOB))
    if not mavens:
        raise SystemExit("mvn.cmd not found under ~/.m2/wrapper")
    maven = mavens[0]

    env = os.environ.copy()
    env["JAVA_HOME"] = pick_jdk()

    cmd = f'"{maven}" ' + " ".join(args)

    # IMPORTANT: do not use capture_output=True here. Maven can emit more than
    # the OS pipe buffer (~64 KiB) before exiting; it then blocks on write while
    # Python blocks waiting for it to exit, so the build hangs forever (hit on
    # the PMI refactor: a compile that never finished in 10+ minutes). Redirect
    # to a temp file instead and read it back -- no pipe, no deadlock.
    fd, log_path = tempfile.mkstemp(prefix="minicad-mvn-", suffix=".log")
    os.close(fd)
    try:
        with open(log_path, "w", encoding="utf-8", errors="replace") as log:
            proc = subprocess.run(cmd, shell=True, env=env, stdout=log, stderr=subprocess.STDOUT)
        with open(log_path, encoding="utf-8", errors="replace") as log:
            blob = log.read()
    finally:
        try:
            os.remove(log_path)
        except OSError:
            pass

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
