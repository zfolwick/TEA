package com.tea.framework.Actions;

import com.tea.framework.SeleniumWrapper.Drivers.WebBrowser;

public abstract class ActionStrategy {
    protected String _action;
    protected WebBrowser Browser;

    public ActionStrategy(WebBrowser browser, String action) {
        _action = action;
        Browser = browser;
    }

    public abstract void Execute(String value);
}  
