package errors;

import java.util.ArrayList;
import java.util.List;

public class Errors {
  private final static List<Error> errors = new ArrayList<>();

  public static void reportError(Error error) {
    errors.add(error);
  }

  public static boolean hasErrors() {
    return !errors.isEmpty();
  }

  public static int printErrors() {
    int exitStatus = 0;

    for (Error error : errors) {
      System.err.println(error.message());

      if (exitStatus == 0) {
        exitStatus = error.exitCode();
      }
    }

    return exitStatus;
  }
}
