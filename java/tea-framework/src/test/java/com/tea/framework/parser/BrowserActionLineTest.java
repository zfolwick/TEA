package com.tea.framework.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrowserActionLineTest {

    @Test
    public void CanLoadBrowserActionLine() {
        String act1 = "click";
        String selector1 = "xpath";
        String locator1 = "//*[text() = 'Log In']";
        String act2 = "type";
        String selector2 = "id";
        String locator2 = "loginUserName";
        String act3 = "click";
        String selector3 = "name";
        String locator3 = "passwordField";
        String act4 = "type";
        String selector4 = "password";
        String locator4 = "//*[@id='password']";
        List<Token> list = new ArrayList<Token>(Arrays.asList(
            new Token(Token.TokenType.TOKEN_ACTION, 0, act1),
            new Token(Token.TokenType.TOKEN_ACTION, 1, selector1),
            new Token(Token.TokenType.TOKEN_ACTION, 2, locator1),
            new Token(Token.TokenType.TOKEN_ACTION, 0, act2),
            new Token(Token.TokenType.TOKEN_ACTION, 1, selector2),
            new Token(Token.TokenType.TOKEN_ACTION, 2, locator2),
            new Token(Token.TokenType.TOKEN_ACTION, 0, act3),
            new Token(Token.TokenType.TOKEN_ACTION, 1, selector3),
            new Token(Token.TokenType.TOKEN_ACTION, 2, locator3),
            new Token(Token.TokenType.TOKEN_ACTION, 0, act4),
            new Token(Token.TokenType.TOKEN_ACTION, 1, selector4),
            new Token(Token.TokenType.TOKEN_ACTION, 2, locator4),
            new Token(Token.TokenType.TOKEN_EOE, 4, null)
        ));


        BrowserActionLine lines = new BrowserActionLine(list);
        assertEquals(String.join("\t", act1, selector1, locator1), lines.getActionLines().get(0).toString());
        assertEquals(String.join("\t", act2, selector2, locator2), lines.getActionLines().get(1).toString());
        assertEquals(String.join("\t", act1, selector3, locator3), lines.getActionLines().get(2).toString());
        assertEquals(String.join("\t", act2, selector4, locator4), lines.getActionLines().get(3).toString());
    }
}
