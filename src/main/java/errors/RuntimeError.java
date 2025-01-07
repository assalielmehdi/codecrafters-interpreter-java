package errors;

import lexer.Token;

public class RuntimeError extends RuntimeException implements Error {
  private final Token token;

  public RuntimeError(String message, Token token) {
    super(message);
    this.token = token;
  }

  @Override
  public int exitCode() {
    return 70;
  }

  @Override
  public String message() {
    return super.getMessage() + "\n" + "[line " + token.line() + "]";
  }
}
