package lexer;

import java.util.Set;

sealed interface TokenType permits EOFTokenType, OneCharTokenType, TwoCharTokenType, UnexpectedTokenType {
  Set<Class<? extends TokenType>> FIXED_LENGTH_TOKEN_TYPES = Set.of(
    OneCharTokenType.class, TwoCharTokenType.class
  );

  String type();

  String lexeme();

  int length();
}

enum OneCharTokenType implements TokenType {
  LEFT_PAREN("LEFT_PAREN", "("),
  RIGHT_PAREN("RIGHT_PAREN", ")"),
  LEFT_BRACE("LEFT_BRACE", "{"),
  RIGHT_BRACE("RIGHT_BRACE", "}"),
  COMMA("COMMA", ","),
  DOT("DOT", "."),
  MINUS("MINUS", "-"),
  PLUS("PLUS", "+"),
  SEMICOLON("SEMICOLON", ";"),
  SLASH("SLASH", "/"),
  STAR("STAR", "*"),
  BANG("BANG", "!"),
  EQUAL("EQUAL", "="),
  GREATER("GREATER", ">"),
  LESS("LESS", "<");

  private final String type;
  private final String lexeme;

  OneCharTokenType(String type, String lexeme) {
    this.type = type;
    this.lexeme = lexeme;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String lexeme() {
    return lexeme;
  }

  @Override
  public int length() {
    return 1;
  }

  @Override
  public String toString() {
    return type;
  }
}

enum TwoCharTokenType implements TokenType {
  BANG_EQUAL("BANG_EQUAL", "!="),
  EQUAL_EQUAL("EQUAL_EQUAL", "=="),
  GREATER_EQUAL("GREATER_EQUAL", ">="),
  LESS_EQUAL("LESS_EQUAL", "<="),
  IF("IF", "if"),
  OR("OR", "or");

  private final String type;
  private final String lexeme;

  TwoCharTokenType(String type, String lexeme) {
    this.type = type;
    this.lexeme = lexeme;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String lexeme() {
    return lexeme;
  }

  @Override
  public int length() {
    return 2;
  }

  @Override
  public String toString() {
    return type;
  }
}

enum EOFTokenType implements TokenType {
  EOF("EOF", "");

  private final String type;
  private final String lexeme;

  EOFTokenType(String type, String lexeme) {
    this.type = type;
    this.lexeme = lexeme;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String lexeme() {
    return lexeme;
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public String toString() {
    return type;
  }
}

enum UnexpectedTokenType implements TokenType {
  UNEXPECTED_TOKEN_TYPE("UNEXPECTED_TOKEN_TYPE", "");

  private final String type;
  private final String lexeme;

  UnexpectedTokenType(String type, String lexeme) {
    this.type = type;
    this.lexeme = lexeme;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String lexeme() {
    return lexeme;
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public String toString() {
    return type;
  }
}