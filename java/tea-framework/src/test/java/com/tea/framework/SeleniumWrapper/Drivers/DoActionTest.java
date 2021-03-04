package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.Actions.ActionStrategy;
import com.tea.framework.Actions.DoAction;
import com.tea.framework.parser.ActionLine;
import com.tea.framework.parser.BrowserActionLine;
import com.tea.framework.parser.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DoActionTest
{

    @Test
    public void CanSelectFromBrowserActionLine()
    {
        String act1 = "click";
        String selector1 = "xpath";
        String locator1 = "//html";

        List<Token> list = new ArrayList<Token>(){{
            add(new Token(Token.TokenType.TOKEN_ACTION, 0, act1));
            add(new Token(Token.TokenType.TOKEN_ACTION, 1, selector1));
            add(new Token(Token.TokenType.TOKEN_ACTION, 2, locator1));
            add(new Token(Token.TokenType.TOKEN_EOE, 3, null));
        }};
        BrowserActionLine browserActionLine = new BrowserActionLine(list);

        WebBrowser browser = new LoggingBrowser(new WebBrowser());
        browser.Start(BrowserList.Chrome);
        DoAction by;
        for ( ActionLine line : browserActionLine.getActionLines())
        {
            by = new DoAction( browser, line.getAction());
            ActionStrategy actionStrategy = by.SelectBy(line.getBy());
            Assertions.assertNotNull(actionStrategy);

        }

        browser.Quit();
    }
}
