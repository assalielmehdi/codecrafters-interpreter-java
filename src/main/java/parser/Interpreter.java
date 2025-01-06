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
    var leftValue = evaluate(expr.left());
    var rightValue = evaluate(expr.right());

    return switch (expr.operator().type()) {
      case Token.Type.PLUS -> {
        if (leftValue instanceof Double d1 && rightValue instanceof Double d2) {
          yield d1 + d2;
        }

        if (leftValue instanceof String d1 && rightValue instanceof String d2) {
          yield d1 + d2;
        }

        yield null;
      }
      case Token.Type.MINUS -> (double) leftValue - (double) rightValue;
      case Token.Type.STAR -> (double) leftValue * (double) rightValue;
      case Token.Type.SLASH -> (double) leftValue / (double) rightValue;
      case Token.Type.EQUAL_EQUAL -> leftValue == rightValue;
      case Token.Type.BANG_EQUAL -> leftValue != rightValue;
      case Token.Type.LESS -> (double) leftValue < (double) rightValue;
      case Token.Type.LESS_EQUAL -> (double) leftValue <= (double) rightValue;
      case Token.Type.GREATER -> (double) leftValue > (double) rightValue;
      case Token.Type.GREATER_EQUAL -> (double) leftValue >= (double) rightValue;
      case Token.Type.AND -> isTruthy(leftValue) && isTruthy(rightValue);
      case Token.Type.OR -> isTruthy(leftValue) || isTruthy(rightValue);
      default -> null;
    };
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
