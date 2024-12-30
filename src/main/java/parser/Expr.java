package parser;

import lexer.Token;

public sealed interface Expr permits Expr.Binary, Expr.Grouping, Expr.Literal, Expr.Unary {
  <R> R accept(Visitor<R> visitor);

  record Binary(Expr left, Expr right, Token operator) implements Expr {
    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }
  }

  record Grouping(Expr expr) implements Expr {
    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }
  }

  record Literal(Object literal) implements Expr {
    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }
  }

  record Unary(Expr right, Token operator) implements Expr {
    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }
  }
}
