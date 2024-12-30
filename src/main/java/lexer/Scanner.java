package lexer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scanner {
  private final String source;
  private int start, current;
  private int line = 1;
  private static final Map<String, Token.Type> keywords = new HashMap<>();

  static {
    keywords.put("and", Token.Type.AND);
    keywords.put("class", Token.Type.CLASS);
    keywords.put("else", Token.Type.ELSE);
    keywords.put("false", Token.Type.FALSE);
    keywords.put("for", Token.Type.FOR);
    keywords.put("fun", Token.Type.FUN);
    keywords.put("if", Token.Type.IF);
    keywords.put("nil", Token.Type.NIL);
    keywords.put("or", Token.Type.OR);
    keywords.put("print", Token.Type.PRINT);
    keywords.put("return", Token.Type.RETURN);
    keywords.put("super", Token.Type.SUPER);
    keywords.put("this", Token.Type.THIS);
    keywords.put("true", Token.Type.TRUE);
    keywords.put("var", Token.Type.VAR);
    keywords.put("while", Token.Type.WHILE);
  }

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
    switch (poll()) {
      case '(' -> addToken(Token.Type.LEFT_PAREN);
      case ')' -> addToken(Token.Type.RIGHT_PAREN);
      case '{' -> addToken(Token.Type.LEFT_BRACE);
      case '}' -> addToken(Token.Type.RIGHT_BRACE);
      case ',' -> addToken(Token.Type.COMMA);
      case '.' -> addToken(Token.Type.DOT);
      case '-' -> addToken(Token.Type.MINUS);
      case '+' -> addToken(Token.Type.PLUS);
      case ';' -> addToken(Token.Type.SEMICOLON);
      case '*' -> addToken(Token.Type.STAR);
      case '/' -> {
        if (match('/')) {
          while (!atEOF() && peek() != '\n') {
            poll();
          }
        } else {
          addToken(Token.Type.SLASH);
        }
      }
      case '!' -> addToken(match('=') ? Token.Type.BANG_EQUAL : Token.Type.BANG);
      case '=' -> addToken(match('=') ? Token.Type.EQUAL_EQUAL : Token.Type.EQUAL);
      case '<' -> addToken(match('=') ? Token.Type.LESS_EQUAL : Token.Type.LESS);
      case '>' -> addToken(match('=') ? Token.Type.GREATER_EQUAL : Token.Type.GREATER);
      case '"' -> parseString();
      case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> parseNumber();
      case '\n' -> line++;
      case ' ', '\t', '\r' -> {}
      case char symbol when Character.isAlphabetic(symbol) || symbol == '_' -> parseIdentifier();
      case char symbol -> this.errors.add(String.format("[line %d] Error: Unexpected character: %s", line, symbol));
    }
  }

  private void parseIdentifier() {
    while (Character.isAlphabetic(peek()) || Character.isDigit(peek()) || peek() == '_') {
      poll();
    }

    var lexeme = source.substring(start, current);
    var type = keywords.getOrDefault(lexeme, Token.Type.IDENTIFIER);

    this.tokens.add(new Token(type, lexeme, null, line));
  }

  private void parseNumber() {
    while (Character.isDigit(peek())) {
      poll();
    }

    if (peek() == '.' && Character.isDigit(peekNext())) {
      do {
        poll();
      } while (Character.isDigit(peek()));
    }

    var literal = Double.parseDouble(source.substring(start, current));
    addToken(Token.Type.NUMBER, literal);
  }

  private void parseString() {
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
    var literal = source.substring(start + 1, current - 1);
    addToken(Token.Type.STRING, literal);
  }

  private void addToken(Token.Type type) {
    addToken(type, null);
  }

  private void addToken(Token.Type type, Object literal) {
    var lexeme = source.substring(start, current);
    this.tokens.add(new Token(type, lexeme, literal, line));
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
