package com.tea.framework.SeleniumWrapper.Elements;

import org.openqa.selenium.By;

public abstract class Element {
    protected By by;
    protected String text;
    protected boolean enabled;
    protected boolean displayed;

    public abstract void TypeText(String text);

    public abstract void Click();

    public abstract String GetAttribute(String attributeName);

    public By getBy() {
        return by;
    }

    public String getText() {
        return text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisplayed() {
        return displayed;
    }
}