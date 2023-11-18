import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;
    private boolean isAtEnd = false;

    public Scanner(String source) {
        this.source = source;
    }

    /**
     * Parse the source file for language tokens.
     * @return List of tokens parsed from the source.
     */
    public List<Token> scanTokens() {
        // Keep scanning tokens until we have consumed all characters
        // from the source string
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Scan a single language token from the source.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            // Single character tokens
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            // One or two character tokens
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;
            case '/':
                if (match('/')) {  // comment, should be ignored
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            default:
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    /**
     * Consume a character from the source and return it.
     * Advances the current source character index as a side effect.
     * @return Next character from source.
     */
    private char advance() {
        char next_char = source.charAt(current);
        current++;
        return next_char;
    }

    /**
     * @return the next character in the source without consuming it.
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Peeks at the next character from the current one and returns whether
     * the argument matches the next character.
     * @param expected Character to match with the next character in source.
     * @return true on matching next character, false otherwise.
     */
    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    /**
     * Add a token of given type to the running list of tokens.
     * @param type Type of the language token.
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Add a token of given type and literal to the running list of tokens.
     * @param type Type of the language token.
     * @param literal TODO
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * @return true if current character index has exceeded the length of the source file,
     *  false otherwise.
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }
}
