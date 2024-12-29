package lexer;


import java.util.ArrayList;
import java.util.List;


public class Scanner {
  private final Source source;
  private final StateMachine stateMachine;

  private final List<Token> tokens = new ArrayList<>();
  private final List<String> errors = new ArrayList<>();

  private boolean alreadyScanned = false;

  public Scanner(String content) {
    this.source = Source.from(content);
    this.stateMachine = new StateMachine();
  }

  public void scan() {
    if (this.alreadyScanned) {
      throw new IllegalStateException("An instance of Scanner can only be used to scan once.");
    }

    for (
      var token = nextToken();
      !token.type().equals(EOFTokenType.EOF);
      token = nextToken()
    ) {
      if (token.type().equals(UnexpectedTokenType.UNEXPECTED_TOKEN_TYPE)) {
        this.reportError(token);
      } else {
        tokens.add(token);
      }
    }

    tokens.add(Token.EOF);

    this.alreadyScanned = true;
  }

  private Token nextToken() {
    return this.stateMachine.parseLongest(this.source);
  }

  public List<Token> getTokens() {
    return tokens;
  }

  public List<String> getErrors() {
    return errors;
  }

  private void reportError(Token token) {
    this.errors.add(String.format("[line %d] Error: Unexpected character: %s", token.line() + 1, token.lexeme()));
  }
}
