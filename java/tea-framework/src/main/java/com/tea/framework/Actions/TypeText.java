package com.tea.framework.Actions;

import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;
import org.openqa.selenium.By;

public class TypeText extends Action {
    public TypeText(By by) {
        super(by);
    }

    @Override
    public void Perform(WebBrowser browser, String value) {
        if (value == "random") {
            browser.GetActiveElement().TypeText(GetRandomString(10).replace("\"", ""));

        } else {
            browser.GetActiveElement().TypeText(value.replace("\"", ""));

        }
    }
}