package lexer;

import java.util.Set;

sealed interface TokenType permits OneCharTokenType, EOFTokenType {
  Set<Class<? extends TokenType>> FIXED_LENGTH_TOKEN_TYPES = Set.of(OneCharTokenType.class);

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