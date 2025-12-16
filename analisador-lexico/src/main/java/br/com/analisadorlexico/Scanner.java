package br.com.analisadorlexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.analisadorlexico.TokenType.BANG;
import static br.com.analisadorlexico.TokenType.BANG_EQUAL;
import static br.com.analisadorlexico.TokenType.COMMA;
import static br.com.analisadorlexico.TokenType.DOT;
import static br.com.analisadorlexico.TokenType.EOF;
import static br.com.analisadorlexico.TokenType.EQUAL;
import static br.com.analisadorlexico.TokenType.EQUAL_EQUAL;
import static br.com.analisadorlexico.TokenType.GREATER;
import static br.com.analisadorlexico.TokenType.GREATER_EQUAL;
import static br.com.analisadorlexico.TokenType.LEFT_BRACE;
import static br.com.analisadorlexico.TokenType.LEFT_PAREN;
import static br.com.analisadorlexico.TokenType.LESS;
import static br.com.analisadorlexico.TokenType.LESS_EQUAL;
import static br.com.analisadorlexico.TokenType.MINUS;
import static br.com.analisadorlexico.TokenType.AND;
import static br.com.analisadorlexico.TokenType.CLASS;
import static br.com.analisadorlexico.TokenType.ELSE;
import static br.com.analisadorlexico.TokenType.FALSE;
import static br.com.analisadorlexico.TokenType.FOR;
import static br.com.analisadorlexico.TokenType.FUN;
import static br.com.analisadorlexico.TokenType.IF;
import static br.com.analisadorlexico.TokenType.NIL;
import static br.com.analisadorlexico.TokenType.OR;
import static br.com.analisadorlexico.TokenType.PRINT;
import static br.com.analisadorlexico.TokenType.RETURN;
import static br.com.analisadorlexico.TokenType.SUPER;
import static br.com.analisadorlexico.TokenType.THIS;
import static br.com.analisadorlexico.TokenType.TRUE;
import static br.com.analisadorlexico.TokenType.VAR;
import static br.com.analisadorlexico.TokenType.WHILE;
import static br.com.analisadorlexico.TokenType.IDENTIFIER;
import static br.com.analisadorlexico.TokenType.NUMBER;
import static br.com.analisadorlexico.TokenType.STRING;
import static br.com.analisadorlexico.TokenType.PLUS;
import static br.com.analisadorlexico.TokenType.RIGHT_BRACE;
import static br.com.analisadorlexico.TokenType.RIGHT_PAREN;
import static br.com.analisadorlexico.TokenType.SEMICOLON;
import static br.com.analisadorlexico.TokenType.SLASH;
import static br.com.analisadorlexico.TokenType.STAR;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();

  private int start = 0;
  private int current = 0;
  private int line = 1;

  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(':
        addToken(LEFT_PAREN);
        break;
      case ')':
        addToken(RIGHT_PAREN);
        break;
      case '{':
        addToken(LEFT_BRACE);
        break;
      case '}':
        addToken(RIGHT_BRACE);
        break;
      case ',':
        addToken(COMMA);
        break;
      case '.':
        addToken(DOT);
        break;
      case '-':
        addToken(MINUS);
        break;
      case '+':
        addToken(PLUS);
        break;
      case ';':
        addToken(SEMICOLON);
        break;
      case '*':
        addToken(STAR);
        break;
      case '/':
        if (match('/')) {
          while (peek() != '\n' && !isAtEnd())
            advance();
        } else {
          addToken(SLASH);
        }
        break;
      case '!':
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
      case '=':
        addToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      case '<':
        addToken(match('=') ? LESS_EQUAL : LESS);
        break;
      case '>':
        addToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      case ' ':
      case '\r':
      case '\t':
        break;

      case '\n':
        line++;
        break;
      case '"':
        string();
        break;
      case 'o':
        if (match('r')) {
          addToken(OR);
        }
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Lox.error(line, "Unexpected character.");
        }
        break;
    }
  }

  private void identifier() {
    while (isAlphaNumeric(peek()))
      advance();
    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null)
      type = IDENTIFIER;
    addToken(type);
  }

  private void number() {
    while (isDigit(peek()))
      advance();

    if (peek() == '.' && isDigit(peekNext())) {
      advance();

      while (isDigit(peek()))
        advance();
    }

    addToken(NUMBER,
        Double.parseDouble(source.substring(start, current)));
  }

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n')
        line++;
      advance();
    }

    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    advance();

    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private boolean match(char expected) {
    if (isAtEnd())
      return false;
    if (source.charAt(current) != expected)
      return false;
    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd())
      return '\0';
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length())
      return '\0';
    return source.charAt(current + 1);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
        (c >= 'A' && c <= 'Z') ||
        c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("fun", FUN);
    keywords.put("if", IF);
    keywords.put("nil", NIL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("return", RETURN);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
  }

}