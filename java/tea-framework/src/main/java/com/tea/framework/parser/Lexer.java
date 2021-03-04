package com.tea.framework.parser;

/*
I need to be capable of writing a parser that can parse a file containing html DOM element attribute type, an action to perform on them, and the DOM identifier to use when looking for them.
e.g.,
# here is a comment.
<# this is a block comment.
it will keep being ignored by the compiler until it finds the ending comment.
#>

http://www.example.com

Test Case: This test tries to log in using a random username.
click   xpath   //*[text() = 'Log In']
click   xpath   //*[@id='loginUsername']
type    id      random
# here's another comment   <# it's possible to block comment on the same line #>
click   id      loginPassword
type    id      password
click   xpath   //fieldset[5]/button


Test cases are defined in ACTION    SELECTOR    LOCATOR and can be read as: "perform an ACTION using a SELECTOR on a LOCATION on the webpage under test".

ACTION
The second column represents actions that can be taken on an element.  Actions that are currently supported are: click, type.  Soon asserts will be added.

SELECTOR
column 1 is the DOM element attribute to look for.  In the first case, we're looking for xpath, the second case we're looking for id.  Selenium has support for: css class, css id, text, xpath, tagname, name, partialLinkText, and this is what the first column represents.
This maps to the selenium By.{selector}

LOCATOR
The 3rd column is the value that should be used with the selector. For selectors of type 'xpath', the Locator should be a valid xpath.

Each line is a token type.
- lines starting with "http://" and "https://" are a TOKEN_URL and the rest of the url is part of the token
- lines starting with "Test Case:" are a TOKEN_TCDESC and the rest of the line is part of the token
- lines starting with # are a comment and the token is the rest of the line
- text with <# and #> are block comments and are of type TOKEN_BLKCOMMENT.  the comment is the token value
- text that is the first on the line, and consists of: id, text, xpath, tagname, name, partialLinkText, and ends with a tab or a space are of type TOKEN_SELECTOR
- text that has a tab or space before and after, and the text consists of "click" and "type" are of type TOKEN_ACTION.  asserts will be added later.
- text that follows that is of type TEXTLOCATOR
*/

import java.util.HashMap;
import java.util.Stack;
import java.util.function.BiFunction;

/*
    This lexer turns a string expression into a token.
*/
public class Lexer {

    private HashMap<String, BiFunction<Integer, String, Token>> map = new HashMap<String, BiFunction<Integer, String, Token>>() {{
        put("click", (p, v) -> new Token(Token.TokenType.TOKEN_ACTION, p, v));
        put("type", (p, v) -> new Token(Token.TokenType.TOKEN_ACTION, p, v));
        put("xpath", (p, v) -> new Token(Token.TokenType.TOKEN_SELECTOR, p, v));
        put("id", (p, v) -> new Token(Token.TokenType.TOKEN_SELECTOR, p, v));
        put("tagname", (p, v) -> new Token(Token.TokenType.TOKEN_SELECTOR, p, v));
        put("text", (p, v) -> new Token(Token.TokenType.TOKEN_SELECTOR, p, v));
        put("class", (p, v) -> new Token(Token.TokenType.TOKEN_SELECTOR, p, v));
        put(System.lineSeparator(), (p, v) -> new Token(Token.TokenType.TOKEN_NEWLINE, p, v));  //XPATH
    }};


    private SourceScanner scanner;
    private Stack<Token> tokens = new Stack<>();


    public Lexer(SourceScanner sourceScanner) {
        scanner = sourceScanner;
    }

