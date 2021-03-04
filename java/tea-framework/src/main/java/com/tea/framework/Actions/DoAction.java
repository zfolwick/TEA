package com.tea.framework.Actions;


import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;

public class DoAction {
    private WebBrowser _browser;
    private String _actionToPerform;

    public DoAction(WebBrowser browser, String action) {
        _browser = browser;
        _actionToPerform = action;
    }

    public ActionStrategy SelectBy(String by) {
        switch (by) {
            case SelectorOptions.By.Text:
                return new TextStrategy(_browser, _actionToPerform);

            case SelectorOptions.By.Id:
                return new IdStrategy(_browser, _actionToPerform);

            case SelectorOptions.By.Xpath:
                return new XpathStrategy(_browser, _actionToPerform);

            default:
                throw new IllegalArgumentException("Invalid By selector chosen: {" + by + "}");
        }
    }
}

