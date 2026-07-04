# Security Policy

MiniCAD is a local-first CAD tool. This document describes the security model and how to report vulnerabilities.

## Threat Model

### Local Viewer (StepViewerApp)

The web viewer is designed to run on `127.0.0.1:8080` by default. It accepts STEP file uploads via HTTP POST.

**Trust boundary:**
- The viewer trusts local network traffic (localhost only by default)
- File uploads are bounded (default 50MB, configurable via `--max-upload`)
- Example files are validated against a whitelist pattern `[A-Za-z0-9._-]+`
- Preview cache is bounded (default 1GB, LRU eviction)

**Not in scope:**
- Authentication/authorization (local-only tool)
- CSRF protection (no sensitive state)
- Rate limiting (single-user tool)

### STEP Parser

The parser handles untrusted STEP file content. It:
- Validates numeric bounds (rejects NaN, Infinity, extreme exponents)
- Enforces entity ID limits (≤ Integer.MAX_VALUE)
- Detects duplicate entity IDs
- Validates parameter counts and types
- Rejects malformed strings, comments, and sections

**Known limitations:**
- No schema validation against AP214/AP242 EXPRESS schemas
- Complex entity nesting depth is not bounded (but parsing is finite)
- Memory usage scales with file size (bounded by upload limit)

## Security Features

The viewer implements:
- **Upload size limits**: `--max-upload` (default 50MB), returns HTTP 413 if exceeded
- **Cache size limits**: `--max-cache` (default 1GB), LRU eviction
- **Path traversal protection**: Example names validated against whitelist
- **Atomic cache writes**: Prevents partial file reads
- **Security headers**: `X-Content-Type-Options: nosniff`, `Referrer-Policy: no-referrer`, CSP
- **Loopback binding**: Default `127.0.0.1`, warning if bound to `0.0.0.0`

## Reporting a Vulnerability

If you discover a security vulnerability, please report it via GitHub private vulnerability reporting:

1. Go to the repository's **Security** tab
2. Click **Report a vulnerability**
3. Provide a description, reproduction steps, and impact assessment

Alternatively, email: `security@example.com` (replace with actual contact)

**Please do not open public issues for security vulnerabilities.**

## Response Timeline

- **Initial response**: Within 7 days
- **Triage**: Within 14 days
- **Fix timeline**: Depends on severity and complexity

## Supported Versions

| Version | Supported |
|---------|-----------|
| 0.1.x   | ✅        |

## Security Updates

Security updates are released as patch versions (e.g., 0.1.1 → 0.1.2). Subscribe to repository releases to receive notifications.
