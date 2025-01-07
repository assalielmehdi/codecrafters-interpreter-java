package errors;

public record StaticError(String error) implements Error {
  @Override
  public int exitCode() {
    return 65;
  }

  @Override
  public String message() {
    return error;
  }
}
