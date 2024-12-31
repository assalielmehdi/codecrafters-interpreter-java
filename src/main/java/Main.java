import lexer.Scanner;
import parser.AstPrinter;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: ./your_program.sh <command> <filename>");
      System.exit(64);
    }

    var command = args[0];
    var filepath = args[1];

    switch (command) {
      case "tokenize" -> tokenize(filepath);
      case "parse" -> scan(filepath);
      default -> {
        System.err.println("Unknown command: " + command);
        System.exit(64);
      }
    }
  }

  static String readFile(String filepath) {
    var content = "";
    try {
      content = Files.readString(Path.of(filepath));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(65);
    }
    return content;
  }

  static void tokenize(String filepath) {
    var scanner = new Scanner(readFile(filepath));
    scanner.scan();

    scanner.getTokens().forEach(System.out::println);

    var errors = scanner.getErrors();
    errors.forEach(System.err::println);
    if (!errors.isEmpty()) {
      System.exit(65);
    }
  }

  static void scan(String filepath) {
    var scanner = new Scanner(readFile(filepath));
    scanner.scan();

    var parser = new Parser(scanner.getTokens());
    parser.parse();

    var printer = new AstPrinter();

    parser.getExpressions().forEach((expr) -> System.out.println(printer.print(expr)));

    var errors = parser.getErrors();
    errors.forEach(System.err::println);
    if (!errors.isEmpty()) {
      System.exit(65);
    }
  }
}