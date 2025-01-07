package errors;

public interface Error {
  int exitCode();
  String message();
}
