package lexer;


import java.util.ArrayList;
import java.util.List;


public class Scanner {
  private final String source;
  private int start, current;
  private int line = 1;

  private final List<Token> tokens = new ArrayList<>();
  private final List<String> errors = new ArrayList<>();

  private boolean alreadyScanned = false;

  public Scanner(String source) {
    this.source = source;
  }

  public void scan() {
    if (this.alreadyScanned) {
      throw new IllegalStateException("An instance of Scanner can only be used to scan once.");
    }

    while (!atEOF()) {
      start = current;
      scanToken();
    }

    tokens.add(Token.EOF);
    this.alreadyScanned = true;
  }

  private void scanToken() {
    var symbol = poll();

    switch (symbol) {
      case '(' -> this.tokens.add(new Token(Token.Type.LEFT_PAREN, "(", null, line));
      case ')' -> this.tokens.add(new Token(Token.Type.RIGHT_PAREN, ")", null, line));
      case '{' -> this.tokens.add(new Token(Token.Type.LEFT_BRACE, "{", null, line));
      case '}' -> this.tokens.add(new Token(Token.Type.RIGHT_BRACE, "}", null, line));
      case ',' -> this.tokens.add(new Token(Token.Type.COMMA, ",", null, line));
      case '.' -> this.tokens.add(new Token(Token.Type.DOT, ".", null, line));
      case '-' -> this.tokens.add(new Token(Token.Type.MINUS, "-", null, line));
      case '+' -> this.tokens.add(new Token(Token.Type.PLUS, "+", null, line));
      case ';' -> this.tokens.add(new Token(Token.Type.SEMICOLON, ";", null, line));
      case '*' -> this.tokens.add(new Token(Token.Type.STAR, "*", null, line));
      case '/' -> {
        if (match('/')) {
          while (!atEOF() && peek() != '\n') {
            poll();
          }
        } else {
          this.tokens.add(new Token(Token.Type.SLASH, "/", null, line));
        }
      }

      case '!' -> this.tokens.add(match('=')
        ? new Token(Token.Type.BANG_EQUAL, "!=", null, line)
        : new Token(Token.Type.BANG, "!", null, line));
      case '=' -> this.tokens.add(match('=')
        ? new Token(Token.Type.EQUAL_EQUAL, "==", null, line)
        : new Token(Token.Type.EQUAL, "=", null, line));
      case '<' -> this.tokens.add(match('=')
        ? new Token(Token.Type.LESS_EQUAL, "<=", null, line)
        : new Token(Token.Type.LESS, "<", null, line));
      case '>' -> this.tokens.add(match('=')
        ? new Token(Token.Type.GREATER_EQUAL, ">=", null, line)
        : new Token(Token.Type.GREATER, ">", null, line));

      case '"' -> {
        while (!atEOF() && peek() != '"') {
          if (poll() == '\n') {
            line++;
          }
        }

        if (atEOF()) {
          this.errors.add(String.format("[line %d] Error: Unterminated string.", line));
          return;
        }

        poll();
        var lexeme = source.substring(start + 1, current - 1);
        this.tokens.add(new Token(Token.Type.STRING, '"' + lexeme + '"', lexeme, line));
      }

      case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
        while (Character.isDigit(peek())) {
          poll();
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
          do {
            poll();
          } while (Character.isDigit(peek()));
        }

        var lexeme = source.substring(start, current);
        this.tokens.add(new Token(Token.Type.NUMBER, lexeme, Double.parseDouble(lexeme), line));
      }

      case '\n' -> line++;
      case ' ', '\t', '\r' -> {}

      default -> this.errors.add(String.format("[line %d] Error: Unexpected character: %s", line, symbol));
    }
  }

  private boolean atEOF() {
    return current >= source.length();
  }

  private char peek() {
    return atEOF() ? '\0' : source.charAt(current);
  }

  private char peekNext() {
    return current + 1 >= source.length() ? '\0' : source.charAt(current + 1);
  }

  private char poll() {
    return atEOF() ? '\0' : source.charAt(current++);
  }

  private boolean match(char symbol) {
    if (peek() != symbol) {
      return false;
    }

    // Always true
    return poll() == symbol;
  }

  public List<Token> getTokens() {
    return tokens;
  }

  public List<String> getErrors() {
    return errors;
  }
}
