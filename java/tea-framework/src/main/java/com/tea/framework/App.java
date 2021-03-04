package com.tea.framework;

import com.tea.framework.Actions.ActionStrategy;
import com.tea.framework.Actions.DoAction;
import com.tea.framework.SeleniumWrapper.Drivers.BrowserList;
import com.tea.framework.SeleniumWrapper.Drivers.LoggingBrowser;
import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;
import com.tea.framework.SeleniumWrapper.Elements.Element;
import com.tea.framework.SeleniumWrapper.Elements.ElementDecorator;
import com.tea.framework.parser.ActionLine;
import com.tea.framework.parser.TeaFile;
import com.tea.framework.parser.Token;

import java.io.FileNotFoundException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {

        String teaFile = "src/test/resources/LoginWithFailingUsername.tea";
        TeaFile tea = new TeaFile(teaFile);
        WebBrowser browser = new LoggingBrowser(new WebBrowser());
        browser.Start(BrowserList.Chrome);
        browser.GoTo(tea.getUrl());

        for (ActionLine line : tea.getActionLines()) {
            DoAction doAction = new DoAction(browser, line.getAction());
            ActionStrategy strategy = doAction.SelectBy(line.getBy());
            strategy.Execute(line.getText());
        }

        browser.Quit();
    }
}
