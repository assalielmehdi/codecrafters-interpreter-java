package lexer;


public record Token(
  TokenType type,
  String lexeme,
  Object literal,
  int line,
  int column
) {
  // Position information are irrelevant for EOF token.
  static Token EOF = new Token(EOFTokenType.EOF, EOFTokenType.EOF.lexeme(), null, -1, -1);

  @Override
  public String toString() {
    return String.format("%s %s %s", this.type, this.lexeme, this.literal);
  }
}
