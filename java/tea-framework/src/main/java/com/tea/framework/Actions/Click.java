package com.tea.framework.Actions;

import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;
import com.tea.framework.SeleniumWrapper.Elements.Element;
import org.openqa.selenium.By;

public class Click extends Action {

    public Click(By by) {
        super(by);
    }

    @Override
    public void Perform(WebBrowser browser, String value) {
        Element element = browser.FindElement(_by);
        element.Click();
    }
}
