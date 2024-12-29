import lexer.Scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
//    if (args.length < 2) {
//      System.err.println("Usage: ./your_program.sh tokenize <filename>");
//      System.exit(64);
//    }
//
//    var command = args[0];
//    var filename = args[1];
//
//    if (!command.equals("tokenize")) {
//      System.err.println("Unknown command: " + command);
//      System.exit(64);
//    }

    String fileContent = "";
    try {
      fileContent = Files.readString(Path.of("test.lox"));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(65);
    }

    var scanner = new Scanner(fileContent);
    scanner.scan();

    var tokens = scanner.getTokens();
    var errors = scanner.getErrors();

    tokens.forEach(System.out::println);

    if (!errors.isEmpty()) {
      errors.forEach(System.err::println);
      System.exit(65);
    }
  }
}
