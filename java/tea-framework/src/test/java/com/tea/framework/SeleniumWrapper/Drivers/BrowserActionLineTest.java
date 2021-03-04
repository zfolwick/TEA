package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.parser.BrowserActionLine;
import com.tea.framework.parser.TeaFile;
import com.tea.framework.parser.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        List<Token> list = new ArrayList<Token>() {{
            add(new Token(Token.TokenType.TOKEN_ACTION, 0, act1));
            add(new Token(Token.TokenType.TOKEN_ACTION, 1, selector1));
            add(new Token(Token.TokenType.TOKEN_ACTION, 2, locator1));
            add(new Token(Token.TokenType.TOKEN_ACTION, 0, act2));
            add(new Token(Token.TokenType.TOKEN_ACTION, 1, selector2));
            add(new Token(Token.TokenType.TOKEN_ACTION, 2, locator2));
            add(new Token(Token.TokenType.TOKEN_ACTION, 0, act3));
            add(new Token(Token.TokenType.TOKEN_ACTION, 1, selector3));
            add(new Token(Token.TokenType.TOKEN_ACTION, 2, locator3));
            add(new Token(Token.TokenType.TOKEN_ACTION, 0, act4));
            add(new Token(Token.TokenType.TOKEN_ACTION, 1, selector4));
            add(new Token(Token.TokenType.TOKEN_ACTION, 2, locator4));
            add(new Token(Token.TokenType.TOKEN_EOE, 4, null));
        }};


        BrowserActionLine lines = new BrowserActionLine(list);
        Assertions.assertEquals(String.join("\t", act1, selector1, locator1), lines.getActionLines().get(0).toString());
        Assertions.assertEquals(String.join("\t", act2, selector2, locator2), lines.getActionLines().get(1).toString());
        Assertions.assertEquals(String.join("\t", act1, selector3, locator3), lines.getActionLines().get(2).toString());
        Assertions.assertEquals(String.join("\t", act2, selector4, locator4), lines.getActionLines().get(3).toString());
    }

    @Test
    public void CanInterpretATeaFileInput() throws FileNotFoundException {
        String teaFile = "src/test/resources/LoginWithFailingUsername.tea";
        String url = "https://www.reddit.com/login/?experiment_d2x_sso_login_link=enabled";
        String tc = "Test Case: Trying to login to reddit with a random username/password will fail.";
        // String action1 = "click xpath //*[text() = 'Log In']";
        String action1 = "click\txpath\t//*[@id='loginUsername']";
        String action2 = "type\tid\trandom";
        String action3 = "click\tid\tloginPassword";
        String action4 = "type\tid\tpassword";
        String action5 = "click\txpath\t//fieldset[5]/button";

        TeaFile tea = new TeaFile(teaFile);

        Assertions.assertEquals(url, tea.getUrl());
        Assertions.assertEquals(tc, tea.getTestCase());

        // Assertions.assertEquals(action1, tea.ActionLines[0].ToString());
        Assertions.assertEquals(action1, tea.getActionLines().get(0).toString());
        Assertions.assertEquals(action2, tea.getActionLines().get(1).toString());
        Assertions.assertEquals(action3, tea.getActionLines().get(2).toString());
        Assertions.assertEquals(action4, tea.getActionLines().get(3).toString());
        Assertions.assertEquals(action5, tea.getActionLines().get(4).toString());
    }
}