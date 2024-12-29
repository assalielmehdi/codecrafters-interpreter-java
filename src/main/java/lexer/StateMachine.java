package lexer;

import utils.Pair;

import java.util.*;

class StateMachine {
  private final State start;

  StateMachine() {
    this.start = new State();

    this.buildForFixedLengthTokenTypes();
  }

  public Token parseLongest(Source source) {
    source.skipWhitespaces();
    if (source.atEOF()) {
      return Token.EOF;
    }
    var startPosition = source.position();

    var possibleTokens = new ArrayList<Token>();

    Queue<Pair<State, StringBuilder>> bfsQueue = new LinkedList<>();
    bfsQueue.add(new Pair<>(this.start, new StringBuilder()));

    while (!bfsQueue.isEmpty()) {
      // Process states of the same level at once
      Queue<Pair<State, StringBuilder>> nextBfsQueue = new LinkedList<>();
      var symbol = source.poll();

      while (!bfsQueue.isEmpty()) {
        var pair = bfsQueue.poll();
        var state = pair.first();
        var lexeme = pair.second();

        // Final state
        if (state.type != null) {
          possibleTokens.add(new Token(state.type, lexeme.toString(), null, startPosition.line, startPosition.column));
        }

        var nextStates = state.next.get(symbol);
        if (nextStates == null) {
          continue;
        }

        for (var nextState : nextStates) {
          nextBfsQueue.add(new Pair<>(nextState, new StringBuilder(lexeme).append(symbol)));
        }
      }

      bfsQueue = nextBfsQueue;
    }

    var parsedToken = possibleTokens.stream()
      .max(Comparator.comparingInt(token -> token.lexeme().length()))
      .orElse(Token.EOF);
    source.seek(startPosition);
    source.advance(parsedToken.lexeme().length());

    return parsedToken;
  }

  private void buildForFixedLengthTokenTypes() {
    for (var type : TokenType.FIXED_LENGTH_TOKEN_TYPES) {
      if (!type.isEnum()) {
        continue;
      }

      TokenType[] values = type.getEnumConstants();

      for (TokenType value : values) {
        var prevState = this.start;

        for (int i = 0; i < value.lexeme().length(); i++) {
          var state = new State();
          if (i == value.lexeme().length() - 1) {
            state.type = value;
          }

          prevState.next.computeIfAbsent(value.lexeme().charAt(i), _ -> new HashSet<>()).add(state);
          prevState = state;
        }
      }
    }
  }
}
