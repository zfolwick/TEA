package com.tea.framework.Actions;

import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;
import org.openqa.selenium.By;

public class TextStrategy extends ActionStrategy {

    public TextStrategy(WebBrowser browser, String action) {
        super(browser, action);
    }

    @Override
    public void Execute(String value) {
        Action act = null;

        By by = By.linkText(value);
        switch (_action) {
            case SelectorOptions.Action.Click:
                act = new Click(by);
                break;
            case SelectorOptions.Action.TypeText:
                act = new TypeText(by);
                break;
            default:
                throw new IllegalArgumentException("Invalid action chosen!");
        }

        act.Perform(Browser, value);
    }
}