    public Token readNext() throws IllegalArgumentException {
        Token token = new Token(null, 0, null);
        ;

        //- lines starting with # are a comment and the token is the rest of the line
        // RemoveComments();
        tokenizeLineComments(token);
        if (token.getType() != null) {
            tokens.push(token);
            return token;
        }

        // deal with EOE
        if (scanner.atEndOfSource()) {
            token = new Token(Token.TokenType.TOKEN_EOE, (tokens.size() == 0) ?
                    scanner.getPosition() :
                    tokens.pop().getPosition() + 1, null);

        } else if (scanner.peek() != null && map.containsKey(scanner.peek()))
            token = map.get(scanner.peek()).apply(scanner.getPosition(), scanner.read());

        else if (isXpath() || (tokens.size() > 0 && tokens.peek().getType().equals(Token.TokenType.TOKEN_SELECTOR))) {
            token = new Token(null, 0, null);
            tokenizeLocator(token);
        }

        //- lines starting with "http://" and "https://" are a TOKEN_URL and the rest of the url is part of the token
        else if (isUrl())
            token = new Token(
                    Token.TokenType.TOKEN_URL, scanner.getPosition(), scanner.read());

            //- lines starting with "Test Case:" are a TOKEN_TCDESC and the rest of the line is part of the token

        else if (isTestCase()) {
            tokenizeTestCaseLine(token);
        }

        if (token.getType() == null)
            throw new IllegalArgumentException(
                    String.format("could not parse {%s} at position {%s}.", scanner.peek(), scanner.getPosition()));

        tokens.push(token);

        return token;
    }

    private void tokenizeLocator(Token token) {
        // handle space
        StringBuilder sb = new StringBuilder();
        int startpos = scanner.getPosition();

        // build the xpath word
        while (scanner != null && scanner.peek() != null && !scanner.atEndOfSource()) {
            String next = scanner.peek();
            if (next == null) {
                token.setType(Token.TokenType.TOKEN_EOE);
                token.setPosition(startpos);
                token.setValue(null);
            }

            if (next.contains("#")) break;

            // append a string to the xpath.
            sb.append(" ");
            // xpath has quotes in it.
            if (next.contains("'")) {
                while (scanner.peek() != null && !scanner.peek().contains("#")) {
                    sb.append(scanner.read()).append(" ");
                }
            }

            if (scanner.peek() != null && !scanner.peek().contains("#"))
                sb.append(scanner.read());

            else break;
        }

        token.setType(Token.TokenType.TOKEN_LOCATOR);
        token.setPosition(startpos);
        token.setValue(sb.toString().trim());
    }

    private void tokenizeTestCaseLine(Token token) {
        StringBuilder sb = new StringBuilder();
        int startpos = consumeRestOfLineIntoToken(sb);

        token.setPosition(startpos);
        token.setType(Token.TokenType.TOKEN_TC_DESC);
        token.setValue(sb.toString().trim());
    }

    private int consumeRestOfLineIntoToken(StringBuilder sb) {
        String peek = scanner.peek();
        int startpos = scanner.getPosition();

        while (peek != null && !peek.contains(System.lineSeparator())) {
            sb.append(" ").append(scanner.read());
            peek = scanner.peek();
        }

        if (peek != null) sb.append(" ").append(peek);

        return startpos;
    }

    private Boolean isTestCase() {
        scanner.push();
        String current = scanner.read();
        String next = scanner.read();
        scanner.pop();
        // 
        return scanner.peek() != null &&
                scanner.peek().startsWith("Test") &&
                next.startsWith("Case:");
        // the next word after "Test" is "Case".  If not, don't mutate the position.
    }

    private Boolean isUrl() {
        return scanner.peek() != null && scanner.peek().startsWith("http://") || scanner.peek().startsWith("https://");
    }

    private void tokenizeLineComments(Token t) {
        String peek = scanner.peek();

        if (peek != null && !peek.startsWith("#"))
            return;

        StringBuilder sb = new StringBuilder();
        int startpos = consumeRestOfLineIntoToken(sb);
        startpos = tokens.size();

        if (peek == null) {
            t.setType(Token.TokenType.TOKEN_EOE);
            t.setPosition(startpos);
            t.setValue(null);
            return;
        }

        t.setType(Token.TokenType.TOKEN_LINE_COMMENT);
        t.setPosition(startpos);
        t.setValue(sb.toString().trim());
    }

    private Boolean isXpath() {
        return scanner.peek() != null & scanner.peek().startsWith("//") && tokens.peek().getValue() == "xpath";
    }
}