package lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class State {
  TokenType type;
  final Map<Character, Set<State>> next = new HashMap<>();
}
