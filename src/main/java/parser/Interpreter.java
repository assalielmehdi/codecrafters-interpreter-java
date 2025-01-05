package parser;

import lexer.Token;

public class Interpreter implements Visitor<Object> {
  private static Interpreter instance;

  private Interpreter() {}

  public static Interpreter getInstance() {
    if (instance == null) {
      instance = new Interpreter();
    }

    return instance;
  }

  public Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    return null;
  }

  @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expr());
  }

  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.literal();
  }

  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    return switch (expr.operator().type()) {
      case Token.Type.MINUS -> -(double) evaluate(expr.right());
      case Token.Type.BANG -> !isTruthy(evaluate(expr.right()));
      default -> null;
    };
  }

  private boolean isTruthy(Object value) {
    return switch (value) {
      case Boolean booleanValue -> booleanValue;
      case null -> false;
      default -> true;
    };
  }
}