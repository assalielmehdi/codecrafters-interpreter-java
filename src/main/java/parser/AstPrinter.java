package parser;

import java.util.Arrays;

public class AstPrinter implements Visitor<String> {
  public String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator().lexeme(), expr.left(), expr.right());
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expr());
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    return expr.literal() == null ? "nil" : expr.literal().toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator().lexeme(), expr.right());
  }

  private String parenthesize(String name, Expr... exprs) {
    return "(" + name + " " + String.join(" ", Arrays.stream(exprs).map(this::print).toList()) + ")";
  }
}
