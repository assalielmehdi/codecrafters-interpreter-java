package lexer;

import java.util.List;


class Source {
  private final List<String> lines;
  private Cursor cursor;

  private Source(List<String> lines) {
    this.lines = lines;
    this.cursor = new Cursor();
  }

  static Source from(String content) {
    return new Source(content.lines().toList());
  }

  boolean atEOF() {
    return this.cursor.line >= this.lines.size();
  }

  char poll() {
    var symbol = this.peek();
    this.advance();

    return symbol;
  }

  char peek() {
    if (this.atEOF()) {
      return '\0';
    }

    return this.lines.get(this.cursor.line).charAt(this.cursor.column);
  }

  void seek(Cursor cursor) {
    this.cursor = new Cursor(cursor);
  }

  Cursor position() {
    return new Cursor(this.cursor);
  }

  void skipWhitespaces() {
    while (!atEOF() && Character.isWhitespace(this.peek())) {
      this.advance();
    }
  }

  void advance() {
    // Already at the end of the source
    if (this.atEOF()) {
      return;
    }

    this.cursor.column++;

    // Reached the end of the current line
    if (this.cursor.column >= this.lines.get(this.cursor.line).length()) {
      this.cursor.line++;
      this.cursor.column = 0;
    }
  }

  void advance(int charCount) {
    while (charCount-- > 0) {
      this.advance();
    }
  }
}
