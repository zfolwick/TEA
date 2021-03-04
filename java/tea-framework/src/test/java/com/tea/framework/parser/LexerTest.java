package com.tea.framework.parser;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;

public class LexerTest {
    @ParameterizedTest
    @ValueSource( strings = {"#here is a comment.", "#here is a comment with a keyword click"})
    public void CanTokenizeAComment(String expression) {
        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_LINE_COMMENT, 0, expression));
            add( new Token(Token.TokenType.TOKEN_EOE, 1, null));
        }};

        AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    private static Stream<Arguments> actionLines() {
        return Stream.of(
                of(new String[] { "click", "xpath", "//iframe" }),
                of(new String[] { "click", "xpath", "//*[text() = 'Log In']" }),
                of(new String[] {"click", "id", "loginBtn" }),
                of(new String[] {"click", "tagname", "input" }),
                of(new String[] {"click", "text", "'the sun will come out tomorrow'" }),
                of(new String[] {"click", "class", "codesnippet" }),
                of(new String[] {" click", "class", "codesnippet" }), // space before
                of(new String[] {" click", " class", "codesnippet" }), // space before
                of(new String[] {" click", "class", " codesnippet" }), // space before
                of(new String[] {"      click", "class", " codesnippet" }), // spaces before
                of(new String[] {"\tclick", "class", "codesnippet" }), // tabs before
                of(new String[] {"click", "\tclass", "codesnippet" }), // tabs before
                of(new String[] {"click", "class", "\tcodesnippet" }), // tabs before
                of(new String[] {"\tclick", "\tclass", "\tcodesnippet" }) // tabs everywhere
        );
    }

    private static Stream<Arguments> actionLinesWithComments() {
        return Stream.of(
                of(new String[] { "click",  "xpath",  "//iframe/div",  "# here is a comment" }),
                of(new String[] { "click", "xpath", "//*[text() = 'Log In']", "# here is a comment" })
        );
    }

    private static Stream<Arguments> actionLinesWithUrl() {
        return Stream.of(
            of("http://www.example.com"),
            of("https://www.example.com")
        );
    }

    private static Stream<Arguments> multiLineComments() {
        return Stream.of(
                of()
        );
    }
    @ParameterizedTest
    @MethodSource("actionLines")
    public void CanTokenizeAnActionLine(String action, String selector, String locator) {
        String expression = String.join(" ", new String[] {action.trim(), selector.trim() , locator.trim()});

        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_ACTION, 0, action.trim()));
            add( new Token(Token.TokenType.TOKEN_SELECTOR, 1, selector.trim()));
            add( new Token(Token.TokenType.TOKEN_LOCATOR, 2, locator.trim()));
            add( new Token(Token.TokenType.TOKEN_EOE, 3, null));
        }};


        AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    @ParameterizedTest
    @MethodSource("actionLinesWithComments")
    public void CanTokenizeAComment_EndOfALine(String action,
                                               String selector,
                                               String locator,
                                               String comment)
    {
        String expression = String.join(" ", new String[] {action.trim(), selector.trim() , locator.trim(), comment});

        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_ACTION, 0, action));
            add( new Token(Token.TokenType.TOKEN_SELECTOR, 1, selector));
            add( new Token(Token.TokenType.TOKEN_LOCATOR, 2, locator));
            add( new Token(Token.TokenType.TOKEN_LINE_COMMENT, 3, comment));
            add( new Token(Token.TokenType.TOKEN_EOE, 4, null));
        }};

        AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    @ParameterizedTest
    @MethodSource("actionLinesWithUrl")
    public void CanTokenizeAUrl(String expression) {
        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_URL, 0, expression));
            add( new Token(Token.TokenType.TOKEN_EOE, 1, null));
        }};

        AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    @Test
    public void CanTokenizeATestCase() {
        String expression = "Test Case: This is a Test Case";
        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_TC_DESC, 0, expression));
        }};

        AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    @Test
    @MethodSource("multiLineComments")
    @Disabled("This test cannot run until the lexer can handle multiple lines.")
    public void CanTokenizeABlockComment() {
        String startOfBlock = "<# Test Case: This is a Test Case";
        String endOfBlock = "Test Case: This is a Test Case #>";
        String expression = startOfBlock + System.lineSeparator() + endOfBlock;
        // block comments might come in different expressions. If a TOKEN_LF_COMMENT is seen, then the following tokens are all of type Token.TokenType.TOKEN_LINE_COMMENT until a token of tokentype TOKENTYPE.TOKEN_RT_COMMENT
        List<Token> expectedTokens = new ArrayList<Token>() {{
            add( new Token(Token.TokenType.TOKEN_LF_COMMENT, 0, "<#"));
            add( new Token(Token.TokenType.TOKEN_LINE_COMMENT, 0, startOfBlock.substring(3)));
            add( new Token(Token.TokenType.TOKEN_RT_COMMENT, 0, endOfBlock.substring(0, endOfBlock.length() - 2)));
        }};


         AssertTokensEqual(expectedTokens, new Lexer(new SourceScanner(expression)));
    }

    private static void AssertTokensEqual( List<Token> expectedTokens, Lexer lexer) {
        assertTrue(expectedTokens.size() > 0);
        for (Token e : expectedTokens) {
            Token actualToken = lexer.readNext();

            assertEquals(e.getType(), actualToken.getType());
            assertEquals(e.getPosition(), actualToken.getPosition());
            assertEquals(e.getValue(), actualToken.getValue());
        }
    }
}
