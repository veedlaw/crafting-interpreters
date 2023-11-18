public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    // Track which line a token appears on
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return "Token(type=" + type + ", lexeme='" + lexeme + "', literal=" + literal + ")";
    }
}
