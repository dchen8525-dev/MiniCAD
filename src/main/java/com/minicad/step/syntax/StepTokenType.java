package com.minicad.step.syntax;

/**
 * Token kinds used by the minimal STEP tokenizer.
 */
public enum StepTokenType {
    HASH,
    EQUALS,
    LPAREN,
    RPAREN,
    COMMA,
    SEMICOLON,
    DOLLAR,
    STAR,
    INTEGER,
    NUMBER,
    IDENTIFIER,
    STRING,
    ENUM,
    EOF
}
