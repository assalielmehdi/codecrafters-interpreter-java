import errors.Errors;
import lexer.Scanner;
import parser.AstPrinter;
import parser.Interpreter;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: ./your_program.sh <command> <filename>");
      System.exit(64);
    }

    var command = args[0];
    var filepath = args[1];

    switch (command) {
      case "tokenize" -> {
        var scanner = new Scanner(readFile(filepath)).scan();

        scanner.getTokens().forEach(System.out::println);
      }
      case "parse" -> {
        var scanner = new Scanner(readFile(filepath)).scan();
        var parser = new Parser(scanner.getTokens()).parse();

        parser.getExpressions().forEach(expr -> System.out.println(AstPrinter.getInstance().print(expr)));
      }
      case "evaluate" -> {
        var scanner = new Scanner(readFile(filepath)).scan();
        var parser = new Parser(scanner.getTokens()).parse();

        if (!Errors.hasErrors()) {
          parser.getExpressions().stream()
            .map(Interpreter.getInstance()::interpret)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(System.out::println);
        }
      }
      default -> {
        System.err.println("Unknown command: " + command);
        System.exit(64);
      }
    }

    System.exit(Errors.printErrors());
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


}