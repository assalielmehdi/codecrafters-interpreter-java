package lexer;


import java.util.ArrayList;
import java.util.List;


public class Scanner {
  private final Source source;
  private final StateMachine stateMachine;

  public Scanner(String content) {
    this.source = Source.from(content);
    this.stateMachine = new StateMachine();
  }

  public List<Token> scan() {
    List<Token> tokens = new ArrayList<>();

    for (
      var token = nextToken();
      !token.type().equals(EOFTokenType.EOF);
      token = nextToken()
    ) {
      tokens.add(token);
    }

    return tokens;
  }

  private Token nextToken() {
    return this.stateMachine.parseLongest(this.source);
  }
}
