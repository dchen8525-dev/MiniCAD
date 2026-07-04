# Contributing to MiniCAD

Thank you for your interest in contributing to MiniCAD! This document provides guidelines and information for contributors.

## Table of Contents

- [Development Setup](#development-setup)
- [Build & Test Commands](#build--test-commands)
- [Code Style](#code-style)
- [Testing Requirements](#testing-requirements)
- [Entity Support Policy](#entity-support-policy)
- [Pull Request Process](#pull-request-process)

## Development Setup

### Prerequisites

- **Java 11** (required) — Java 21+ is NOT supported
- **Maven 3.6+**
- **Git**

### Initial Setup

```bash
git clone https://github.com/dchen8525-dev/MiniCAD.git
cd MiniCAD
mvn clean compile
```

## Build & Test Commands

### Standard Build

```bash
mvn clean test                          # Run all tests
mvn -B clean test                       # Non-interactive mode (for CI)
mvn package                             # Build JAR
```

### Running Examples

```bash
# Parse a STEP file
mvn -q exec:java -Dexec.args="examples/minimal-square.step"

# Parse engine.stp (large file, ~93K entities)
mvn -q exec:java -Dexec.args="examples/engine.stp"

# Start web viewer
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java
```

### Baseline Verification (Required Before PR)

Every PR must pass these baseline checks:

```bash
# 1. All tests pass
mvn -B clean test

# 2. Example files parse successfully
mvn -q exec:java -Dexec.args="examples/minimal-square.step"
mvn -q exec:java -Dexec.args="examples/engine.stp"
```

### Code Quality Checks

```bash
# Spotless formatting check (non-generated code only)
mvn spotless:check

# Apply Spotless formatting
mvn spotless:apply

# Forbidden APIs check (non-fatal, reports only)
mvn forbiddenapis:check
```

## Code Style

### Formatting

- **Spotless** is configured for `app`, `common`, `geometry`, `geometry2d`, `topology`, `tools`, and `test` packages
- Generated STEP model classes (`step/model/**`) are excluded from formatting
- Run `mvn spotless:apply` before committing to auto-format

### Naming Conventions

- **Classes**: PascalCase (e.g., `StepEntityResolver`)
- **Methods**: camelCase (e.g., `resolveCartesianPoint`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `DEFAULT_MAX_UPLOAD_BYTES`)
- **Packages**: all lowercase (e.g., `com.minicad.geometry`)

### Documentation

- Public APIs should have Javadoc
- Complex algorithms should include inline comments
- STEP entity references should cite the standard (e.g., "ISO 10303-21")

## Testing Requirements

### Test Location

- Unit tests: `src/test/java/com/minicad/...`
- Test resources: `src/test/resources/...`

### Test Coverage

- **New features**: Must include tests
- **Bug fixes**: Must include regression tests
- **Entity support**: Should include parsing + resolution tests

### Test Naming

- Test classes: `<ClassName>Test` (e.g., `StepEntityResolverTest`)
- Test methods: `should<Action>When<Condition>` (e.g., `shouldRejectDuplicateEntityIds`)

### Running Specific Tests

```bash
# Run a single test class
mvn test -Dtest=StepEntityResolverTest

# Run tests matching a pattern
mvn test -Dtest="*Resolver*"
```

## Entity Support Policy

### Adding a New STEP Entity

1. **Model class**: Create `StepXxx` in `com.minicad.step.model.<subpackage>`
2. **Resolver**: Add `resolveXxx()` method in `StepEntityResolver`
3. **Registry**: Register in `MiscRegistry.register()`
4. **Builder**: Add `buildXxx()` method in `StepCadBuilder` (if geometry/topology)
5. **Tests**: Add parsing + resolution tests

### Entity Support Levels

| Level | Meaning |
|-------|---------|
| L0 | Model class exists |
| L1 | Registered in resolver |
| L2 | Builder references |
| L3 | Exporter/preview supports |
| L4 | Has tests |

### Unsupported Entities

- Must throw `UnsupportedStepEntityException` (not silently skip)
- Should be documented in coverage reports
- Track in `doc/generated/coverage.md`

## Pull Request Process

### Before Submitting

1. **Run baseline tests** (see above)
2. **Update documentation** if behavior changed
3. **Add tests** for new features/fixes
4. **Check code style**: `mvn spotless:apply`

### PR Description Template

```markdown
## Summary
Brief description of changes

## Type
- [ ] Bug fix
- [ ] New feature
- [ ] Entity support
- [ ] Documentation
- [ ] Refactoring

## Testing
- [ ] All existing tests pass
- [ ] New tests added
- [ ] Baseline examples verified

## Checklist
- [ ] Code follows project style
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
```

### Review Process

1. **Automated checks**: CI runs tests and examples
2. **Code review**: At least one maintainer approval
3. **Merge**: Squash merge to main branch

## Architecture Overview

See [README.md](README.md#architecture-overview) for the high-level architecture.

Key packages:
- `step.syntax`: Tokenizer, Parser (ISO 10303-21)
- `step.semantic`: Resolver, Builder
- `step.model`: 1264 entity model classes
- `geometry` / `topology`: B-Rep kernel
- `app`: CLI and web viewer

## Getting Help

- **Issues**: GitHub Issues for bugs/feature requests
- **Discussions**: GitHub Discussions for questions
- **Security**: See [SECURITY.md](SECURITY.md) for vulnerability reporting

## License

By contributing, you agree that your contributions will be licensed under the project's MIT License.
