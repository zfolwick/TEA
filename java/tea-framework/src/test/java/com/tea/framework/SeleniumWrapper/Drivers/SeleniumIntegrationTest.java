package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.Actions.ActionStrategy;
import com.tea.framework.Actions.DoAction;
import com.tea.framework.parser.ActionLine;
import com.tea.framework.parser.TeaFile;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SeleniumIntegrationTest
{

    /**
     * This is the final test to prove out that the framework will take in a text file and loop over the product, iterating completion.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void CanRunASeleniumTest() throws FileNotFoundException {
        String teaFile = "src/test/resources/LoginWithFailingUsername.tea";
        TeaFile tea = new TeaFile(teaFile);
        WebBrowser browser = new LoggingBrowser(new WebBrowser());
        browser.Start(BrowserList.Chrome);
        browser.GoTo(tea.getUrl());

        for (ActionLine line : tea.getActionLines() )
        {
            DoAction doAction = new DoAction(browser, line.getAction());
            ActionStrategy strategy = doAction.SelectBy(line.getBy());
            strategy.Execute(line.getText());
        }

        browser.Quit();
    }
}