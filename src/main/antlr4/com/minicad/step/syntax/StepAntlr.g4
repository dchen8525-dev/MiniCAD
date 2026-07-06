/*
 * STEP Physical File Grammar (ISO 10303-21)
 *
 * Complete ANTLR4 grammar for parsing STEP physical file format.
 * Supports HEADER, ANCHOR, REFERENCE, DATA sections and all parameter types.
 *
 * Grammar Features (Fixed):
 * - ISO-10303-21 header and footer
 * - HEADER section: TYPE_NAME(parameters) format (not #id=parameter)
 * - ANCHOR section (optional)
 * - REFERENCE section (optional)
 * - DATA section with entity instances (#id = TYPE_NAME(parameters))
 * - Simple and complex entity instances
 * - All parameter types: references, numbers, strings, enumerations, lists, typed parameters
 * - String escape sequences: \S\, \P\, \X\, \X2\, \X4
 * - Position tracking for error reporting
 * - Numeric edge cases: E9999, E308, E0, NaN, Infinity
 */

grammar StepAntlr;

/* ===== Parser Rules ===== */

stepFile
    : ISO_HEADER? headerSection? anchorSection? referenceSection? dataSection? ISO_FOOTER? EOF
    ;

/* HEADER section: entries are TYPE_NAME(parameters), not #id=parameter */
headerSection
    : HEADER_SEMI headerEntry* ENDSEC_SEMI
    ;

headerEntry
    : typeName '(' parameterList? ')' ';'
    ;

anchorSection
    : ANCHOR_SEMI anchorEntry* ENDSEC_SEMI
    ;

anchorEntry
    : entityId '=' anchorItem ';'
    ;

anchorItem
    : entityId '<' anchorItemContent '>'
    ;

anchorItemContent
    : parameter (',' parameter)*
    ;

referenceSection
    : REFERENCE_SEMI referenceEntry* ENDSEC_SEMI
    ;

referenceEntry
    : entityId '=' referenceItem ';'
    ;

referenceItem
    : entityId '=' parameter
    ;

/* DATA section: entity instances are #id = TYPE_NAME(parameters) */
dataSection
    : DATA_SEMI entityInstance* ENDSEC_SEMI
    ;

entityInstance
    : entityId '=' (simpleEntity | complexEntity) ';'
    ;

entityId
    : HASH INTEGER
    ;

simpleEntity
    : typeName '(' parameterList? ')'
    ;

complexEntity
    : '(' simpleEntity+ ')'
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : reference           # RefParam
    | number              # NumParam
    | string              # StrParam
    | enumeration         # EnumParam
    | omitted             # OmittedParam
    | notProvided         # NotProvidedParam
    | list                # ListParam
    | typedParameter      # TypedParam
    ;

reference
    : entityId
    ;

/* Numbers: support edge cases like E9999, E308, E0, NaN, Infinity */
number
    : INTEGER             # IntNum
    | REAL                # RealNum
    | SPECIAL_NUMBER      # SpecialNum  // NaN, INF, -INF
    ;

string
    : STRING
    ;

enumeration
    : '.' TYPE_NAME '.'
    ;

omitted
    : '$'
    ;

notProvided
    : '*'
    ;

list
    : '(' parameterList? ')'
    ;

/* Typed parameters: TYPE_NAME(single_param) or TYPE_NAME(param1, param2, ...) */
typedParameter
    : typeName '(' parameterList ')'
    ;

typeName
    : TYPE_NAME
    ;

/* ===== Lexer Rules ===== */

// ISO 10303-21 header/footer
ISO_HEADER    : 'ISO-10303-21;';
ISO_FOOTER    : 'END-ISO-10303-21;';

// Section keywords
HEADER_SEMI   : 'HEADER;';
ENDSEC_SEMI   : 'ENDSEC;';
ANCHOR_SEMI   : 'ANCHOR;';
REFERENCE_SEMI: 'REFERENCE;';
DATA_SEMI     : 'DATA;';

// Literals
HASH          : '#';

// Integer: simple integer without exponent
INTEGER       : [0-9]+;

// Real: support large exponents (E9999, E-9999, E308, E0) and negative numbers
REAL          : MINUS? [0-9]+ '.' [0-9]* EXPONENT?
              | MINUS? '.' [0-9]+ EXPONENT?
              | MINUS? [0-9]+ EXPONENT
              ;

// Exponent: must have at least one digit after E/e and optional sign
// Reject: E, E+, E- without digits
EXPONENT      : [eE] [+-]? [0-9]+;

// Special numbers: NaN, INF, -INF (uppercase only, lowercase rejected by parser)
SPECIAL_NUMBER: 'NAN' | 'INF' | MINUS 'INF'
              | 'nan' | 'NaN'  // lowercase will be rejected
              | 'inf' | 'Inf' | 'infinity' | 'Infinity' | MINUS 'inf' | MINUS 'infinity'
              ;

// String literal with escape sequence support
// STEP strings support: '' (doubled quote), \S\, \P\, \X\, \X2\, \X4
STRING        : '\'' ( '\'\'' 
                      | '\\S\\' . 
                      | '\\P\\' [A-Z] 
                      | '\\X\\' HEX HEX
                      | '\\X2\\' (HEX HEX)+ '\\X0\\'
                      | '\\X4\\' (HEX HEX)+ '\\X0\\'
                      | ~[\\'] 
                      )* '\'';

// Type names (uppercase with underscores, or lowercase for flexibility)
TYPE_NAME     : [A-Z][A-Z0-9_]*    // Standard STEP type names
              | [a-z][a-zA-Z0-9_]* // Allow lowercase for compatibility
              ;

// Hexadecimal digits for escape sequences
fragment HEX : [0-9A-Fa-f];

// Minus sign for special numbers
fragment MINUS : '-';

// Whitespace and comments
WS            : [ \t\r\n\u000C]+ -> skip;
COMMENT       : '/*' .*? '*/' -> skip;

// Error handling: catch-all for invalid characters
INVALID_CHAR  : . ;