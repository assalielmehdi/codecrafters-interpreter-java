package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {
  private final List<Token> tokens;
  private int current = 0;

  private final List<Expr> expressions = new ArrayList<>();
  private final List<String> errors = new ArrayList<>();

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public List<Expr> getExpressions() {
    return expressions;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void parse() {
    while (!atEOF()) {
      expressions.add(expression());
    }
  }

  private Expr expression() {
    return equality();
  }

  private Expr equality() {
    var expr = comparison();

    while (match(Token.Type.BANG_EQUAL, Token.Type.EQUAL_EQUAL)) {
      var operator = poll();
      var right = comparison();
      expr = new Expr.Binary(expr, right, operator);
    }

    return expr;
  }

  private Expr comparison() {
    var expr = term();

    while (match(Token.Type.GREATER, Token.Type.GREATER_EQUAL, Token.Type.LESS, Token.Type.LESS_EQUAL)) {
      var operator = poll();
      var right = term();
      expr = new Expr.Binary(expr, right, operator);
    }

    return expr;
  }

  private Expr term() {
    var expr = factor();

    while (match(Token.Type.MINUS, Token.Type.PLUS)) {
      var operator = poll();
      var right = factor();
      expr = new Expr.Binary(expr, right, operator);
    }

    return expr;
  }

  private Expr factor() {
    var expr = unary();

    while (match(Token.Type.SLASH, Token.Type.STAR)) {
      var operator = poll();
      var right = unary();
      expr = new Expr.Binary(expr, right, operator);
    }

    return expr;
  }

  private Expr unary() {
    if (match(Token.Type.BANG, Token.Type.MINUS)) {
      poll();
      return unary();
    }

    return primary();
  }

  private Expr primary() {
    var currentToken = poll();

    return switch (currentToken.type()) {
      case Token.Type.NUMBER, Token.Type.STRING -> new Expr.Literal(currentToken.literal());
      case Token.Type.TRUE -> new Expr.Literal(true);
      case Token.Type.FALSE -> new Expr.Literal(false);
      case Token.Type.NIL -> new Expr.Literal(null);
      case Token.Type.LEFT_PAREN -> {
        var expr = expression();

        if (!match(Token.Type.RIGHT_PAREN)) {
          // TODO: ERROR
        }

        poll();
        yield new Expr.Grouping(expr);
      }
      default -> throw new RuntimeException(); // TODO: Error
    };
  }

  private boolean atEOF() {
    return current >= tokens.size() || tokens.get(current).type().equals(Token.Type.EOF);
  }

  private Token poll() {
    return tokens.get(current++);
  }

  private boolean match(Token.Type... types) {
    if (atEOF()) {
      return false;
    }

    var currentType = tokens.get(current).type();
    for (var type : types) {
      if (currentType == type) {
        return true;
      }
    }

    return false;
  }
}
