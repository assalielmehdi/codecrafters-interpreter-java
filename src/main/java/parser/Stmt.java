package parser;

public sealed interface Stmt permits Stmt.Expression, Stmt.Print {
  interface Visitor<R> {
    R visitExpressionStmt(Stmt.Expression stmt);

    R visitPrintStmt(Stmt.Print stmt);
  }

  <R> R accept(Visitor<R> visitor);

  record Expression(Expr expr) implements Stmt {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStmt(this);
    }
  }

  record Print(Expr expr) implements Stmt {

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStmt(this);
    }
  }
}
