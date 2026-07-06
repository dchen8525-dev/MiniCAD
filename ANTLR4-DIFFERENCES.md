# ANTLR4 Parser Integration Differences

## Status: Phase 1 Completed (Grammar Foundation)

**Date**: 2026-07-06
**Branch**: antlr4-grammar-fix
**Test Results**: 21/60 passing (35% pass rate, improved from 17%)

## Key Improvements

### ✅ Grammar Fixes Completed

1. **HEADER Section Format**: Corrected from `#id=parameter` to `TYPE_NAME(parameters)`
   - Matches ISO-10303-21 specification
   - Supports FILE_NAME(...), FILE_SCHEMA(...), FILE_DESCRIPTION(...)

2. **Numeric Parsing**: Enhanced edge case support
   - Large exponents: E9999, E308, E0
   - Special numbers: NaN, INF, -INF (lexer supported, Bridge validation pending)

3. **Typed Parameter List**: Corrected from single parameter to parameterList
   - Supports `TYPE_NAME(a, b, c)` format

4. **String Escape Sequences**: Comprehensive decoding in Bridge layer
   - `\S\X` single byte escape
   - `\X\HH` hex byte
   - `\X2\HHHH...\X0\` UTF-16 sequence
   - `\X4\HHHHHHHH...\X0\` UTF-32 sequence
   - `\P\A` code page directive (skipped)

## Remaining Differences (Phase 2 Work)

### ⚠️ Validation Logic Gaps

ANTLR4 parser is more tolerant than hand-written parser. Missing validations:

1. **Entity ID Range**: Hand-written parser rejected values > Integer.MAX_VALUE
   - ANTLR4 Bridge needs explicit validation
   - Test: `shouldRejectVeryLargeEntityId`

2. **NaN/Infinity**: Hand-written parser rejected non-finite numbers
   - ANTLR4 Bridge needs explicit validation
   - Tests: `shouldRejectNonFiniteNumbers`, `shouldRejectNonFiniteInList`, `shouldRejectNonFiniteInTypedValue`

3. **String Escape Validation**: Hand-written parser validated all escapes strictly
   - ANTLR4 Bridge needs enhanced validation
   - Tests: `shouldRejectTruncatedBackslashXAtEndOfString`, `shouldRejectUnsupportedStringEscapeZ`

4. **Unterminated Constructs**: Hand-written parser detected EOF in strings/comments
   - ANTLR4 lexer catches some, but error messages differ
   - Tests: `shouldRejectUnterminatedString`, `shouldRejectUnterminatedComment`

### ⚠️ Error Message Format Differences

Hand-written parser used specific error messages:
- `"missing ENDSEC for DATA section"`
- `"multiple DATA sections are not supported"`
- `"unterminated complex entity opened at position 9"`

ANTLR4 uses generic messages:
- `"line 3:0 - extraneous input '<EOF>' expecting {'ENDSEC;', '#'}"`
- `"line 4:0 - mismatched input 'DATA;' expecting {<EOF>, 'END-ISO-10303-21;'}"`

**Phase 2 Solution**: Custom error listener to format messages like hand-written parser.

### ⚠️ Complex Entity Error Position

Hand-written parser tracked opening position of unterminated complex entities:
- Error: `"unterminated complex entity opened at position 9"`

ANTLR4 reports error at closing position:
- Error: `"line 3:0 - extraneous input 'ENDSEC;' expecting {')', TYPE_NAME}"`

**Phase 2 Solution**: Enhanced context tracking in Bridge layer.

## Architecture

### File Structure

```
src/main/antlr4/com/minicad/step/syntax/StepAntlr.g4     (198 lines)
src/main/java/com/minicad/step/syntax/
  - StepAntlrBridge.java                                   (369 lines)
  - StepParser.java                                         (25 lines, wrapper)
target/generated-sources/antlr4/com/minicad/step/syntax/
  - StepAntlrLexer.java                                     (generated)
  - StepAntlrParser.java                                    (generated)
  - StepAntlrListener.java                                  (generated)
  - StepAntlrVisitor.java                                   (generated)
```

### Dependency Chain

```
StepParser.parse(String)
  → StepAntlrBridge.parse(String)
    → StepAntlrLexer (lexer)
    → StepAntlrParser (parser)
    → ParseTree conversion
      → StepFile model
```

## Next Phase: Validation Enhancement (Phase 2)

### Estimated Work: 2-3 hours

1. **Bridge Layer Validation** (1 hour)
   - Add entity ID range check (int overflow)
   - Add NaN/Infinity rejection
   - Add string escape validation
   - Add multiple DATA section rejection

2. **Custom Error Listener** (1 hour)
   - Format error messages to match hand-written parser
   - Track complex entity opening position
   - Provide specific error codes

3. **Test Compatibility** (0.5 hour)
   - Verify all 60 tests pass
   - Document any intentional API changes

4. **Documentation** (0.5 hour)
   - Update README.md
   - Update CONTRIBUTING.md
   - Create ANTLR4-INTEGRATION.md

## Backup

Hand-written parser backup preserved at:
```
/tmp/handwritten-parser-backup/
  - StepParser.java (344 lines)
  - StepTokenizer.java (384 lines)
```

Can restore if ANTLR4 integration abandoned.

## References

- ISO 10303-21: STEP Physical File Format
- ANTLR4 Documentation: https://www.antlr.org/
- Original hand-written parser: src/main/java/com/minicad/step/syntax/ (archived)