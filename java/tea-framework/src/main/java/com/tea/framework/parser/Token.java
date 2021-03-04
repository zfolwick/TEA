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
- lines starting with "http://" and "https://" are a TOKEN_URL
- lines starting with "Test Case:" are a TOKEN_TC_DESC
- lines starting with # are a ignored by the compiler
- text with <# and #> are block comments and are of type TOKEN_BLK_COMMENT
- text that is the first on the line, and consists of: id, text, xpath, tagname, name, partialLinkText, and ends with a tab or a space are of type TOKEN_SELECTOR
- text that has a tab or space before and after, and the text consists of "click" and "type" are of type TOKEN_ACTION.  asserts will be added later.
- text that follows that is not a comment is of type TEXT_LOCATOR
*/
public class Token {
    public enum TokenType {
        TOKEN_EOE,          // end of expression
        TOKEN_ACTION,       // click, type
        TOKEN_LINE_COMMENT, // #.  Ends after newline.
        TOKEN_LF_COMMENT,   // <# characters
        TOKEN_RT_COMMENT,   // #> characters
        TOKEN_SELECTOR,     // id, xpath, tagname, name, text
        TOKEN_LOCATOR,      // criteria for finding an element by css, xpath, or text
        TOKEN_NEWLINE,      // \n on *nix, \r\n on windows.
        TOKEN_TC_DESC,    // "Test Case:" defines a new test. Test case ends after 2 newlines.
        TOKEN_URL         // http:// or https://
    }

    private TokenType type;
    private int position;
    private String value;

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Token(TokenType type, int position, String value) {
        setType(type);
        setPosition(position);
        setValue(value);
    }
}