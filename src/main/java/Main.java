import lexer.Scanner;

void main(String[] args) {
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

String readFile(String filepath) {
  var content = "";
  try {
    content = Files.readString(Path.of(filepath));
  } catch (IOException e) {
    System.err.println("Error reading file: " + e.getMessage());
    System.exit(65);
  }
  return content;
}

void tokenize(String filepath) {
  var scanner = new Scanner(readFile(filepath));
  scanner.scan();

  scanner.getTokens().forEach(System.out::println);

  var errors = scanner.getErrors();
  errors.forEach(System.err::println);

  if (!errors.isEmpty()) {
    errors.forEach(System.err::println);
    System.exit(65);
  }
}

void scan(String filepath) {
  // TODO
}