package lexer;

class Cursor {
  int line;
  int column;

  Cursor() {
    this.line = 0;
    this.column = 0;
  }

  Cursor(Cursor cursor) {
    this.line = cursor.line;
    this.column = cursor.column;
  }
}