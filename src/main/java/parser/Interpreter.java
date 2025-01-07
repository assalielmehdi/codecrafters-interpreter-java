package parser;

import errors.Errors;
import errors.RuntimeError;
import lexer.Token;

import java.util.Optional;

public class Interpreter implements Visitor<Object> {
  private static Interpreter instance;

  private Interpreter() {}

  public static Interpreter getInstance() {
    if (instance == null) {
      instance = new Interpreter();
    }

    return instance;
  }

  public Optional<Object> interpret(Expr expr) {
    try {
      return Optional.of(evaluate(expr));
    } catch (RuntimeError error) {
      Errors.reportError(error);
      return Optional.empty();
    }
  }

  private Object evaluate(Expr expr) {
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

        throw new RuntimeError("Operands must be two numbers or two strings.", expr.operator());
      }
      case Token.Type.MINUS -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue - (double) rightValue;
      }
      case Token.Type.STAR -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue * (double) rightValue;
      }
      case Token.Type.SLASH -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue / (double) rightValue;
      }
      case Token.Type.EQUAL_EQUAL -> equals(leftValue, rightValue);
      case Token.Type.BANG_EQUAL -> !equals(leftValue, rightValue);
      case Token.Type.LESS -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue < (double) rightValue;
      }
      case Token.Type.LESS_EQUAL -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue <= (double) rightValue;
      }
      case Token.Type.GREATER -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue > (double) rightValue;
      }
      case Token.Type.GREATER_EQUAL -> {
        checkNumbers(expr.operator(), leftValue, rightValue);
        yield (double) leftValue >= (double) rightValue;
      }
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
    var rightValue = evaluate(expr.right());

    return switch (expr.operator().type()) {
      case Token.Type.MINUS -> {
        checkNumbers(expr.operator(), rightValue);
        yield -(double) rightValue;
      }
      case Token.Type.BANG -> !isTruthy(rightValue);
      default -> null;
    };
  }

  private void checkNumbers(Token operator, Object... objects) {
    for (Object o : objects) {
      if (!(o instanceof Double)) {
        throw new RuntimeError(String.format(
          "Operand%s must be%s number%s.",
          objects.length > 1 ? "s" : "",
          objects.length > 1 ? " a" : "",
          objects.length > 1 ? "s" : ""
        ), operator);
      }
    }
  }

  private boolean isTruthy(Object value) {
    return switch (value) {
      case Boolean booleanValue -> booleanValue;
      case null -> false;
      default -> true;
    };
  }

  private boolean equals(Object value1, Object value2) {
    if (value1 == null && value2 == null) {
      return true;
    }

    if (value1 == null) {
      return false;
    }

    return value1.equals(value2);
  }
}
