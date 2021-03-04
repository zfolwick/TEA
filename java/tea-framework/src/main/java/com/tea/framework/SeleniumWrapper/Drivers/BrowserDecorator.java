package com.tea.framework.SeleniumWrapper.Drivers;

import com.tea.framework.SeleniumWrapper.Elements.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class BrowserDecorator extends WebBrowser {
    protected WebBrowser browser;

    public BrowserDecorator(WebBrowser browser) {
        this.browser = browser;
    }

    @Override
    public WebDriver GetDriver() {
        return browser.GetDriver();
    }

    @Override
    public Element GetActiveElement() {
        return browser.GetActiveElement();
    }

    @Override
    public Element FindElement(By locator) {
        return browser.FindElement(locator);
    }

    @Override
    public java.util.List<Element> FindElements(By locator) {
        return browser.FindElements(locator);
    }

    @Override
    public List<Element> FindAllFrames() {
        return browser.FindAllFrames();
    }

    @Override
    public Element FindElementInFrames(By by) {
        return browser.FindElementInFrames(by);
    }

    @Override
    public void GoTo(String url) {
        browser.GoTo(url);
    }

    @Override
    public void Quit() {
        browser.Quit();
    }

    @Override
    public void Start(BrowserList browser) {
        this.browser.Start(browser);
    }
}
