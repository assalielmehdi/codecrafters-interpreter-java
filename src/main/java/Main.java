import lexer.Scanner;
import parser.AstPrinter;
import parser.Interpreter;
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
      case "evaluate" -> evaluate(filepath);
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

    if (!scanner.getErrors().isEmpty()) {
      scanner.getErrors().forEach(System.err::println);
      System.exit(65);
    }
  }

  static void scan(String filepath) {
    var scanner = new Scanner(readFile(filepath));
    scanner.scan();

    var parser = new Parser(scanner.getTokens());
    parser.parse();

    if (!parser.getErrors().isEmpty()) {
        parser.getErrors().forEach(System.err::println);
        System.exit(65);
    }

    parser.getExpressions().forEach((expr) -> System.out.println(AstPrinter.getInstance().print(expr)));

    if (!scanner.getErrors().isEmpty()) {
      scanner.getErrors().forEach(System.err::println);
      System.exit(65);
    }
  }

  private static void evaluate(String filepath) {
    var scanner = new Scanner(readFile(filepath));
    scanner.scan();

    var parser = new Parser(scanner.getTokens());
    parser.parse();

    if (!parser.getErrors().isEmpty()) {
      parser.getErrors().forEach(System.err::println);
      System.exit(65);
    }

    parser.getExpressions().forEach((expr) -> System.out.println(print(Interpreter.getInstance().evaluate(expr))));

    if (!scanner.getErrors().isEmpty()) {
      scanner.getErrors().forEach(System.err::println);
      System.exit(65);
    }
  }

  private static Object print(Object value) {
    return switch (value) {
      case null -> "nil";
      case Double dv when Math.floor(dv) == dv -> dv.longValue();
      default -> value;
    };
  }
}